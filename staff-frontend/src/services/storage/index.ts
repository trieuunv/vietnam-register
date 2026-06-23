import LocalStorageError from "@/errors/LocalStorageError";

const PREFIX = `local::`;

const set = <T = unknown>(key: string, value: T): void => {
    try {
        const serializedValue = JSON.stringify(value);
        localStorage.setItem(PREFIX + key, serializedValue);
    } catch (error) {
        throw new LocalStorageError(`Store serialization failed for key "${key}": ${error}`);
    }
};

const get = <T = unknown>(key: string): T | undefined => {
    try {
        const serializedValue = localStorage.getItem(PREFIX + key);
        if (serializedValue === null) {
            return undefined;
        }
        return JSON.parse(serializedValue) as T;
    } catch (error) {
        throw new LocalStorageError(`Store deserialization failed for key "${key}": ${error}`);
    }
};

const removeItem = (key: string): void => {
    try {
        localStorage.removeItem(PREFIX + key);
    } catch (error) {
        throw new LocalStorageError(`Failed to remove item with key "${key}": ${error}`);
    }
};

const removeAllItem = (): void => {
    try {
        localStorage.clear();
    } catch (error) {
        throw new LocalStorageError(`Failed to clear localStorage: ${error}`);
    }
};

export const OAUTH_TOKEN = 'charbet_access_token';
export const REFRESH_TOKEN = 'charbet_refresh_token';
export const USER_INFO = 'charbet_user_info';
export const LANGUAGE = 'charbet_language';

export default {
    set,
    get,
    removeItem,
    removeAllItem,
    OAUTH_TOKEN,
    REFRESH_TOKEN,
    USER_INFO,
    LANGUAGE,
};