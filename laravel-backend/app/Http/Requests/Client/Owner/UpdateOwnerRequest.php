<?php

namespace App\Http\Requests\Client\Owner;

use Illuminate\Foundation\Http\FormRequest;

class UpdateOwnerRequest extends FormRequest
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
            'full_name' => 'required|string|max:255',
            'day_of_birth' => 'nullable|date',
            'gender' => 'nullable|in:Male,Female,Other',
            'email' => 'nullable|email',
            'phone' => 'nullable|string|max:15',
        ];
    }
}
