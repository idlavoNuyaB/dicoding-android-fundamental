package com.freisia.consumerapp.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.database.getStringOrNull
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.freisia.consumerapp.R
import com.freisia.consumerapp.data.model.Users
import com.freisia.consumerapp.ui.detail.DetailActivity
import com.freisia.consumerapp.utilitas.Constant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems : ArrayList<Users> = ArrayList()

    override fun onCreate() {
        val listUsers = ArrayList<Users>()
        val identifyToken = Binder.clearCallingIdentity()
        CoroutineScope(Dispatchers.IO).launch {
            val cursor = mContext.contentResolver.query(
                Constant.CONTENT_URI,
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
        }
        mWidgetItems = listUsers
        Binder.restoreCallingIdentity(identifyToken)
    }

    override fun onDataSetChanged() {
        mWidgetItems.clear()
        val listUsers = ArrayList<Users>()
        val identifyToken = Binder.clearCallingIdentity()
        val cursor = mContext.contentResolver.query(
            Constant.CONTENT_URI,
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
        mWidgetItems = listUsers
        Binder.restoreCallingIdentity(identifyToken)
    }


    override fun onDestroy() {
        mWidgetItems.clear()
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        Log.d("DICODING",position.toString())
        if(position <= count){
            val item = mWidgetItems[position]
            Log.d("DICODING",item.toString())
            var bitmap : Bitmap? = null
            try {
                @GlideModule
                bitmap = Glide.with(mContext).asBitmap().load(item.avatarUrl).error(R.mipmap.ic_launcher).submit().get()
            }catch (e: Exception){
                e.printStackTrace()
            }
            rv.setImageViewBitmap(R.id.imageView,bitmap)
            val intent = Intent()
            val bundle = Bundle()
            bundle.putParcelable("User",item)
            intent.putExtra(DetailActivity.EXTRA_ITEM,bundle)
            rv.setOnClickFillInIntent(R.id.imageView,intent)

        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false


}