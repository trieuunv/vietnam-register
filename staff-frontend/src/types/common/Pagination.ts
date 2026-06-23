import { IApiResponse } from "./ApiResponse";

export interface IPaginationMeta {
    currentPage: number;
    lastPage: number;
    perPage: number;
    total: number;
}

export interface IPaginationLinks {
    first: string;
    last: string;
    prev: string | null;
    next: string | null;
}

export interface IPaginatedData<T> {
    data: T[];
    meta: IPaginationMeta;
    links: IPaginationLinks;
}

export interface IApiPaginationResponse<T> extends IApiResponse<IPaginatedData<T>> {}