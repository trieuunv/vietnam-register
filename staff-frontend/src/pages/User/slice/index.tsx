/* eslint-disable @typescript-eslint/no-explicit-any */

import { createSlice } from "@reduxjs/toolkit";

import UserActionThunk from './actions';

const slice = createSlice({
    name: 'user',
    initialState: {},
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(UserActionThunk.getMe.fulfilled, (state, action) => {
                console.log(action.payload.data.data);
            })
            .addCase(UserActionThunk.getMe.rejected, (state, error: any) => {
                console.log(error);
            })
    },
});

export default slice.reducer;