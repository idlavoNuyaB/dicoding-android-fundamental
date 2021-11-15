package com.freisia.consumerapp.ui.favorite


import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.consumerapp.R
import com.freisia.consumerapp.adapter.CardAdapter
import com.freisia.consumerapp.adapter.GridAdapter
import com.freisia.consumerapp.adapter.ListAdapter
import com.freisia.consumerapp.data.model.Users
import kotlinx.android.synthetic.main.activity_favorite.*
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class FavoriteActivity : AppCompatActivity(),
    ListAdapter.OnLoadMoreListener,
    ListAdapter.OnLongClickers,
    GridAdapter.OnLoadMoreListener,
    GridAdapter.OnLongClickers,
    CardAdapter.OnLoadMoreListener,
    CardAdapter.OnLongClickers{

    private lateinit var listUserAdapter: ListAdapter
    private lateinit var gridUserAdapter: GridAdapter
    private lateinit var cardAdapter: CardAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel
    private var users: ArrayList<Users?> = ArrayList()
    private lateinit var allUser : List<Users>
    private var check : Int = 0
    private var total : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        loadingf.visibility = View.VISIBLE
        white_viewf.visibility = View.VISIBLE
        initToolbar()
        initRecyclerView()
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        favoriteViewModel.allUser.observeForever(userObserver())
        favoriteViewModel.isLoading.observeForever(loadingObserver())
        favoriteViewModel.isFounds.observeForever(foundObserve())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_favorite, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
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
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    private fun changeLocale(locale : Locale){
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
        recreate()
    }

    private fun initRecyclerView() {
        list1f.itemAnimator = DefaultItemAnimator()
        list1f.layoutManager = LinearLayoutManager(this)
        listUserAdapter = ListAdapter(users, list1f)
        listUserAdapter.onLoadMoreListener = this
        listUserAdapter.onLongClicker = this
        list1f.adapter = listUserAdapter
        check = 1
    }

    private fun cardRecyclerView() {
        list1f.itemAnimator = DefaultItemAnimator()
        list1f.layoutManager = LinearLayoutManager(this)
        cardAdapter = CardAdapter(users, list1f)
        cardAdapter.onLoadMoreListener = this
        cardAdapter.onLongClickers = this
        list1f.adapter = cardAdapter
        check = 2
    }

    private fun gridRecyclerView() {
        list1f.itemAnimator = DefaultItemAnimator()
        list1f.layoutManager = GridLayoutManager(this,2)
        gridUserAdapter = GridAdapter(users, list1f)
        gridUserAdapter.onLoadMoreListener = this
        gridUserAdapter.onLongClickers = this
        list1f.adapter = gridUserAdapter
        check = 3
    }

    private fun initToolbar(){
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
            val intent = Intent(this,FavoriteActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onLoadMore() {
        favoriteViewModel.onLoadMore()
    }

    override fun onGridLoadMore() {
        favoriteViewModel.onLoadMore()
    }

    override fun onCardLoadMore() {
        favoriteViewModel.onLoadMore()
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
        if(list1f.visibility != View.GONE){
            list1f.visibility = View.GONE
        }
        nff.visibility = View.VISIBLE
        loadingf.visibility = View.GONE
        white_viewf.visibility = View.GONE
    }

    private fun found() {
        if(nff.visibility != View.GONE){
            nff.visibility = View.GONE
        }
        list1f.visibility = View.VISIBLE
        loadingf.visibility = View.GONE
        white_viewf.visibility = View.GONE
    }

    override fun onLongClicks(item: Users) {
        Toast.makeText(this,item.login,Toast.LENGTH_LONG).show()
    }

    override fun onGridLongClicks(item: Users) {
        Toast.makeText(this,item.login,Toast.LENGTH_LONG).show()
    }

    override fun onCardClicks(item: Users) {
        Toast.makeText(this,item.login,Toast.LENGTH_LONG).show()
    }

    private fun foundObserve() : Observer<Boolean>{
        return Observer {isFound ->
            if(isFound){
                found()
                getUser()
            }
            else {
                notFound()
            }
        }
    }

    private fun readObserve() : Observer<List<Users>>{
        return Observer {
            users = it as ArrayList<Users?>
            favoriteViewModel.read(total)
        }
    }

    private fun loadingObserver() : Observer<Boolean>{
        return Observer {
            if(it){
                loadingf.visibility = View.VISIBLE
                white_viewf.visibility = View.VISIBLE
            } else {
                loadingf.visibility = View.GONE
                white_viewf.visibility = View.GONE
            }
        }
    }

    private fun userObserver() : Observer<List<Users>>{
        return Observer {
            allUser = it
            total = if(!allUser.isNullOrEmpty()){
                allUser.size
            } else {
                0
            }
            favoriteViewModel.allUserByPage.observeForever(readObserve())
        }
    }
}
