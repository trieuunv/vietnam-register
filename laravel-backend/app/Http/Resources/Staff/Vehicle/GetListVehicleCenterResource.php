<?php

namespace App\Http\Resources\Staff\Vehicle;

use Illuminate\Http\Resources\Json\JsonResource;

class GetListVehicleCenterResource extends JsonResource
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
            'registration_number' => $this->registration_number,
            'status' => $this->status,
            'registration_status' => $this->registration_status,
            'owner_name' => $this->owner->full_name ?? null,
            'vehicle_type_name' => $this->vehicleType->name ?? null,
            'inspected_at' => optional($this->latestInspection)->inspected_at,
        ];
    }
}
