package com.freisia.consumerapp.ui.detail.follower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.freisia.consumerapp.R
import com.freisia.consumerapp.data.model.Users
import com.freisia.consumerapp.adapter.ListAdapter
import kotlinx.android.synthetic.main.fragment_followers.*

/**
 * A simple [Fragment] subclass.
 */
class FollowersFragment(index : Int,name: String?,followerInt: Int?) : Fragment(),
    ListAdapter.OnLoadMoreListener,
    ListAdapter.OnLongClickers{
    private var index = 1
    private var name: String? = null
    private var followerIndex = 1
    private lateinit var listUserAdapter: ListAdapter
    private lateinit var followersViewModel: FollowersViewModel
    private var usersErs: ArrayList<Users?> = ArrayList()
    private var isFound = false
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
        listUserAdapter.onLongClicker = this
        list2.adapter = listUserAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.retainInstance = true
        initRecyclerView()
        followersViewModel = ViewModelProvider(getFrag()).get(FollowersViewModel::class.java)
        loadings.visibility = View.VISIBLE
        white_views.visibility = View.VISIBLE
        list2.visibility = View.GONE
        followersViewModel.checkFollower(name as String,followerIndex)
        followersViewModel.isFounds.observe(viewLifecycleOwner, Observer{ it ->
            isFound = it
            if (isFound){
                followersViewModel.listFollowers.observe(viewLifecycleOwner, Observer {
                    usersErs = it as ArrayList<Users?>
                    found()
                    getData()
                })
            } else {
                notFound()
            }
        })
    }

    private fun getData() {
        listUserAdapter.searchResult(usersErs)
        listUserAdapter.setLoad()
    }

    private fun notFound() {
        if(list2.visibility == View.VISIBLE){
            list2.visibility = View.GONE
        }
        loadings.visibility = View.GONE
        white_views.visibility = View.GONE
        nfs.visibility = View.VISIBLE
    }

    private fun found() {
        if(nfs.visibility == View.VISIBLE){
            nfs.visibility = View.GONE
        }
        loadings.visibility = View.GONE
        white_views.visibility = View.GONE
        list2.visibility = View.VISIBLE
    }

    override fun onLoadMore() {
        followersViewModel.onLoadMore()
        followersViewModel.isFounds.observe(viewLifecycleOwner, Observer{ it ->
            isFound = it
            if (isFound){
                followersViewModel.listFollowers.observe(viewLifecycleOwner, Observer {
                    usersErs = it as ArrayList<Users?>
                    found()
                    getData()

                })
            } else {
                notFound()
            }
        })
    }

    override fun onLongClicks(item: Users) {
        Toast.makeText(this.requireContext(),item.login, Toast.LENGTH_LONG).show()
    }
}
