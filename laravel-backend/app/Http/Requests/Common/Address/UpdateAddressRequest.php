<?php

namespace App\Http\Requests\Common\Address;

use Illuminate\Foundation\Http\FormRequest;

class UpdateAddressRequest extends FormRequest
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
            'province_code' => 'required|string',
            'district_code' => 'required|string',
            'ward_code' => 'required|string',
            'detail' => 'required|string|max:255',
        ];
    }
}
