<?php

namespace App\Http\Requests\Client\Vehicle;

use Illuminate\Foundation\Http\FormRequest;

class UpdateVehicleRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     *
     * @return bool
     */
    public function authorize()
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, mixed>
     */
    public function rules()
    {
        return [
            'vehicle_type_id' => 'nullable|exists:vehicle_types,id',
            'registration_number' => 'nullable|string|max:50',
            'chassis_number' => 'nullable|string|max:100',
            'engine_number' => 'nullable|string|max:100',
            'brand' => 'nullable|string|max:100',
            'model' => 'nullable|string|max:100',
            'manufacture_year' => 'nullable|integer|between:1900,' . date('Y'),
            'color' => 'nullable|string|max:50',
            'seat_count' => 'nullable|integer|min:1',
            'purpose_of_use' => 'nullable|string|max:255',
        ];
    }
}
