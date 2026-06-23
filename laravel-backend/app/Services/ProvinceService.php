<?php

namespace App\Services;

class ProvinceService
{
    public function getProvinces()
    {
        return readJsonFileToArray('app/hanhchinhvn/dist/tinh_tp.json');
    }

    public function getDistricts(string $province_code) 
    {
        return readJsonFileToArray('app/hanhchinhvn/dist/quan-huyen/' . $province_code . '.json');
    }

    public function getWards(string $district_code)
    {
        return readJsonFileToArray('app/hanhchinhvn/dist/xa-phuong/' . $district_code . '.json');
    }

    public function getLocation(string $province_code, string $district_code, string $ward_code): ?string
    {
        // Đọc file tỉnh
        $provinces = readJsonFileToArray('node_modules/hanhchinhvn/dist/tinh_tp.json');
        if (!isset($provinces[$province_code])) return null;
        $province = $provinces[$province_code];

        // Đọc file huyện theo province
        $districtsPath = 'node_modules/hanhchinhvn/dist/quan-huyen/' . $province_code . '.json';
        $districts = readJsonFileToArray($districtsPath);
        if (!isset($districts[$district_code])) return null;
        $district = $districts[$district_code];

        // Đọc file xã theo district
        $wardsPath = 'node_modules/hanhchinhvn/dist/xa-phuong/' . $district_code . '.json';
        $wards = readJsonFileToArray($wardsPath);
        if (!isset($wards[$ward_code])) return null;
        $ward = $wards[$ward_code];

        // Trả về chuỗi đầy đủ: ví dụ "Phường X, Quận Y, Thành phố Z"
        return $ward['name_with_type'] . ', ' . $district['name_with_type'] . ', ' . $province['name_with_type'];
    }
}