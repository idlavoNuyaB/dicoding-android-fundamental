package com.freisia.github.ui.detail.following

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager

import com.freisia.github.R
import com.freisia.github.data.model.Users
import com.freisia.github.adapter.ListAdapter

import kotlinx.android.synthetic.main.fragment_following.*

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment(index : Int,name: String?,followingInt: Int?) : Fragment(),
    ListAdapter.OnLoadMoreListener,
    ListAdapter.OnLongClickers{

    private var index = 1
    private var name: String? = null
    private var followingIndex = 1
    private lateinit var listUserAdapter: ListAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private var usersIng: ArrayList<Users?> = ArrayList()
    private var isFound = false

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
        followingViewModel = ViewModelProvider(getFrag()).get(FollowingViewModel::class.java)
        initRecyclerView()
        loadings2.visibility = View.VISIBLE
        white_views2.visibility = View.VISIBLE
        list3.visibility = View.GONE
        followingViewModel.checkFollowing(name as String,followingIndex)
        followingViewModel.isFounds.observe(viewLifecycleOwner, Observer { it ->
            isFound = it
            if (isFound){
                followingViewModel.listFollowing.observe(viewLifecycleOwner, Observer {
                    usersIng = it as ArrayList<Users?>
                    found()
                    getData()
                })
            } else {
                notFound()
            }
        })
    }

    private fun initRecyclerView(){
        list3.itemAnimator = DefaultItemAnimator()
        list3.layoutManager = LinearLayoutManager(this.requireActivity())
        listUserAdapter = ListAdapter(usersIng, list3)
        listUserAdapter.onLoadMoreListener = this
        listUserAdapter.onLongClicker = this
        list3.adapter = listUserAdapter
    }

    private fun getData() {
        listUserAdapter.searchResult(usersIng)
        listUserAdapter.setLoad()
    }

    private fun notFound() {
        if(list3.visibility == View.VISIBLE){
            list3.visibility = View.GONE
        }
        loadings2.visibility = View.GONE
        white_views2.visibility = View.GONE
        nfs2.visibility = View.VISIBLE
    }

    private fun found() {
        if(nfs2.visibility == View.VISIBLE){
            nfs2.visibility = View.GONE
        }
        loadings2.visibility = View.GONE
        white_views2.visibility = View.GONE
        list3.visibility = View.VISIBLE
    }

    override fun onLoadMore() {
        followingViewModel.onLoadMore()
        followingViewModel.isFounds.observe(viewLifecycleOwner, Observer { it ->
            isFound = it
            if (isFound){
                followingViewModel.listFollowing.observe(viewLifecycleOwner, Observer {
                    usersIng = it as ArrayList<Users?>
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
