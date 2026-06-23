<?php

namespace App\Http\Resources\Client\Vehicle;

use Illuminate\Http\Resources\Json\JsonResource;

class GetVehicleDetailResource extends JsonResource
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
            'vehicle_type_id' => $this->vehicle_type_id,
            'registration_number' => $this->registration_number,
            'chassis_number' => $this->chassis_number,
            'engine_number' => $this->engine_number,
            'brand' => $this->brand,
            'model' => $this->model,
            'manufacture_year' => $this->manufacture_year,
            'color' => $this->color,
            'seat_count' => $this->seat_count,
            'first_registration_date' => $this->first_registration_date,
            'purpose_of_use' => $this->purpose_of_use,
            'registration_status' => $this->registration_status,
            'status' => $this->status,
            'created_at' => $this->created_at,
            'updated_at' => $this->updated_at,
        ];
    }
}
