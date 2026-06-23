package com.example.frontend2.data.pref

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.frontend2.domain.model.AccessToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext context: Context
) {
    companion object {
        private const val PREFS_NAME = "auth_prefs"
        private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val KEY_REFRESH_TOKEN = "REFRESH_TOKEN"
        private const val KEY_TOKEN_EXPIRE_AT = "TOKEN_EXPIRE_AT"
    }

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccessToken(accessToken: AccessToken) {
        sharedPreferences.edit {
            putString(KEY_ACCESS_TOKEN, accessToken.token)
                .putLong(KEY_TOKEN_EXPIRE_AT, accessToken.expireAt)
        }
    }

    fun saveRefreshToken(refreshToken: String) {
        sharedPreferences.edit { putString(KEY_REFRESH_TOKEN, refreshToken) }
    }

    fun getAccessToken(): AccessToken? {
        val token = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
        val expiresAt = sharedPreferences.getLong(KEY_TOKEN_EXPIRE_AT, 0L)

        if (token.isNullOrEmpty()) {
            return null
        }

        return AccessToken(token, expiresAt)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(KEY_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        sharedPreferences.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_REFRESH_TOKEN)
            remove(KEY_TOKEN_EXPIRE_AT)
        }
    }
}