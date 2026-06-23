<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Http\Request;

class ModifyResponse
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure(\Illuminate\Http\Request): (\Illuminate\Http\Response|\Illuminate\Http\RedirectResponse)  $next
     * @return \Illuminate\Http\Response|\Illuminate\Http\RedirectResponse
     */
    public function handle(Request $request, Closure $next)
    {
        $response = $next($request);

        if ($response->isSuccessful()) {
            $content = $response->getContent();

            $data = json_decode($content, true);

            if (json_last_error() === JSON_ERROR_NONE) {
                $modifiedData = $this->convertToCamelCase($data);
                
                $response->setContent(json_encode($modifiedData));
            }
        }

        return $response;
    }

    public static function convertToCamelCase($data)
    {
        $toCamelCase = function ($string) {
            $parts = explode('_', $string);
            $camelCaseString = array_shift($parts);
            foreach ($parts as $part) {
                $camelCaseString .= ucfirst(strtolower($part));
            }
            return $camelCaseString;
        };

        $processData = function ($input) use (&$processData, $toCamelCase) {
            if (is_array($input)) {
                $result = [];
                foreach ($input as $key => $value) {
                    $newKey = $toCamelCase($key);
                    if (is_array($value) || is_object($value)) {
                        $value = $processData($value);
                    }
                    $result[$newKey] = $value;
                }
                return $result;
            } elseif (is_object($input)) {
                $arrayData = json_decode(json_encode($input), true);
                return $processData($arrayData);
            }
            return $input;
        };

        return $processData($data);
    }
}
