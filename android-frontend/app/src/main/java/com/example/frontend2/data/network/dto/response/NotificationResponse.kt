package com.example.frontend2.data.network.dto.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("data")
    val data: List<Notification>,

    @SerializedName("meta")
    val meta: Meta,

    @SerializedName("links")
    val links: Links
)

data class Notification(
    @SerializedName("id")
    val id: Int,

    @SerializedName("userId")
    val userId: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("content")
    val content: String,

    @SerializedName("notificationType")
    val notificationType: String,

    @SerializedName("isRead")
    val isRead: Boolean,

    @SerializedName("relatedId")
    val relatedId: Int,

    @SerializedName("relatedType")
    val relatedType: String,

    @SerializedName("createdAt")
    val createdAt: String
)

data class Meta(
    @SerializedName("currentPage")
    val currentPage: Int,

    @SerializedName("lastPage")
    val lastPage: Int,

    @SerializedName("perPage")
    val perPage: Int,

    @SerializedName("total")
    val total: Int
)

data class Links(
    @SerializedName("first")
    val first: String,

    @SerializedName("last")
    val last: String,

    @SerializedName("prev")
    val prev: String?,

    @SerializedName("next")
    val next: String?
)
