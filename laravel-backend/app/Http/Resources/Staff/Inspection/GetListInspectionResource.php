<?php

namespace App\Http\Resources\Staff\Inspection;

use Illuminate\Http\Resources\Json\JsonResource;

class GetListInspectionResource extends JsonResource
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
            'registration_number' => $this->vehicle->registration_number,
            'inspector_name' => $this->inspector->full_name,
            'inspection_date' => $this->inspection_date,
            'result' => $this->result,
            'status' => $this->status,
        ];
    }
}
