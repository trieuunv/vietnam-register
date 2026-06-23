<?php

namespace App\Http\Resources\Staff\Owner;

use Illuminate\Http\Resources\Json\JsonResource;

class GetOwnerDetailResource extends JsonResource
{
    private function filterVehicles($vehicle)
    {
        return [
            'id' => $vehicle->id,
            'vehicle_type_name' => $vehicle->vehicleType->name,
            'registration_number' => $vehicle->registration_number,
            'brand' => $vehicle->brand,
            'model' => $vehicle->model,
            'manufacture_year' => $vehicle->manufacture_year,
            'registration_status' => $vehicle->registration_status,
            'status' => $vehicle->status,
            'last_inspection_date' => optional($vehicle->latestPassedInspection)->inspection_date,
            'next_inspection_date' => optional($vehicle->latestPassedInspection)->next_inspection_date,
        ];
    }

    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array|\Illuminate\Contracts\Support\Arrayable|\JsonSerializable
     */
    public function toArray($request)
    {
        $vehicles = $this->vehicles->isNotEmpty() ? $this->vehicles->map(function ($vehicle) {
            return $this->filterVehicles($vehicle);
        }) : [];

        return [
            'full_name' => $this->full_name,
            'day_of_birth' => $this->day_of_birth,
            'gender' => $this->gender,
            'email' => $this->email,
            'phone' => $this->phone,
            'citizen_number' => optional($this->citizenCard)->citizen_id,
            'created_at' => $this->created_at,

            'vehicles' => $vehicles
        ];
    }
}
