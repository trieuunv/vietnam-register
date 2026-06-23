import { IApiPaginationResponse } from "@/types/common/Pagination";
import { createService } from "./axios";
import { IVehicleDetail, IVehicleListItem } from "@/types/Vehicle";
import { IApiResponse } from "@/types";

const instance = createService(import.meta.env.VITE_API_URL);

const fetchVehicles = (params?: Record<string, any>) => {
    const url = '/vehicle';
    return instance.get<IApiPaginationResponse<IVehicleListItem>>(url, { params });
}

const fetchVehicle = (id: string) => {
    const url = `/vehicle/${id}`;
    return instance.get<IApiResponse<IVehicleDetail>>(url);
}

export default { fetchVehicles, fetchVehicle }