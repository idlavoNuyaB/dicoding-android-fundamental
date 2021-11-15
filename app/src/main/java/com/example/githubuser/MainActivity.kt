package com.example.githubuser

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val username : Array<String> = resources.getStringArray(R.array.username)
        val name : Array<String> = resources.getStringArray(R.array.name)
        val location : Array<String> = resources.getStringArray(R.array.location)
        val strrepository : Array<String> = resources.getStringArray(R.array.repository)
        val company : Array<String> = resources.getStringArray(R.array.company)
        val strfollowers : Array<String> = resources.getStringArray(R.array.followers)
        val strfollowing : Array<String> = resources.getStringArray(R.array.following)
        val stravatar : Array<String> = resources.getStringArray(R.array.avatar)
        val repository = strrepository.map { it.toInt() }
        val followers = strfollowers.map { it.toInt() }
        val following = strfollowing.map { it.toInt() }
        var listUser = ArrayList<User>()
        for((index,user) in username.withIndex()){
            val prepath : String = stravatar.get(index).removeRange(
                startIndex = 0,
                endIndex = 4
            )
            val paths = prepath.removeSuffix(".png")
            val path = paths.removeRange(
                startIndex = 8,
                endIndex = 17
            )
            Log.d("PATHAPON",path)
            val resourceId = resources.getIdentifier(path, null, packageName)
            listUser.add(User(user,name.get(index),location.get(index),repository.get(index)
                ,company.get(index),followers.get(index),following.get(index),Uri.parse("android.resource://" + R::class.java.getPackage()!!.name + "/" + resourceId).toString()))
        }
        val userAdapter = UserListAdapter(listUser)
        list1.apply{
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }
}
