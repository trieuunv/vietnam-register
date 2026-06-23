import { createServiceNoToken } from "./axios";
import { IApiResponse, ILoginRequest, ILoginResponse, IRefreshTokenResponse } from "@/types";

const instanceNoToken = createServiceNoToken(import.meta.env.VITE_API_URL);

const refreshToken = (refreshToken: string) => {
    const url = '/auth/refresh';
    return instanceNoToken.post<IApiResponse<IRefreshTokenResponse>>(url, { refresh_token: refreshToken });
}

const login = (data: ILoginRequest) => {
    const url = '/auth/login';
    return instanceNoToken.post<IApiResponse<ILoginResponse>>(url, data);
}

export default {
    refreshToken,
    login
}