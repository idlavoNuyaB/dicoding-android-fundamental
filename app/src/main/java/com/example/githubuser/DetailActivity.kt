package com.example.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.title = "Detail"
        val tvname : TextView = findViewById(R.id.name)
        val tvusername : TextView = findViewById(R.id.username)
        val tvcompany : TextView = findViewById(R.id.company)
        val tvloc : TextView = findViewById(R.id.location)
        val tvrepo : TextView = findViewById(R.id.repo)
        val tvfollowers : TextView = findViewById(R.id.followers)
        val tvfollowing : TextView = findViewById(R.id.following)
        val tvava : ImageView = findViewById(R.id.avatar)

        val user = intent.getParcelableExtra<User>(EXTRA_USER)
        val name = user.name
        val username = user.username
        val company = user.company
        val loc = user.location
        var repo = "${user.repository} Repository"
        var followers = "${user.followers} Followers"
        var following = "${user.following} Following"
        var avatar = user.avatar

        tvname.text = name
        tvusername.text = username
        tvcompany.text = company
        tvloc.text = loc
        tvrepo.text = repo
        tvfollowers.text = followers
        tvfollowing.text = following
        Glide.with(applicationContext).load(avatar).into(tvava)
    }
}
