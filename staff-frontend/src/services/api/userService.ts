import { IApiResponse, IUserResponse } from "@/types";
import { createService } from "./axios"

const instance = createService(import.meta.env.VITE_API_URL);

const getMe = () => {
    const url = '/user';
    return instance.get<IApiResponse<IUserResponse>>(url);
}

export default {
    getMe,
}