package com.freisia.github.ui.detail

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.freisia.github.R
import com.freisia.github.adapter.SectionPagerAdapter
import com.freisia.github.data.model.Users
import com.freisia.github.ui.favorite.FavoriteActivity
import com.freisia.github.ui.search.SearchActivity
import com.freisia.github.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_detail.*
import java.util.*
import kotlin.properties.Delegates

class DetailActivity : AppCompatActivity(){

    private var followingInt : Int = 0
    private var followersInt : Int = 0
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var username : String
    private lateinit var user : Users
    private var isFavorite by Delegates.notNull<Boolean>()

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_ITEM = "extra_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        initToolbar()
        backPress()
        getData()
        initTabLayout()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menuin -> {
                val locale = Locale("in")
                changeLocale(locale)
            }
            R.id.menuenus -> {
                val locale = Locale("en-US")
                changeLocale(locale)
            }
            R.id.favorite -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.notification -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @Suppress("DEPRECATION")
    private fun changeLocale(locale : Locale){
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        recreate()
    }

    private fun initToolbar(){
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.toolbar_detail)
            elevation = 0f
        }
        val actionBar = supportActionBar
        val toolbar = actionBar?.customView?.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.contentInsetEnd
        toolbar.setPadding(0, 0, 0, 0)
    }

    private fun backPress(){
        val imageHome = findViewById<ImageView>(R.id.back)
        imageHome.setOnClickListener {
            finish()
        }
    }

    private fun initTabLayout(){
        val sectionPagerAdapter = SectionPagerAdapter(
            this,
            supportFragmentManager,
            username,
            followersInt,
            followingInt
        )
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }
    private fun getData(){
        val bundle = intent.getBundleExtra(EXTRA_ITEM)
        user = if(bundle != null){
            bundle.getParcelable("User")!!
        } else {
            intent.getParcelableExtra(EXTRA_USER) as Users
        }
        val tvName : TextView = findViewById(R.id.name)
        val tvUsername : TextView = findViewById(R.id.username)
        val tvCompany : TextView = findViewById(R.id.company)
        val tvLoc : TextView = findViewById(R.id.location)
        val tvRepo : TextView = findViewById(R.id.repo)
        val tvFollowers : TextView = findViewById(R.id.followers)
        val tvFollowing : TextView = findViewById(R.id.following)
        val tvAva : ImageView = findViewById(R.id.avatar)
        val name = user.name
        username = user.login
        var company = user.company
        var loc = user.location
        if(company.isNullOrEmpty()){
           company = getString(R.string.empty)
        }
        if(loc.isNullOrEmpty()){
            loc = getString(R.string.empty)
        }
        val repo = getString(R.string.repo,user.publicRepo.toString())
        val followers = getString(R.string.followers,user.followers.toString())
        followersInt = user.followers
        val following = getString(R.string.following,user.following.toString())
        followingInt = user.following
        val avatar = user.avatarUrl
        tvName.text = name
        tvUsername.text = username
        tvCompany.text = company
        tvLoc.text = loc
        tvRepo.text = repo
        tvFollowers.text = followers
        tvFollowing.text = following
        Glide.with(applicationContext).load(avatar).error(R.mipmap.ic_launcher).into(tvAva)
        check()
    }

    override fun onDestroy() {
        super.onDestroy()
        detailViewModel.destroy()
        detailViewModel.allUser.removeObserver(observe())
    }

    override fun onBackPressed() {
        val a = Intent(this.applicationContext, SearchActivity::class.java)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun check(){
        detailViewModel.allUser.observeForever(observe())
    }

    private fun observe() : Observer<List<Users>>{
        return Observer {
                users ->
            if(!users.isNullOrEmpty()){
                for(i in users.indices){
                    if(users[i] == user){
                        isFavorite = true
                        break
                    }
                    else{
                        isFavorite = false
                    }
                }
                if(!isFavorite){
                    fab.setImageResource(R.drawable.ic_action_favorite_off)
                } else {
                    fab.setImageResource(R.drawable.ic_action_favorite_on)
                }

            } else {
                isFavorite = false
                if(!isFavorite){
                    fab.setImageResource(R.drawable.ic_action_favorite_off)
                } else {
                    fab.setImageResource(R.drawable.ic_action_favorite_on)
                }
            }
            fab.setOnClickListener{
                if(!isFavorite){
                    detailViewModel.insert(user)
                    fab.setImageResource(R.drawable.ic_action_favorite_on)
                } else {
                    detailViewModel.delete(user)
                    fab.setImageResource(R.drawable.ic_action_favorite_off)
                }
            }
        }
    }
}
