<?php

namespace App\Http\Resources\Client\Vehicle;

use Illuminate\Http\Resources\Json\JsonResource;

class GetListVehicleResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array|\Illuminate\Contracts\Support\Arrayable|\JsonSerializable
     */
    public function toArray($request)
    {
        $vehicleType = $this->vehicleType;

        return [
            'id' => $this->id,
            'vehicle_type_name' => $vehicleType ? $vehicleType->name : null,
            'registration_number' => $this->registration_number,
            'brand' => $this->brand,
            'color' => $this->color,
            'registration_status' => $this->registration_status,
            'status' => $this->status,
        ];
    }
}
