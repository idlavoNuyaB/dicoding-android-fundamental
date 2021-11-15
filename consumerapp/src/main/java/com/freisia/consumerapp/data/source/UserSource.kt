package com.freisia.consumerapp.data.source

import android.content.Context
import android.net.Uri
import androidx.core.database.getStringOrNull
import com.freisia.consumerapp.data.model.Users
import com.freisia.consumerapp.utilitas.Constant

class UserSource(context: Context,private val limit : Int, private val offset : Int) : UserSourceLiveData<List<Users>>(context,URI){

    companion object{
        val URI : Uri = Constant.CONTENT_URI
    }

    private fun getUser(context:Context) : List<Users>{
        val listUsers = mutableListOf<Users>()
        if(limit == 0 && offset == 0){
            val cursor = context.contentResolver.query(
                URI,
                null,
                null,
                null,
                null
            )
            if(cursor != null && cursor.moveToFirst()){
                do{
                    val login = cursor.getString(0)
                    val id = cursor.getInt(1)
                    val nodeId = cursor.getStringOrNull(2)
                    val avatarUrl = cursor.getStringOrNull(3)
                    val gravatarId = cursor.getStringOrNull(4)
                    val url = cursor.getStringOrNull(5)
                    val htmlUrl = cursor.getStringOrNull(6)
                    val followersUrl = cursor.getStringOrNull(7)
                    val followingUrl = cursor.getStringOrNull(8)
                    val gistUrl = cursor.getStringOrNull(9)
                    val starredUrl = cursor.getStringOrNull(10)
                    val subscriptionsUrl = cursor.getStringOrNull(11)
                    val organizationUrl = cursor.getStringOrNull(12)
                    val reposUrl = cursor.getStringOrNull(13)
                    val eventsUrl = cursor.getStringOrNull(14)
                    val receivedEventsUrl = cursor.getStringOrNull(15)
                    val type = cursor.getStringOrNull(16)
                    val siteAdmin = cursor.getInt(17) > 0
                    val name = cursor.getStringOrNull(18)
                    val company = cursor.getStringOrNull(19)
                    val blog = cursor.getStringOrNull(20)
                    val location = cursor.getStringOrNull(21)
                    val email = cursor.getStringOrNull(22)
                    val hireable = cursor.getStringOrNull(23)
                    val bio = cursor.getStringOrNull(24)
                    val twitterUsername = cursor.getStringOrNull(25)
                    val publicRepos = cursor.getInt(26)
                    val publicGists = cursor.getInt(27)
                    val followers = cursor.getInt(28)
                    val following = cursor.getInt(29)
                    val createdAt = cursor.getStringOrNull(30)
                    val updatedAt = cursor.getStringOrNull(31)
                    listUsers.add(Users(login,id,nodeId,avatarUrl,gravatarId,url,htmlUrl,followersUrl,
                        followingUrl,gistUrl,starredUrl,subscriptionsUrl,organizationUrl,reposUrl,eventsUrl,
                        receivedEventsUrl,type,siteAdmin,name,company,blog,location,email,hireable,bio,
                        twitterUsername,publicRepos,publicGists,followers,following,createdAt,updatedAt))
                } while (cursor.moveToNext())
                cursor.close()
            }
        } else{
            val cursor = context.contentResolver.query(
                URI,
                null,
                null,
                null,
                "limit " + limit + "offset" +  offset
            )
            if(cursor != null && cursor.moveToFirst()){
                do{
                    val login = cursor.getString(0)
                    val id = cursor.getInt(1)
                    val nodeId = cursor.getStringOrNull(2)
                    val avatarUrl = cursor.getStringOrNull(3)
                    val gravatarId = cursor.getStringOrNull(4)
                    val url = cursor.getStringOrNull(5)
                    val htmlUrl = cursor.getStringOrNull(6)
                    val followersUrl = cursor.getStringOrNull(7)
                    val followingUrl = cursor.getStringOrNull(8)
                    val gistUrl = cursor.getStringOrNull(9)
                    val starredUrl = cursor.getStringOrNull(10)
                    val subscriptionsUrl = cursor.getStringOrNull(11)
                    val organizationUrl = cursor.getStringOrNull(12)
                    val reposUrl = cursor.getStringOrNull(13)
                    val eventsUrl = cursor.getStringOrNull(14)
                    val receivedEventsUrl = cursor.getStringOrNull(15)
                    val type = cursor.getStringOrNull(16)
                    val siteAdmin = cursor.getInt(17) > 0
                    val name = cursor.getStringOrNull(18)
                    val company = cursor.getStringOrNull(19)
                    val blog = cursor.getStringOrNull(20)
                    val location = cursor.getStringOrNull(21)
                    val email = cursor.getStringOrNull(22)
                    val hireable = cursor.getStringOrNull(23)
                    val bio = cursor.getStringOrNull(24)
                    val twitterUsername = cursor.getStringOrNull(25)
                    val publicRepos = cursor.getInt(26)
                    val publicGists = cursor.getInt(27)
                    val followers = cursor.getInt(28)
                    val following = cursor.getInt(29)
                    val createdAt = cursor.getStringOrNull(30)
                    val updatedAt = cursor.getStringOrNull(31)
                    listUsers.add(Users(login,id,nodeId,avatarUrl,gravatarId,url,htmlUrl,followersUrl,
                        followingUrl,gistUrl,starredUrl,subscriptionsUrl,organizationUrl,reposUrl,eventsUrl,
                        receivedEventsUrl,type,siteAdmin,name,company,blog,location,email,hireable,bio,
                        twitterUsername,publicRepos,publicGists,followers,following,createdAt,updatedAt))
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return listUsers
    }

    override fun getContentValue(): List<Users> = getUser(context)

}