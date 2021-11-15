package com.freisia.github.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Follow(
    @SerializedName("login") val login: String?,
    @SerializedName("id") val id: Int,
    @SerializedName("node_id") val nodeId: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("gravatar_id") val gravatarId: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("followers_url") val followersUrl: String?,
    @SerializedName("following_url") val followingUrl: String?,
    @SerializedName("gists_url") val gistsUrl: String?,
    @SerializedName("starred_url") val starredUrl: String?,
    @SerializedName("subscriptions_url") val subscriptionsUrl: String?,
    @SerializedName("organizations_url") val organizationsUrl: String?,
    @SerializedName("repos_url") val reposUrl: String?,
    @SerializedName("events_url") val eventsUrl: String?,
    @SerializedName("received_events_url") val receivedEventsUrl: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("site_admin") val siteAdmin: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
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
        1 == parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(login)
        parcel.writeInt(id)
        parcel.writeString(nodeId)
        parcel.writeString(avatarUrl)
        parcel.writeString(gravatarId)
        parcel.writeString(url)
        parcel.writeString(htmlUrl)
        parcel.writeString(followersUrl)
        parcel.writeString(followingUrl)
        parcel.writeString(gistsUrl)
        parcel.writeString(starredUrl)
        parcel.writeString(subscriptionsUrl)
        parcel.writeString(organizationsUrl)
        parcel.writeString(reposUrl)
        parcel.writeString(eventsUrl)
        parcel.writeString(receivedEventsUrl)
        parcel.writeString(type)
        parcel.writeInt(if (siteAdmin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Follow> {
        override fun createFromParcel(parcel: Parcel): Follow {
            return Follow(parcel)
        }

        override fun newArray(size: Int): Array<Follow?> {
            return arrayOfNulls(size)
        }
    }
}