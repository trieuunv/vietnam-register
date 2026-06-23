<?php

namespace App\Http\Requests\Client\Comment;
use Illuminate\Foundation\Http\FormRequest;

class CreateCommentRequest extends FormRequest 
{
    public function authorize()
    {
        return true;
    }

    public function rules()
    {
        return [
            'content' => ['required', 'string'],
        ];  
    }
}