<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Common\NotificationRepository;
use App\Repositories\Staff\AppointmentRepository;
use App\Repositories\Staff\CriteriaResultRepository;
use App\Repositories\Staff\InspectionRepository;
use App\Repositories\Staff\InspectionResultRepository;
use App\Repositories\Staff\OwnerRepository;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;

class InspectionService
{
    protected $inspectionRepository;
    protected $appointmentRepository;
    protected $ownerRepository;
    protected $notificationRepository;
    protected $criteriaResultRepository;

    public function __construct(
        InspectionRepository $inspectionRepository,
        AppointmentRepository $appointmentRepository,
        OwnerRepository $ownerRepository,
        NotificationRepository $notificationRepository,
        CriteriaResultRepository $criteriaResultRepository,
    ) {
        $this->inspectionRepository = $inspectionRepository;
        $this->appointmentRepository = $appointmentRepository;
        $this->ownerRepository = $ownerRepository;
        $this->notificationRepository = $notificationRepository;
        $this->criteriaResultRepository = $criteriaResultRepository;
    }

    public function getByCenterId(User $staff)
    {
        return $this->inspectionRepository->getByCenterId($staff->center_id);
    }

    public function getDetailById(User $staff, $id)
    {
        $inspection = $this->inspectionRepository->getDetailById($staff->center_id, $id);

        if (!$inspection) {
            throw new HandlerException('Inspection not found.', 404);
        }

        return $inspection;
    }

    public function completeInspection(User $staff, $inspectionId)
    {
        return $this->submitInspection($staff, $inspectionId);
    }

    public function submitInspection(User $staff, $inspectionId)
    {
        $inspection = $this->inspectionRepository->getDetailById($staff->center_id, $inspectionId);

        if (!$inspection) {
            throw new HandlerException('Inspection not found.', 404);
        }

        if ($inspection->status !== 'in_progress') {
            throw new HandlerException('This inspection is not in progress and cannot be submitted.', 400);
        }

        // 1. Check if all criteria are inspected (none can be 'uninspected')
        $criteriaResults = $inspection->criteriaResults;
        if ($criteriaResults->isEmpty()) {
            throw new HandlerException('No criteria results found for this inspection.', 400);
        }

        foreach ($criteriaResults as $cr) {
            if ($cr->result === 'uninspected') {
                throw new HandlerException('Vui lòng hoàn thành đánh giá tất cả các tiêu chí trước khi hoàn thành.', 400);
            }
        }

        // 2. Compute overall result
        $hasFailed = false;
        $hasWarning = false;
        foreach ($criteriaResults as $cr) {
            if ($cr->result === 'failed') {
                $hasFailed = true;
            } elseif ($cr->result === 'warning') {
                $hasWarning = true;
            }
        }

        $overallResult = 'passed';
        if ($hasFailed) {
            $overallResult = 'failed';
        } elseif ($hasWarning) {
            $overallResult = 'conditional';
        }

        return DB::transaction(function () use ($staff, $inspection, $overallResult) {
            $updateData = [
                'status' => 'completed',
                'result' => $overallResult,
            ];

            // 3. If passed or conditional, generate VR certificate number and calculate next inspection date
            if ($overallResult === 'passed' || $overallResult === 'conditional') {
                $yearMonth = now()->format('Ym');
                $randomDigits = str_pad(mt_rand(1, 99999), 5, '0', STR_PAD_LEFT);
                $updateData['certificate_number'] = 'VR-' . $yearMonth . '-' . $randomDigits;

                $months = $inspection->vehicle->vehicleType->inspection_frequency_months ?? 12;
                $updateData['next_inspection_date'] = now()->addMonths($months);
            } else {
                $updateData['certificate_number'] = null;
                $updateData['next_inspection_date'] = null;
            }

            // Perform the update
            $this->inspectionRepository->update($inspection, $updateData);

            // 4. Create/update a payment record if fee applies
            $fee = $inspection->vehicle->vehicleType->inspection_fee ?? 0;
            if ($fee > 0) {
                \App\Models\Payment::updateOrCreate(
                    ['inspection_id' => $inspection->id],
                    [
                        'amount' => $fee,
                        'payment_method' => 'cash',
                        'status' => 'pending',
                        'transaction_code' => 'TXN-' . now()->format('YmdHis') . '-' . mt_rand(1000, 9999),
                        'notes' => 'Lệ phí kiểm định phương tiện ' . $inspection->vehicle->registrationNumber,
                    ]
                );
            }

            // 5. Update associated appointment status to completed (if any)
            $appointment = \App\Models\Appointment::where('inspection_id', $inspection->id)->first();
            if ($appointment) {
                $appointment->update(['status' => 'completed']);
            }

            // 6. Notify user
            $userClient = $inspection->vehicle->owner->user;
            if ($userClient) {
                $resultText = $overallResult === 'passed' ? 'Đạt' : ($overallResult === 'conditional' ? 'Đạt có điều kiện' : 'Không đạt');
                $notificationData = [
                    'user_id' => $userClient->id,
                    'title' => 'Kết quả kiểm định xe ' . $inspection->vehicle->registrationNumber,
                    'content' => 'Việc kiểm tra xe của bạn đã hoàn tất. Kết quả tổng thể: ' . $resultText . '.',
                    'notification_type' => 'inspection',
                    'related_id' => $inspection->id,
                    'related_type' => 'Inspection'
                ];
                $this->notificationRepository->create($notificationData);
            }

            return $this->getDetailById($staff, $inspection->id);
        });
    }


    public function cancelInspection(User $staff, $inspectionId)
    {
        $inspection = $this->inspectionRepository->findById($inspectionId);

        if (!$inspection) {
            throw new HandlerException('Inspection not found.', 404);
        }

        if ($inspection->center_id !== $staff->center_id) {
            throw new HandlerException('You are not authorized to complete this inspection.', 403);
        }

        return DB::transaction(function () use ($staff, $inspection) {
            $this->inspectionRepository->update(
                $inspection,
                ['status' => 'cancelled']
            );

            $userClient = $inspection->vehicle->owner->user;

            $notificationData = [
                'user_id' => $userClient->id,
                'title' => 'Việc kiểm tra xe của bạn đã bị hủy.',
                'content' => 'Việc kiểm tra xe của bạn đã bị hủy.',
                'notification_type' => 'inspection',
                'related_id' => $inspection->id,
                'related_type' => 'Inspection'
            ];

            $this->notificationRepository->create($notificationData);

            return $this->getDetailById($staff, $inspection->id);
        });
    }

    public function updateResult(User $staff, $inspectionId, array $data)
    {
        $inspection = $this->inspectionRepository->findById($inspectionId);

        if (!$inspection) {
            throw new HandlerException('Inspection not found.', 404);
        }

        if ($inspection->center_id !== $staff->center_id) {
            throw new HandlerException('You are not authorized to complete this inspection.', 403);
        }

        $result = $data['result'];

        // Check conditions

        $this->inspectionRepository->update($inspection, [
            'result' => $result
        ]);

        return true;
    }

    public function update(User $staff, $id, array $data)
    {
        $inspection = $this->inspectionRepository->findById($id);

        if (!$inspection) {
            throw new HandlerException('Inspection not found.', 404);
        }

        if ($inspection->center_id !== $staff->center_id) {
            throw new HandlerException('You are not authorized to complete this inspection.', 403);
        }

        $this->inspectionRepository->update($inspection, $data);

        return $this->getDetailById($staff, $inspection->id);
    }
}
