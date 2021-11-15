package com.freisia.github.data.db.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import com.freisia.github.data.model.Users

@Dao
interface UserDao {
    @Query("SELECT * FROM User ORDER BY Lower(name) ASC")
    fun findAllUser() : LiveData<List<Users>>

    @Query("SELECT * FROM User ORDER BY Lower(name) ASC")
    fun findAllUserCursor() : Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: Users) : Long

    @Delete
    suspend fun delete(user: Users)

    @Query("DELETE FROM User")
    suspend fun deleteAll()
}