<?php

namespace App\Http\Resources\Staff\Inspection;

use Illuminate\Http\Resources\Json\JsonResource;

class GetDetailInspectionResource extends JsonResource
{
    private function filterResult($result)
    {
        return [
            'id' => $result->id,
            'criteria_id' => $result->criteria_id,
            'criteria_name' => $result->criteria->name ?? null,
            'result' => $result->result,
            'notes' => $result->notes,
            'checked_at' => $result->checked_at,
        ];
    }

    public function toArray($request)
    {
        $vehicleType = $this->vehicle->vehicleType;
        $owner = $this->vehicle->owner;
        $citizenCard = $owner->citizenCard;

        $criteriaResults = $this->criteriaResults->map(function ($result) {
            return $this->filterResult($result);
        });

        return [
            'id' => $this->id,
            'result' => $this->result,
            'status' => $this->status,
            'fee' => $this->fee,
            'inspection_date' => $this->inspection_date,
            'certificate_number' => $this->certificate_number,
            'next_inspection_date' => $this->next_inspection_date,
            'notes' => $this->notes,
            'vehicle' => [
                'id' => $this->vehicle->id,
                'registration_number' => $this->vehicle->registration_number,
                'brand' => $this->vehicle->brand,
                'model' => $this->vehicle->model,
                'manufacture_year' => $this->vehicle->manufacture_year,
                'chassis_number' => $this->vehicle->chassis_number,
                'engine_number' => $this->vehicle->engine_number,
                'vehicle_type' => $vehicleType->name,
            ],
            'owner' => [
                'id' => $owner->id,
                'full_name' => $owner->full_name,
                'citizen_id' => optional($owner->citizenCard)->citizen_id,
                'phone' => $owner->phone
            ],
            'criteria_results' => $criteriaResults,
        ];
    }
}
