import { IApiResponse } from "@/types";
import { createService } from "./axios";
import { IInspectionDetail, IInspectionListItem } from "@/types/Inspection";

const instance = createService(import.meta.env.VITE_API_URL);

const fetchInspections = () => {
    const url = '/inspection';
    return instance.get<IApiResponse<IInspectionListItem[]>>(url);
}

const fetchInspection = (id: string) => {
    const url = `/inspection/${id}`;
    return instance.get<IApiResponse<IInspectionDetail>>(url);
}

const updateResult = (id: string, data: { result: string }) => {
    const url = `/inspection/update-result/${id}`;
    return instance.put<IApiResponse<{}>>(url, data);
}

const complete = (id: string) => {
    const url = `/inspection/complete/${id}`;
    return instance.post<IApiResponse<IInspectionDetail>>(url);
}

const cancel = (id: string) => {
    const url = `/inspection/cancel/${id}`;
    return instance.post<IApiResponse<IInspectionDetail>>(url);
}

const update = (
    id: string,
    data: {
        notes?: string,
        status?: string,
    }
) => {
    const url = `/inspection/${id}`;
    return instance.put<IApiResponse<IInspectionDetail>>(url, data);
}

const submit = (data: any, id: string) => {
    const url = `/inspection/submit/${id}`;
    return instance.post<IApiResponse<IInspectionDetail>>(url, data);
}

export default {
    fetchInspections,
    fetchInspection,
    submit,
    updateResult,
    complete,
    cancel,
    update
}