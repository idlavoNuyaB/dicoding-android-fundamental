package com.freisia.github.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.freisia.github.data.db.dao.UserDao
import com.freisia.github.data.model.Users

@Database(entities = [Users::class],version = 5,exportSchema = false)
abstract class GithubDatabase : RoomDatabase(){
    abstract fun userDao() : UserDao

    companion object{
        @Volatile
        private var INSTANCE: GithubDatabase? = null

        fun getDatabase(context: Context) : GithubDatabase{
            if(INSTANCE == null){
                synchronized(GithubDatabase::class.java){
                    if(INSTANCE == null){
                        INSTANCE = create(context)
                    }
                }
            }
            return INSTANCE as GithubDatabase
        }
        fun destroyInstance() {
            if(INSTANCE != null){
                INSTANCE?.close()
                INSTANCE = null
            }
        }
        private fun create(context: Context) : GithubDatabase{
            return Room.databaseBuilder(
                context.applicationContext,
                GithubDatabase::class.java,
                "github.db"
            )   .fallbackToDestructiveMigration()
                .build()
        }
    }
}