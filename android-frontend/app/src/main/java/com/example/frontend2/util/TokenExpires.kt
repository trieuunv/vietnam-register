package com.example.frontend2.util

object TokenExpires {
    fun toExpireAt(expiresIn: Long): Long {
        return System.currentTimeMillis() + (expiresIn * 1000)
    }

    fun checkExpire(expireAt: Long?): Boolean {
        if (expireAt == null) {
            return false
        }

        return expireAt > System.currentTimeMillis()
    }
}