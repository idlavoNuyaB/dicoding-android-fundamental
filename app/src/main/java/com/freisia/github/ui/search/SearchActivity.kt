package com.freisia.github.ui.search

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
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.github.R
import com.freisia.github.adapter.CardAdapter
import com.freisia.github.adapter.GridAdapter
import com.freisia.github.adapter.ListAdapter
import com.freisia.github.data.model.Users
import com.freisia.github.ui.favorite.FavoriteActivity
import com.freisia.github.ui.setting.SettingsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class SearchActivity : AppCompatActivity(),
    ListAdapter.OnLoadMoreListener,
    ListAdapter.OnLongClickers,
    GridAdapter.OnLoadMoreListener,
    GridAdapter.OnLongClickers,
    CardAdapter.OnLoadMoreListener,
    CardAdapter.OnLongClickers{

    private lateinit var listUserAdapter: ListAdapter
    private lateinit var gridUserAdapter: GridAdapter
    private lateinit var cardAdapter: CardAdapter
    private lateinit var searchViewModel: SearchViewModel
    private var users: ArrayList<Users?> = ArrayList()
    private var coroutineJobR : Job? = null
    private var check : Int = 0

    companion object {
        private const val TIME_PICKER_REPEAT_TAG = "TimePickerRepeat"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar()
        initRecyclerView()
        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private fun initRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = LinearLayoutManager(this)
        listUserAdapter = ListAdapter(users, list1)
        listUserAdapter.onLoadMoreListener = this
        listUserAdapter.onLongClicker = this
        list1.adapter = listUserAdapter
        check = 1
    }

    private fun cardRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(users, list1)
        cardAdapter.onLoadMoreListener = this
        cardAdapter.onLongClickers = this
        list1.adapter = cardAdapter
        check = 2
    }

    private fun gridRecyclerView(){
        list1.itemAnimator = DefaultItemAnimator()
        list1.layoutManager = GridLayoutManager(this,2)
        gridUserAdapter = GridAdapter(users, list1)
        gridUserAdapter.onLoadMoreListener = this
        gridUserAdapter.onLongClickers = this
        list1.adapter = gridUserAdapter
        check = 3
    }

    private fun toolbar(){
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setDisplayShowCustomEnabled(true)
            setCustomView(R.layout.toolbar)
            elevation = 0f
        }
        val actionBar = supportActionBar
        val toolbar = actionBar?.customView?.parent as Toolbar
        toolbar.setContentInsetsAbsolute(0, 0)
        toolbar.contentInsetEnd
        toolbar.setPadding(0, 0, 0, 0)
        val view : View = toolbar.getChildAt(0)
        view.setOnClickListener{
            val intent = Intent(this,SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item)
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
                if(nf.visibility == View.VISIBLE) nf.visibility = View.GONE
                if(list1.visibility == View.VISIBLE) list1.visibility = View.GONE
                loading.visibility = View.VISIBLE
                white_view.visibility = View.VISIBLE
                coroutineJobR = CoroutineScope(Dispatchers.IO).launch{
                    delay(1000)
                    searchViewModel.searchResult(newText)
                }
                searchViewModel.isLoading.observeForever(loadingObserver())
                searchViewModel.isFounds.observeForever(observe())
                return true
            }
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        searchViewModel.isFounds.removeObserver(observe())
        searchViewModel.listSearch.removeObserver(searchObserver())
        coroutineJobR?.cancel()
    }

    private fun getUser() {
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

    private fun notFound() {
        if(list1.visibility == View.VISIBLE){
           list1.visibility = View.GONE
        }
        loading.visibility = View.GONE
        white_view.visibility = View.GONE
        nf.visibility = View.VISIBLE
    }

    private fun found() {
        if(nf.visibility == View.VISIBLE){
            nf.visibility = View.GONE
        }
        loading.visibility = View.GONE
        white_view.visibility = View.GONE
        list1.visibility = View.VISIBLE
    }

    private fun setMode(selectedMode : MenuItem){
        when(selectedMode.itemId){
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
            R.id.favorite -> {
                val intent = Intent(this,FavoriteActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.notification -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
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

    override fun onLoadMore() {
        searchViewModel.onLoadMore()
        searchViewModel.isFounds.observeForever(observe())
    }

    override fun onGridLoadMore() {
        searchViewModel.onLoadMore()
        searchViewModel.isFounds.observeForever(observe())
    }

    override fun onCardLoadMore() {
        searchViewModel.onLoadMore()
        searchViewModel.isFounds.observeForever(observe())}

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun observe() : Observer<Boolean>{
        return Observer { isFound ->
            if(isFound){
                searchViewModel.listSearch.observeForever(searchObserver())
            }
            else {
                notFound()
            }
        }
    }

    private fun searchObserver() : Observer<List<Users?>>{
        return Observer {
            users = it as ArrayList<Users?>
            found()
            getUser()
        }
    }

    private fun loadingObserver() : Observer<Boolean>{
        return Observer {
            if(it){
                loading.visibility = View.VISIBLE
                white_view.visibility = View.VISIBLE
            } else {
                loading.visibility = View.GONE
                white_view.visibility = View.GONE
            }
        }
    }

    override fun onLongClicks(item: Users) {
        Toast.makeText(this,item.login, Toast.LENGTH_SHORT).show()
    }

    override fun onGridLongClicks(item: Users) {
        Toast.makeText(this,item.login, Toast.LENGTH_SHORT).show()
    }

    override fun onCardClicks(item: Users) {
        Toast.makeText(this,item.login, Toast.LENGTH_SHORT).show()
    }
}
