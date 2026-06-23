<?php

namespace App\Http\Resources\Staff\Vehicle;

use Illuminate\Http\Resources\Json\JsonResource;

class GetDetailVehicleResource extends JsonResource
{
    private function filterInspection($inspection)
    {
        return [
            'id' => $inspection->id,
            'center_id' => $inspection->center->id,
            'center_name' => $inspection->center->name,
            'inspection_date' => $inspection->inspection_date,
            'next_inspection_date' => $inspection->next_inspection_date,
            'result' => $inspection->result,
            'notes' => $inspection->notes,
        ];
    }

    public function toArray($request)
    {
        $inspections = $this->inspections->isNotEmpty() ? $this->inspections->map(function ($inspection) {
            return $this->filterInspection($inspection);
        }) : [];

        return [
            'id' => $this->id,
            'registration_number' => $this->registration_number,
            'chassis_number' => $this->chassis_number,
            'engine_number' => $this->engine_number,
            'brand' => $this->brand,
            'model' => $this->model,
            'status' => $this->status,
            'registration_status' => $this->registration_status,

            'vehicle_type' => [
                'id' => optional($this->vehicleType)->id,
                'name' => optional($this->vehicleType)->name,
                'description' => optional($this->vehicleType)->description,
            ],

            'owner' => [
                'id' => optional($this->owner)->id,
                'name' => optional($this->owner->user)->name,
                'email' => optional($this->owner->user)->email,
                'phone' => optional($this->owner->user)->phone,
            ],

            'inspections' => $inspections,
        ];
    }
}
