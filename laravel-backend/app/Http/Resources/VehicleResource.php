<?php

namespace App\Http\Resources;

use Illuminate\Http\Resources\Json\JsonResource;

class VehicleResource extends JsonResource
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
            'chassis_number' => $this->chassis_number,
            'engine_number' => $this->engine_number,
            'brand' => $this->brand,
            'model' => $this->model,
            'status' => $this->status,
            'registration_status' => $this->registration_status,
            'purpose_of_use'
        ];
    }
}
