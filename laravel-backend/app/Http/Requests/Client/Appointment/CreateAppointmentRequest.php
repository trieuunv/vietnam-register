<?php

namespace App\Http\Requests\Client\Appointment;

use Illuminate\Foundation\Http\FormRequest;

class CreateAppointmentRequest extends FormRequest
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
            'vehicle_id' => 'required|exists:vehicles,id',
            'center_id' => 'required|exists:centers,id',
            'appointment_date' => 'required|date|after:today',
            'notes' => 'nullable|string|max:255',
        ];
    }
}
