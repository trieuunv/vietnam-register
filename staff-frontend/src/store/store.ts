import { combineReducers, configureStore } from '@reduxjs/toolkit';

import authReducer from "@/pages/Auth/slice";
import userReducer from "@/pages/User/slice";

import inspectionReducer from "@/pages/Inspection/slice";

import { useDispatch } from 'react-redux';

const reducers = combineReducers({
    auth: authReducer,
    user: userReducer,
    inspection: inspectionReducer
});

export const store = configureStore({
    reducer: reducers,
    middleware: (getDefaultMiddleware) => {
        return getDefaultMiddleware({ serializableCheck: false });
    },
});

export type AppDispatch = typeof store.dispatch;
export const useAppDispatch = () => useDispatch<AppDispatch>();

export type RootState = ReturnType<typeof store.getState>;

export default store;

