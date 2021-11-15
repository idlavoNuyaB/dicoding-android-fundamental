package com.freisia.github.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.freisia.github.R
import com.freisia.github.api.model.Users
import com.freisia.github.adapter.ListAdapter

import kotlinx.android.synthetic.main.fragment_following.*
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment(index : Int,name: String?,followingInt: Int?) : Fragment(),
    FollowingView, ListAdapter.OnLoadMoreListener {

    private var index = 1
    private var name: String? = null
    private var followingIndex = 1
    private lateinit var listUserAdapter: ListAdapter
    @Inject
    lateinit var followingPresenterImpl: FollowingPresenterImpl
    private var usersIng: ArrayList<Users?> = ArrayList()
    private var coroutineJobR : Job? = null
    private var coroutineJobLoad : Job? = null

    init {
        this.index = index
        this.name = name
        this.followingIndex = followingInt as Int
    }

    companion object{
        private lateinit var getFragment : FollowingFragment

        fun setFrag(fragment: FollowingFragment){
            getFragment = fragment
        }

        fun getFrag(): FollowingFragment {
            return getFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.retainInstance = true
        initRecyclerView()
        followingPresenterImpl =
            FollowingPresenterImpl(getFrag())
        coroutineJobR?.cancel()
        if(usersIng.isNullOrEmpty()){
            loadings2.visibility = View.VISIBLE
            white_views2.visibility = View.VISIBLE
            list3.visibility = View.GONE
        }
        coroutineJobR = CoroutineScope(Dispatchers.Main).launch {
            delay(500L)
            followingPresenterImpl.followings(name,followingIndex)
        }
    }

    private fun initRecyclerView(){
        list3.itemAnimator = DefaultItemAnimator()
        list3.layoutManager = LinearLayoutManager(this.requireActivity())
        listUserAdapter = ListAdapter(usersIng, list3)
        listUserAdapter.onLoadMoreListener = this
        list3.adapter = listUserAdapter
    }

    override fun getData(user: ArrayList<Users?>) {
        this.usersIng = user
        listUserAdapter.searchResult(usersIng)
        listUserAdapter.setLoad()
    }

    override fun notFound() {
        if(list3.visibility == View.VISIBLE){
            list3.visibility = View.GONE
        }
        loadings2.visibility = View.GONE
        white_views2.visibility = View.GONE
        nfs2.visibility = View.VISIBLE
    }

    override fun found() {
        if(nfs2.visibility == View.VISIBLE){
            nfs2.visibility = View.GONE
        }
        loadings2.visibility = View.GONE
        white_views2.visibility = View.GONE
        list3.visibility = View.VISIBLE
    }

    override fun onLoadMore() {
        coroutineJobLoad?.cancel()
        coroutineJobLoad = CoroutineScope(Dispatchers.IO).launch {
            followingPresenterImpl.onFollowingLoadMore()
        }
    }

}
