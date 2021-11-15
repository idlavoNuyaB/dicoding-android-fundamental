package com.freisia.github.detail.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.github.R
import com.freisia.github.api.model.Users
import com.freisia.github.adapter.ListAdapter
import kotlinx.android.synthetic.main.fragment_followers.*
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FollowersFragment(index : Int,name: String?,followerInt: Int?) : Fragment(),
    FollowersView,
    ListAdapter.OnLoadMoreListener {
    private var index = 1
    private var name: String? = null
    private var followerIndex = 1
    private lateinit var listUserAdapter: ListAdapter
    @Inject
    lateinit var followersPresenterImpl: FollowersPresenterImpl
    private var usersErs: ArrayList<Users?> = ArrayList()
    private var coroutineJobR : Job? = null
    private var coroutineJobLoad : Job? = null
    init {
        this.index = index
        this.name = name
        this.followerIndex = followerInt as Int
    }

    companion object{
        private lateinit var getFragment : FollowersFragment

        fun setFrag(fragment: FollowersFragment){
            getFragment = fragment
        }

        fun getFrag(): FollowersFragment {
            return getFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    private fun initRecyclerView(){
        list2.itemAnimator = DefaultItemAnimator()
        list2.layoutManager = LinearLayoutManager(this.requireContext())
        listUserAdapter = ListAdapter(usersErs, list2)
        listUserAdapter.onLoadMoreListener = this
        list2.adapter = listUserAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.retainInstance = true
        initRecyclerView()
        followersPresenterImpl =
            FollowersPresenterImpl(getFrag())
        loadings.visibility = View.VISIBLE
        white_views.visibility = View.VISIBLE
        list2.visibility = View.GONE
        coroutineJobR?.cancel()
        coroutineJobR = CoroutineScope(Dispatchers.Main).launch {
            delay(500L)
            followersPresenterImpl.follower(name,followerIndex)
        }
    }

    override fun getData(user: ArrayList<Users?>) {
        this.usersErs = user
        listUserAdapter.searchResult(usersErs)
        listUserAdapter.setLoad()
    }

    override fun notFound() {
        if(list2.visibility == View.VISIBLE){
            list2.visibility = View.GONE
        }
        loadings.visibility = View.GONE
        white_views.visibility = View.GONE
        nfs.visibility = View.VISIBLE
    }

    override fun found() {
        if(nfs.visibility == View.VISIBLE){
            nfs.visibility = View.GONE
        }
        loadings.visibility = View.GONE
        white_views.visibility = View.GONE
        list2.visibility = View.VISIBLE
    }

    override fun onLoadMore() {
        coroutineJobLoad?.cancel()
        coroutineJobLoad = CoroutineScope(Dispatchers.IO).launch {
            followersPresenterImpl.onFollowerLoadMore()
        }
    }
}
