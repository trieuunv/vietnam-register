<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class ProvinceRequest extends FormRequest
{
    public function authorize()
    {
        return true;
    }

    public function rules()
    {
        return [
            'province_code' => ['required'],
            'district_code' => ['required'],
            'ward_code' => ['required'],
        ];  
    }
}