<?php

namespace App\Http\Requests\Staff\Vehicle;

use Illuminate\Foundation\Http\FormRequest;

class GetListVehicleCenterRequest extends FormRequest
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
            'per_page' => 'nullable|integer|min:1|max:100',
            'search' => 'nullable|string|max:100',
            'registration_status' => 'nullable|in:registered,pending_registration,unregistered,suspended',
            'status' => 'nullable|in:active,inactive,banned',
            'sort_by' => 'nullable|in:registration_number,created_at,next_inspection_date',
            'sort_dir' => 'nullable|in:asc,desc',
            'need_inspection' => 'nullable|boolean',
            'expired_registration' => 'nullable|boolean'
        ];
    }
}
