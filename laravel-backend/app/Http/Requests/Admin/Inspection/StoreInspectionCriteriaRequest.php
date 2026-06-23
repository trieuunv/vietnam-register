<?php

namespace App\Http\Requests\Admin\Inspection;

use Illuminate\Foundation\Http\FormRequest;

class StoreInspectionCriteriaRequest extends FormRequest
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
            'vehicle_type_id' => 'required|exists:vehicle_types,id',
            'name' => 'required|string|max:100',
            'description' => 'nullable|string',
            'standard' => 'nullable|string',
            'is_mandatory' => 'boolean',
        ];
    }
}
