<?php

namespace App\Http\Resources\Staff\Appointment;

use Illuminate\Http\Resources\Json\JsonResource;

class GetListAppointmentResource extends JsonResource
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
            'appointment_date' => $this->appointment_date,
            'status' => $this->status,
            'confirmation_code' => $this->confirmation_code,
            'owner_name' => $this->owner?->full_name,
            'vehicle_reg_number' => $this->vehicle?->registration_number,
            'center_name' => $this->center?->name,
        ];
    }
}
