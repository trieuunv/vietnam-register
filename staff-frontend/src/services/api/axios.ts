import axios, { AxiosError, AxiosHeaders, AxiosInstance, AxiosRequestConfig } from "axios";
import { AuthService, LocalStorageService } from "..";
import LocalStorageError from "@/errors/LocalStorageError";

export const createService = (
    baseURL: string,
    contentType: string = 'application/json',
): AxiosInstance => {
    return interceptAuth(baseConfig(baseURL, contentType));
}

export const createServiceNoToken = (baseURL: string): AxiosInstance => {
    const instance = axios.create({
        baseURL,
        headers: {
            'Accept-Language': 'en-US',
            'Content-Type': 'application/json',
        },
    });

    instance.interceptors.request.use(config => {
        return config;
    });

    return instance;
}

export const createServiceFormData = (
    baseURL: string,
    contentType: string = 'multipart/form-data',
): AxiosInstance => {
    return interceptAuth(baseConfig(baseURL, contentType));
}

export const downloadFileService = (
    baseURL: string,
    contentType: string = 'application/json',
): AxiosInstance => {
    const config: AxiosRequestConfig = baseConfig(baseURL, contentType);
    config.responseType = 'blob';
    return interceptAuth(config);
}

const baseConfig = (
    baseURL: string,
    contentType: string = 'application/json',
): AxiosRequestConfig => {
    return {
        baseURL,
        headers: {
            'Accept-Language': 'en-US',
            'Content-Type': contentType,
        }
    }
}

const interceptAuth = (config: AxiosRequestConfig) => {
    const instance = axios.create(config);
    
    instance.interceptors.request.use(async (cf) => {
        try {
            const token = LocalStorageService.get<string>(
                LocalStorageService.OAUTH_TOKEN,
            );
            
            if (token && cf?.headers) {
                cf.headers.Authorization = 'Bearer ' + token;
            }

            return cf;
        } catch (error) {
            return Promise.reject(error);
        }
    });

    instance.interceptors.response.use(
        response => response,
        error => {
            if (error.response?.status === 401) {
                return handleUnauthorizedError(error);
            }

            if (error instanceof LocalStorageError) {
                redirectToLogin();
            }

            return Promise.reject(error);
        },
    );

    return instance;
}

const handleUnauthorizedError = async (error: AxiosError) => {
    const refreshToken = LocalStorageService.get<string>(LocalStorageService.REFRESH_TOKEN);

    if (!refreshToken) {
        clearAuthData();
        redirectToLogin();
        return Promise.reject(error);
    }

    try {
        const response = await AuthService.refreshToken(refreshToken);
        const data = response.data?.data;
        const accessToken = data?.accessToken || null;

        LocalStorageService.set(LocalStorageService.OAUTH_TOKEN, accessToken);

        if (error.config) {
            error.config.headers = error.config.headers || new AxiosHeaders();
            error.config.headers.set('Authorization', `Bearer ${accessToken}`);
            return axios(error.config);
        }
    } catch (error) {
        clearAuthData();
        redirectToLogin();
        return Promise.reject(error);
    }
};

const redirectToLogin = () => {
    window.location.href = '/login';
};

const clearAuthData = () => {
    LocalStorageService.removeItem(LocalStorageService.OAUTH_TOKEN);
    LocalStorageService.removeItem(LocalStorageService.REFRESH_TOKEN);
}