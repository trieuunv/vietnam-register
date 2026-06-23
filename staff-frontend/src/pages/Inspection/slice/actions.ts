import { InspectionService } from "@/services";
import { serializeAxiosError } from "@/store/reducer.utils";
import { createAsyncThunk } from "@reduxjs/toolkit";

const fetchInspections = createAsyncThunk(
    'inspection/index',
    () => {
        return InspectionService.fetchInspections();
    },
    { serializeError: serializeAxiosError }
)

export default { fetchInspections };