package com.freisia.consumerapp.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "User", primaryKeys = ["login", "id"])
data class Users(
    @ColumnInfo(name = "login") @SerializedName("login") val login: String,
    @ColumnInfo(name = "id") @SerializedName("id") val id: Int,
    @ColumnInfo(name = "node_id") @SerializedName("node_id") val nodeId: String?,
    @ColumnInfo(name = "avatar_url") @SerializedName("avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "gravatar_id") @SerializedName("gravatar_id") val gravatarId: String?,
    @ColumnInfo(name = "url") @SerializedName("url") val url: String?,
    @ColumnInfo(name = "html_url") @SerializedName("html_url") val htmlUrl: String?,
    @ColumnInfo(name = "followers_url") @SerializedName("followers_url") val followersUrl: String?,
    @ColumnInfo(name = "following_url") @SerializedName("following_url") val followingUrl: String?,
    @ColumnInfo(name = "gists_url") @SerializedName("gists_url") val gistsUrl: String?,
    @ColumnInfo(name = "starred_url") @SerializedName("starred_url") val starredUrl: String?,
    @ColumnInfo(name = "subscriptions_url") @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
    @ColumnInfo(name = "organizations_url") @SerializedName("organizations_url") val organizationsUrl: String?,
    @ColumnInfo(name = "repos_url") @SerializedName("repos_url") val reposUrl: String?,
    @ColumnInfo(name = "events_url") @SerializedName("events_url") val eventsUrl: String?,
    @ColumnInfo(name = "received_events_url") @SerializedName("received_events_url") val receivedEventsUrl: String?,
    @ColumnInfo(name = "type") @SerializedName("type") val type: String?,
    @ColumnInfo(name = "site_admin") @SerializedName("site_admin") val siteAdmin: Boolean,
    @ColumnInfo(name = "name") @SerializedName("name") val name: String?,
    @ColumnInfo(name = "company") @SerializedName("company") val company: String?,
    @ColumnInfo(name = "blog") @SerializedName("blog") val blog: String?,
    @ColumnInfo(name = "location") @SerializedName("location") val location: String?,
    @ColumnInfo(name = "email") @SerializedName("email") val email: String?,
    @ColumnInfo(name = "hireable") @SerializedName("hireable") val hireable: String?,
    @ColumnInfo(name = "bio") @SerializedName("bio") val bio: String?,
    @ColumnInfo(name = "twitter_username") @SerializedName("twitter_username") val twitterUsername: String?,
    @ColumnInfo(name = "public_repos") @SerializedName("public_repos") val publicRepo: Int,
    @ColumnInfo(name = "public_gists") @SerializedName("public_gists") val publicGist: Int,
    @ColumnInfo(name = "followers") @SerializedName("followers") val followers: Int,
    @ColumnInfo(name = "following") @SerializedName("following") val following: Int,
    @ColumnInfo(name = "created_at") @SerializedName("created_at") val created: String?,
    @ColumnInfo(name = "updated_at") @SerializedName("updated_at") val updated: String?
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() as String,
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        1 == parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    )
}