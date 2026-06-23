<?php

namespace App\Http\Resources\Staff\Appointment;

use Illuminate\Http\Resources\Json\JsonResource;

class GetDetailAppointmentResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array|\Illuminate\Contracts\Support\Arrayable|\JsonSerializable
     */
    public function toArray($request)
    {
        return [
            'id' => $this->id,
            'owner' => [
                'id' => $this->owner?->id,
                'full_name' => $this->owner?->full_name,
                'phone' => $this->owner?->phone,
                'email' => $this->owner?->email,
            ],
            'vehicle' => [
                'id' => $this->vehicle?->id,
                'registration_number' => $this->vehicle?->registration_number,
                'brand' => $this->vehicle?->brand,
                'model' => $this->vehicle?->model,
                'color' => $this->vehicle?->color,
                'vehicle_type_name' => $this->vehicle?->vehicleType->name,
            ],
            'center' => [
                'id' => $this->center?->id,
                'name' => $this->center?->name,
                'code' => $this->center?->code,
            ],
            'inspection' => $this->when($this->inspection, function () {
                return [
                    'id' => $this->inspection->id,
                    'status' => $this->inspection->status,
                    'inspection_date' => $this->inspection->inspection_date,
                    'next_inspection_date' => $this->inspection->next_inspection_date,
                    'certificate_number' => $this->inspection->certificate_number,
                    'fee' => $this->inspection->fee,
                    'result' => $this->inspection->result,
                ];
            }),
            'appointment_date' => $this->appointment_date,
            'status' => $this->status,
            'confirmation_code' => $this->confirmation_code,
            'notes' => $this->notes,
            'created_at' => $this->created_at?->toDateTimeString(),
            'updated_at' => $this->updated_at?->toDateTimeString(),
        ];
    }
}
