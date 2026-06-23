<?php

namespace App\Http\Requests\Client\CitizenCard;

use Illuminate\Foundation\Http\FormRequest;

class AuthenticateCitizenCardRequest extends FormRequest
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
            'citizen_id' => 'required|string|max:20',
            'nationality' => 'nullable|string|max:100',
            'place_of_origin' => 'nullable|string|max:255',
            'date_of_issue' => 'nullable|date',
            'place_of_issue' => 'nullable|string|max:255',
            'image_url' => 'nullable|url'
        ];
    }
}
