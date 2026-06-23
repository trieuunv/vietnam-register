<?php

namespace App\Http\Requests\Staff\Inspection;

use Illuminate\Foundation\Http\FormRequest;

class UpdateInspectionRequest extends FormRequest
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
            'inspection_date' => 'nullable|date',
            'next_inspection_date' => 'nullable|date|after_or_equal:inspection_date',
            'result' => 'nullable|in:passed,failed,conditional',
            'notes' => 'nullable|string|max:1000',
            'fee' => 'nullable|numeric|min:0',
            'mileage' => 'nullable|integer|min:0',
            'status' => 'nullable|in:pending,in_progress,completed,canceled',
        ];
    }
}
