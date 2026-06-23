import { IApiResponse, IAppointmentDetail, IAppointmentListItem } from "@/types";
import { createService } from "./axios";

const instance = createService(import.meta.env.VITE_API_URL);

const fetchAppointments = (params?: Record<string, any>) => {
    const url = '/appointment';
    return instance.get<IApiResponse<IAppointmentListItem[]>>(url, { params });
}

const fetchAppointment = (id: string) => {
    const url = `/appointment/${id}`;
    return instance.get<IApiResponse<IAppointmentDetail>>(url);
}

const confirm = (id: string) => {
    const url = `/appointment/${id}/confirm`;
    return instance.put<IApiResponse<IAppointmentDetail>>(url);
}

const reject = (id: string) => {
    const url = `/appointment/${id}/reject`;
    return instance.put<IApiResponse<IAppointmentDetail>>(url);
}

const start = (id: string) => {
    const url = `/appointment/${id}/start`;
    return instance.post<IApiResponse<IAppointmentDetail>>(url);
}

export default { fetchAppointments, fetchAppointment, confirm, reject, start }