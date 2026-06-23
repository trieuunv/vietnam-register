import { createSlice } from "@reduxjs/toolkit";
import InspectionActionThunk from './actions';

import { IInspectionListItem } from "@/types";

const slice = createSlice({
    name: 'inspections',
    initialState: {
        list: [] as IInspectionListItem[],
        loading: false,
        error: '',
    },
    reducers: {},
    extraReducers(builder) {
        builder
            .addCase(InspectionActionThunk.fetchInspections.pending, (state) => {
                state.loading = true;
                state.error = '';
            })
            .addCase(InspectionActionThunk.fetchInspections.fulfilled, (state, action) => {
                state.loading = false;
                state.list = action.payload.data.data;
            })
            .addCase(InspectionActionThunk.fetchInspections.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Lỗi không xác định';
            })
    },
});

export default slice.reducer;