<?php

namespace App\Helpers;

use Carbon\Carbon;

class HolidayHelper
{
    /**
     * List of floating solar dates for Lunar New Year (Tet) and Hung Kings Commemoration Day (2025-2027).
     */
    private static $floatingHolidays = [
        // 2025
        '2025-01-27', '2025-01-28', '2025-01-29', '2025-01-30', '2025-01-31', '2025-02-01', // Tet
        '2025-04-07', // Hung Kings

        // 2026
        '2026-02-16', '2026-02-17', '2026-02-18', '2026-02-19', '2026-02-20', '2026-02-21', // Tet
        '2026-04-26', // Hung Kings

        // 2027
        '2027-02-05', '2027-02-06', '2027-02-07', '2027-02-08', '2027-02-09', '2027-02-10', // Tet
        '2027-04-16', // Hung Kings
    ];

    /**
     * Check if a given date is a weekend or public holiday in Vietnam.
     *
     * @param string|Carbon $date
     * @return bool
     */
    public static function isOffDay($date): bool
    {
        $carbonDate = $date instanceof Carbon ? $date : Carbon::parse($date);
        
        // 1. Check weekend (Saturday or Sunday)
        if ($carbonDate->isWeekend()) {
            return true;
        }

        // 2. Check fixed solar holidays
        $md = $carbonDate->format('m-d');
        $fixedHolidays = [
            '01-01', // western new year
            '04-30', // liberation day
            '05-01', // international workers day
            '09-02', // national day
            '09-03', // day after national day
        ];

        if (in_array($md, $fixedHolidays)) {
            return true;
        }

        // 3. Check floating holidays (Tet & Hung Kings for 2025-2027)
        $ymd = $carbonDate->format('Y-m-d');
        if (in_array($ymd, self::$floatingHolidays)) {
            return true;
        }

        return false;
    }
}
