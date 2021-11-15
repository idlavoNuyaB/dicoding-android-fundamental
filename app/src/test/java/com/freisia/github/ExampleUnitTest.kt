package com.freisia.github

import androidx.room.Room
import org.junit.Test

import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.freisia.github.data.db.GithubDatabase
import com.freisia.github.data.db.dao.UserDao
import com.freisia.github.data.model.Users
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.After
import org.junit.Before
import java.io.IOException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {

    private lateinit var userDao: UserDao
    private lateinit var db: GithubDatabase

    @Before
    fun createDB(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context,GithubDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(Exception::class)
    fun insert() {
        val user = Users("idlavoNuyaB",36811359,"MDQ6VXNlcjM2ODExMzU5","https://avatars1.githubusercontent.com/u/36811359?v=4","","https://api.github.com/users/idlavoNuyaB","https://github.com/idlavoNuyaB",
        "https://api.github.com/users/idlavoNuyaB/followers","https://api.github.com/users/idlavoNuyaB/following{/other_user}","https://api.github.com/users/idlavoNuyaB/gists{/gist_id}","https://api.github.com/users/idlavoNuyaB/starred{/owner}{/repo}",
        "https://api.github.com/users/idlavoNuyaB/subscriptions","https://api.github.com/users/idlavoNuyaB/orgs","https://api.github.com/users/idlavoNuyaB/repos","https://api.github.com/users/idlavoNuyaB/events{/privacy}","https://api.github.com/users/idlavoNuyaB/received_events",
        "User",false,"Bayu Novaldi",null,"","Surabaya",null,null,null,null,15,0,3,3,"2018-02-25T08:59:59Z","2020-06-25T08:57:18Z")
        CoroutineScope(Dispatchers.IO).launch { userDao.insert(user) }
    }
}
