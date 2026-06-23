<?php

namespace App\Http\Resources\Client\Center;

use App\Helpers\AddressHelper;
use Illuminate\Http\Resources\Json\JsonResource;

class GetListCenterResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return array|\Illuminate\Contracts\Support\Arrayable|\JsonSerializable
     */
    public function toArray($request)
    {
        $fullAddress = null;

        $fullAddress = null;

        if (!empty($this->address)) {
            $adminAddress = AddressHelper::getAddressString(
                $this->address->province_code ?? null,
                $this->address->district_code ?? null,
                $this->address->ward_code ?? null
            );

            $detail = trim($this->address->detail ?? '');

            $fullAddress = $detail ? "$detail, $adminAddress" : $adminAddress;
        }

        return [
            'id' => $this->id,
            'name' => $this->name,
            'code' => $this->code,
            'status' => $this->status,
            'full_address' => $fullAddress
        ];
    }
}
