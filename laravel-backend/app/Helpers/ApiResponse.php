<?php 

namespace App\Helpers;

use Illuminate\Http\JsonResponse;

class ApiResponse
{
    public static function success($data = null, string $message = 'Success', int $code = 200): JsonResponse
    {
        return self::makeResponse(true, $message, $data, null, $code);
    }

    public static function error(string $message = 'Error', $errors = null, int $code = 400): JsonResponse
    {
        return self::makeResponse(false, $message, null, $errors, $code);
    }

    public static function notFound(string $message = 'Resource not found'): JsonResponse
    {
        return self::error($message, null, 404);
    }

    public static function unauthorized(string $message = 'Unauthorized'): JsonResponse
    {
        return self::error($message, null, 401);
    }

    public static function validationError(array $errors, string $message = 'Validation Error'): JsonResponse
    {
        return self::error($message, $errors, 422);
    }

    public static function serverError(string $message = 'Internal Server Error', $errors = null): JsonResponse
    {
        return self::error($message, $errors, 500);
    }

    private static function makeResponse(bool $success, string $message, $data = null, $errors = null, int $code = 200): JsonResponse
    {
        $response = [
            'success' => $success,
            'message' => $message,
        ];

        if (!is_null($data)) {
            $response['data'] = $data;
        }

        if (!is_null($errors)) {
            $response['errors'] = $errors;
        }

        return response()->json($response, $code);
    }
}