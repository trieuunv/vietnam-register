import { UserService } from "@/services";
import { serializeAxiosError } from "@/store/reducer.utils";
import { createAsyncThunk } from "@reduxjs/toolkit";

const getMe = createAsyncThunk(
    'user/profile',
    async () => {
        return UserService.getMe();
    },
    { serializeError: serializeAxiosError }
)

export default {
    getMe
}