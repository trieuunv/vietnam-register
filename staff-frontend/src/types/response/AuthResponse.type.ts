export interface ILoginResponse {
    accessToken: string,
    refreshToken: string
}

export interface IRefreshTokenResponse {
    accessToken: string,
    expiresIn: string
}