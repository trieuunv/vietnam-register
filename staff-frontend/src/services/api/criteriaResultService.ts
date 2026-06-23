import { IApiResponse } from "@/types";
import { createService } from "./axios";
import { ICriteriaResult } from "@/types/Criteria";

const instance = createService(import.meta.env.VITE_API_URL);

const update = async (
    id: number,
    data: {
        result: string,
        notes?: string | null
    }
) => {
    const url = `/inspection-result/${id}`;
    return instance.put<IApiResponse<ICriteriaResult>>(url, data);
}

export default {
    update
}