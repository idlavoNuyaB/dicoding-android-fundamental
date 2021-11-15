package com.freisia.github.api.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Users(
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
    @SerializedName("site_admin") val siteAdmin: Boolean,
    @SerializedName("name") val name : String?,
    @SerializedName("company") val company : String?,
    @SerializedName("blog") val blog : String?,
    @SerializedName("location") val location : String?,
    @SerializedName("email") val email : String?,
    @SerializedName("hireable") val hireable : String?,
    @SerializedName("bio") val bio : String?,
    @SerializedName("twitter_username") val twitterUsername : String?,
    @SerializedName("public_repos") val publicRepo : Int,
    @SerializedName("public_gists") val publicGist : Int,
    @SerializedName("followers") val followers : Int,
    @SerializedName("following") val following : Int,
    @SerializedName("created_at") val created : String?,
    @SerializedName("updated_at") val updated : String?
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
    ){
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
        parcel.writeString(name)
        parcel.writeString(company)
        parcel.writeString(blog)
        parcel.writeString(location)
        parcel.writeString(email)
        parcel.writeString(hireable)
        parcel.writeString(bio)
        parcel.writeString(twitterUsername)
        parcel.writeInt(publicRepo)
        parcel.writeInt(publicGist)
        parcel.writeInt(followers)
        parcel.writeInt(following)
        parcel.writeString(created)
        parcel.writeString(updated)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            return Users(parcel)
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }
}