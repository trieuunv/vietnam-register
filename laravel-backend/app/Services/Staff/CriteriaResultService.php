<?php

namespace App\Services\Staff;

use App\Exceptions\HandlerException;
use App\Models\User;
use App\Repositories\Staff\CriteriaResultRepository;
use App\Repositories\Staff\InspectionCriteriaRepository;
use App\Repositories\Staff\InspectionRepository;
use App\Repositories\Staff\InspectionResultRepository;
use App\Repositories\Staff\StaffRepository;
use App\Repositories\Staff\VehicleRepository;

class CriteriaResultService
{
    protected $criteriaResultRepository;
    protected $staffRepository;
    protected $inspectionRepository;
    protected $vehicleRepository;
    protected $inspectionCriteriaRepository;

    public function __construct(
        CriteriaResultRepository $criteriaResultRepository,
        StaffRepository $staffRepository,
        InspectionRepository $inspectionRepository,
        VehicleRepository $vehicleRepository,
        InspectionCriteriaRepository $inspectionCriteriaRepository
    ) {
        $this->criteriaResultRepository = $criteriaResultRepository;
        $this->staffRepository = $staffRepository;
        $this->inspectionRepository = $inspectionRepository;
        $this->vehicleRepository = $vehicleRepository;
        $this->inspectionCriteriaRepository = $inspectionCriteriaRepository;
    }

    public function create(User $staff, array $data)
    {
        $inspection = $this->inspectionRepository->findById($data['inspection_id']);

        if (!$inspection || $inspection->center_id !== $staff->center_id) {
            throw new HandlerException('You are not authorized to add results for this inspection.', 403);
        }

        $inspector = $this->staffRepository->findById($data['inspector_id']);

        if (!$inspector || !$inspector->center_id || $inspector->center_id !== $inspection->center_id) {
            throw new HandlerException('Inspector must belong to the same center.', 403);
        }

        $vehicle = $this->vehicleRepository->findById($inspection->vehicle_id);
        if (!$vehicle) {
            throw new HandlerException('Vehicle not found.', 404);
        }

        $criteria = $this->inspectionCriteriaRepository->findById($data['criteria_id']);
        if (!$criteria || $criteria->vehicle_type_id !== $vehicle->vehicle_type_id) {
            throw new HandlerException('The selected criteria is not valid for the vehicle type.', 400);
        }

        $inspectionResult = $this->criteriaResultRepository->create($data);

        return $inspectionResult;
    }

    public function update(User $staff, $id, array $data)
    {
        $criteriaResult = $this->criteriaResultRepository->findById($id);

        if (!$criteriaResult) {
            throw new HandlerException('Criteria result not found', 404);
        }

        $inspection = $this->inspectionRepository->findById($criteriaResult->inspection_id);

        if (!$inspection || $inspection->center_id !== $staff->center_id) {
            throw new HandlerException('You are not authorized to add results for this inspection.', 403);
        }

        $updated = $this->criteriaResultRepository->update($criteriaResult, $data);

        return $updated;
    }
}
