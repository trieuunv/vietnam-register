class LocalStorageError extends Error {
    constructor(message: string) {
        super(message);
        this.name = "LocalStorageError";
    }
}

export default LocalStorageError;