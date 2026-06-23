<?php

namespace App\Http\Requests\Client\Vehicle;

use Illuminate\Foundation\Http\FormRequest;

class RegisterVehicleRequest extends FormRequest
{
    public function authorize()
    {
        return true;
    }

    public function rules()
    {
        return [
            'vehicle_type_id'         => 'required|integer|exists:vehicle_types,id',
            'registration_number'     => 'required|string|max:20|unique:vehicles,registration_number',
            'chassis_number'          => 'required|string|max:50|unique:vehicles,chassis_number',
            'engine_number'           => 'required|string|max:50|unique:vehicles,engine_number',
            'brand'                   => 'required|string|max:50',
            'model'                   => 'required|string|max:50',
            'manufacture_year'        => 'required|integer|between:1900,' . date('Y'),
            'color'                   => 'nullable|string|max:30',
            'seat_count'              => 'required|integer|min:1',
            'first_registration_date' => 'required|date|before_or_equal:today',
            'purpose_of_use'          => 'nullable|string|max:100',
        ];
    }
}