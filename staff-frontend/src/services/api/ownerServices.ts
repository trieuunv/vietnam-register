import { IApiResponse } from "@/types";
import { createService } from "./axios";
import { IOwnerDetail } from "@/types/Owner";

const instance = createService(import.meta.env.VITE_API_URL);

const getOwner = (id: string) => {
    const url = `/owner/${id}`
    return instance.get<IApiResponse<IOwnerDetail>>(url);
}

export default {
    getOwner
}