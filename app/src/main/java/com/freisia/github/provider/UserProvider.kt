package com.freisia.github.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.freisia.github.R
import com.freisia.github.data.db.GithubDatabase
import com.freisia.github.data.db.dao.UserDao
import com.freisia.github.utilitas.Constant
import java.lang.IllegalArgumentException

class UserProvider : ContentProvider() {

    companion object{
        const val AUTHORITY = Constant.AUTHORITY
        private const val TABLE_NAME = Constant.TABLE_NAME

        private const val CODE_USERS_ALL = 1
        private const val CODE_USERS_ITEM = 2
    }

    private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, TABLE_NAME, CODE_USERS_ALL)
        addURI(AUTHORITY, "$TABLE_NAME/*", CODE_USERS_ITEM)
    }
    private lateinit var githubDatabase: GithubDatabase
    private var userDao: UserDao? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        when(sUriMatcher.match(uri)){
            1 -> {
                val context = context ?: return null
                val cursor = userDao?.findAllUserCursor()
                cursor?.setNotificationUri(context.contentResolver,uri)
                return cursor
            }
            else -> {
                throw IllegalArgumentException(context?.getString(R.string.erroruri,uri))
            }
        }
    }

    override fun onCreate(): Boolean {
        if(context != null){
            githubDatabase = GithubDatabase.getDatabase(context as Context)
            userDao = githubDatabase.userDao()
        }
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}