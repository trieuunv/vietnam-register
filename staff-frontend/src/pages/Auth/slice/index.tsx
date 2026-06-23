import { createSlice } from "@reduxjs/toolkit";
import { LocalStorageService } from "@/services";
import AuthActionThunk from './actions';

const slice = createSlice({
    name: 'auth',
    initialState: {},
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(AuthActionThunk.login.fulfilled, (state, action) => {
                LocalStorageService.set(
                    LocalStorageService.REFRESH_TOKEN,
                    action.payload.data.data.refreshToken,
                );
                LocalStorageService.set(
                    LocalStorageService.OAUTH_TOKEN,
                    action.payload.data.data.accessToken
                )
            })
    },
});

export default slice.reducer;