/** 
 * Action definition -> use for reducer
*/

import { AuthService } from "@/services";
import { serializeAxiosError } from "@/store/reducer.utils";
import { ILoginRequest } from "@/types";
import { createAsyncThunk } from "@reduxjs/toolkit";

const login = createAsyncThunk(
    'auth/login',
    (data: ILoginRequest) => {
        return AuthService.login(data);
    },
    { serializeError: serializeAxiosError }
);

export default { login };