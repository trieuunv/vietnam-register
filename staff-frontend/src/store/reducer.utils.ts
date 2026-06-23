/* eslint-disable @typescript-eslint/no-explicit-any */

import { SerializedError } from "@reduxjs/toolkit";
import { AxiosError } from "axios";

export const serializeAxiosError = (
    value: any,
): AxiosError | SerializedError => {
if (typeof value === 'object' && value !== null) {
    if (value.isAxiosError) {
        return value;
    } else {
        const simpleError: SerializedError  = {};
        for (const property of commonErrorProperties) {
            if (typeof value[property] === 'string') {
                simpleError[property] = value[property];
            }
        }

        return simpleError;
    }
}
return { message: String(value) };
};

const commonErrorProperties: Array<keyof SerializedError> = [
    'name',
    'message',
    'stack',
    'code',
];