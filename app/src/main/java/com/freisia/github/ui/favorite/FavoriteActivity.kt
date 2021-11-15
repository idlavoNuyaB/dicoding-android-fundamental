package com.freisia.github.ui.favorite


import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
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
import com.freisia.github.ui.search.SearchActivity
import com.freisia.github.ui.setting.SettingsActivity
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
    private var checkFirst : Int = 0

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
            R.id.notification -> {
                val intent = Intent(this, SettingsActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            }
            R.id.deleteall -> {
                val positiveButtonClick = { _: DialogInterface, _: Int ->
                    favoriteViewModel.deleteAll()
                    when(check){
                        1 -> {
                            users.clear()
                            listUserAdapter.searchResult(users)
                            listUserAdapter.setLoad()
                            }
                        2 -> {
                            users.clear()
                            cardAdapter.searchResult(users)
                            cardAdapter.setLoad()
                        }
                        3 -> {
                            users.clear()
                            gridUserAdapter.searchResult(users)
                            gridUserAdapter.setLoad()
                        }
                    }
                }
                val negativeButtonClick = { _: DialogInterface, _: Int ->
                    Toast.makeText(this,R.string.cancel,Toast.LENGTH_LONG).show()
                }
                val builder = AlertDialog.Builder(this)
                with(builder){
                    setTitle(R.string.deletea)
                    setMessage(R.string.messagedeletea)
                    setPositiveButton(R.string.yes,DialogInterface.OnClickListener(positiveButtonClick))
                    setNegativeButton(R.string.no,negativeButtonClick)
                    val dialog = create()
                    dialog.show()
                    val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

                    val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
                    layoutParams.weight = 10f
                    btnPositive.layoutParams = layoutParams
                    btnNegative.layoutParams = layoutParams
                }
            }
            R.id.infodelete -> {
                val builder = AlertDialog.Builder(this)
                with(builder){
                    setTitle(R.string.infodel)
                    setMessage(R.string.infonya)
                    val dialog = create()
                    dialog.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val a = Intent(this.applicationContext, SearchActivity::class.java)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteViewModel.destroy()
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
            val intent = Intent(this,SearchActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    override fun onLoadMore() {
    }

    override fun onGridLoadMore() {
    }

    override fun onCardLoadMore() {
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
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            favoriteViewModel.delete(item)
            when(check){
                1 -> {
                    for(position in 0 until users.size){
                        if(users[position] == item){
                            users.removeAt(position)
                            listUserAdapter.searchResult(users)
                            listUserAdapter.setLoad()
                        }
                    }
                }
            }
        }
        val negativeButtonClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this,R.string.cancel,Toast.LENGTH_LONG).show()
        }
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(this@FavoriteActivity.getString(R.string.delete,item.login))
            setMessage(this@FavoriteActivity.getString(R.string.messagedelete,item.login))
            setPositiveButton(R.string.yes,DialogInterface.OnClickListener(positiveButtonClick))
            setNegativeButton(R.string.no,negativeButtonClick)
            val dialog = create()
            dialog.show()
            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams
        }
    }

    override fun onGridLongClicks(item: Users) {
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            favoriteViewModel.delete(item)
            when(check){
                3 ->{
                    for(position in 0 until users.size){
                        if(users[position] == item){
                            users.removeAt(position)
                            gridUserAdapter.searchResult(users)
                            gridUserAdapter.setLoad()
                        }
                    }
                }
            }
        }
        val negativeButtonClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this,R.string.cancel,Toast.LENGTH_LONG).show()
        }
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(this@FavoriteActivity.getString(R.string.delete,item.login))
            setMessage(this@FavoriteActivity.getString(R.string.messagedelete,item.login))
            setPositiveButton(R.string.yes,DialogInterface.OnClickListener(positiveButtonClick))
            setNegativeButton(R.string.no,negativeButtonClick)
            val dialog = create()
            dialog.show()
            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams
        }
    }

    override fun onCardClicks(item: Users) {
        val positiveButtonClick = { _: DialogInterface, _: Int ->
            favoriteViewModel.delete(item)
            when(check){
                2 -> {
                    for(position in 0 until users.size){
                        if(users[position] == item){
                            users.removeAt(position)
                            cardAdapter.searchResult(users)
                            cardAdapter.setLoad()
                        }
                    }
                }
            }
        }
        val negativeButtonClick = { _: DialogInterface, _: Int ->
            Toast.makeText(this,R.string.cancel,Toast.LENGTH_LONG).show()
        }
        val builder = AlertDialog.Builder(this)
        with(builder){
            setTitle(this@FavoriteActivity.getString(R.string.delete,item.login))
            setMessage(this@FavoriteActivity.getString(R.string.messagedelete,item.login))
            setPositiveButton(R.string.yes,DialogInterface.OnClickListener(positiveButtonClick))
            setNegativeButton(R.string.no,negativeButtonClick)
            val dialog = create()
            dialog.show()
            val btnPositive = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val btnNegative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val layoutParams = btnPositive.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 10f
            btnPositive.layoutParams = layoutParams
            btnNegative.layoutParams = layoutParams
        }
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
            users = allUser as ArrayList<Users?>
            if(checkFirst == 0){
                favoriteViewModel.read(total)
                checkFirst = 1
            }
        }
    }
}
