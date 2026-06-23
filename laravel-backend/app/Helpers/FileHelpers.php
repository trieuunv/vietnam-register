<?php

use Illuminate\Support\Facades\File;

if (!function_exists('readJsonFileToArray')) {

    function readJsonFileToArray($filePath)
    {
        if (!File::exists(storage_path($filePath))) {
            return [];
        }

        $fileContents = File::get(storage_path($filePath));

        $data = json_decode($fileContents, true);

        if (json_last_error() !== JSON_ERROR_NONE) {
            return [];
        }

        $result = array_values($data);
        
        return $result;
    }
}