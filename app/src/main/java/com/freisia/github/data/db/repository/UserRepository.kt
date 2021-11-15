package com.freisia.github.data.db.repository

import androidx.lifecycle.LiveData
import com.freisia.github.data.db.dao.UserDao
import com.freisia.github.data.model.Users

class UserRepository(private val userDao: UserDao) {
    val allUser: LiveData<List<Users>> = userDao.findAllUser()

    suspend fun insert(user: Users) : Long {
        return userDao.insert(user)
    }
    suspend fun delete(user: Users) = userDao.delete(user)

    suspend fun deleteAll() = userDao.deleteAll()

}