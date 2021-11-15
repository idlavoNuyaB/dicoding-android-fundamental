package com.freisia.github.search

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.github.R
import com.freisia.github.adapter.CardAdapter
import com.freisia.github.adapter.GridAdapter
import com.freisia.github.adapter.ListAdapter
import com.freisia.github.api.model.Users
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(),SearchViews,
    ListAdapter.OnLoadMoreListener,
    GridAdapter.OnLoadMoreListener,
    CardAdapter.OnLoadMoreListener {

    private lateinit var listUserAdapter: ListAdapter
    private lateinit var gridUserAdapter: GridAdapter
    private lateinit var cardAdapter: CardAdapter
    private lateinit var searchPresenterImpl : SearchPresenterImpl
    private var users: ArrayList<Users?> = ArrayList()
    private var coroutineJobR : Job? = null
    private var coroutineJobLoad : Job? = null
    private var check : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar()
        initRecyclerView()
        searchPresenterImpl = SearchPresenterImpl(this)
    }

    private fun initRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = LinearLayoutManager(this)
        listUserAdapter = ListAdapter(users, list1)
        listUserAdapter.onLoadMoreListener = this
        list1.adapter = listUserAdapter
        check = 1
    }

    private fun cardRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(users, list1)
        cardAdapter.onLoadMoreListener = this
        list1.adapter = cardAdapter
        check = 2
    }

    private fun gridRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = GridLayoutManager(this,2)
        gridUserAdapter = GridAdapter(users, list1)
        gridUserAdapter.onLoadMoreListener = this
        list1.adapter = gridUserAdapter
        check = 3
    }

    private fun toolbar(){
        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.toolbar)
        supportActionBar!!.elevation = 0f
        val actionBar = supportActionBar
        val toolbar = actionBar!!.customView.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.contentInsetEnd
        toolbar.setPadding(0, 0, 0, 0)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String): Boolean {
                coroutineJobR?.cancel()
                loading.visibility = View.VISIBLE
                white_view.visibility = View.VISIBLE
                if(nf.visibility == View.VISIBLE) nf.visibility = View.GONE
                if(list1.visibility == View.VISIBLE) list1.visibility = View.GONE
                coroutineJobR = CoroutineScope(Dispatchers.IO).launch{
                    delay(1000)
                    searchPresenterImpl.searchResult(newText)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun getUser(user: ArrayList<Users?>) {
        this.users = user
        when (check) {
            1 -> {
                listUserAdapter.searchResult(users)
                listUserAdapter.setLoad()
            }
            2 -> {
                cardAdapter.searchResult(users)
                cardAdapter.setLoad()
            }
            else -> {
                gridUserAdapter.searchResult(users)
                gridUserAdapter.setLoad()
            }
        }
    }

    override fun notFound() {
        if(list1.visibility == View.VISIBLE){
           list1.visibility = View.GONE
        }
        loading.visibility = View.GONE
        white_view.visibility = View.GONE
        nf.visibility = View.VISIBLE
    }

    override fun found() {
        if(nf.visibility == View.VISIBLE){
            nf.visibility = View.GONE
        }
        loading.visibility = View.GONE
        white_view.visibility = View.GONE
        list1.visibility = View.VISIBLE
    }

    override fun onLoadMore() {
        coroutineJobLoad?.cancel()
        coroutineJobLoad = CoroutineScope(Dispatchers.IO).launch {
            searchPresenterImpl.onLoadMore()
        }
    }

    private fun setMode(selectedMode : Int){
        when(selectedMode){
            R.id.menulist -> {
                initRecyclerView()
            }
            R.id.menucard -> {
                cardRecyclerView()
            }
            R.id.menugrid -> {
                gridRecyclerView()
            }
            R.id.menuin -> {
                val locale = Locale("in")
                changeLocale(locale)
            }
            R.id.menuenus -> {
                val locale = Locale("en-US")
                changeLocale(locale)
            }

        }
    }

    private fun changeLocale(locale : Locale){
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        recreate()
    }

    override fun onGridLoadMore() {
        coroutineJobLoad?.cancel()
        coroutineJobLoad = CoroutineScope(Dispatchers.IO).launch {
            searchPresenterImpl.onLoadMore()
        }
    }

    override fun onCardLoadMore() {
        coroutineJobLoad?.cancel()
        coroutineJobLoad = CoroutineScope(Dispatchers.IO).launch {
            searchPresenterImpl.onLoadMore()
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }
}
