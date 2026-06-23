<?php

namespace App\Helpers;

use Illuminate\Support\Facades\Storage;

class AddressHelper
{
    protected static $provinces;
    protected static $districts;
    protected static $wards;

    public static function loadData()
    {
        if (!self::$provinces) {
            self::$provinces = json_decode(Storage::get('hanhchinhvn/dist/tinh_tp.json'), true);
            self::$districts = json_decode(Storage::get('hanhchinhvn/dist/quan_huyen.json'), true);
            self::$wards = json_decode(Storage::get('hanhchinhvn/dist/xa_phuong.json'), true);
        }
    }

    public static function getAddressString($provinceCode = null, $districtCode = null, $wardCode = null)
    {
        self::loadData();

        if (!is_null($wardCode)) {
            $ward = self::$wards[$wardCode];
            return $ward['path_with_type'];
        }

        if (!is_null($districtCode)) {
            $district = self::$districts[$districtCode];
            return $district['path_with_type'];
        }

        if (!is_null($provinceCode)) {
            $province = self::$provinces[$provinceCode];
            return $province['name_with_type'];
        }

        return "";
    }
}
