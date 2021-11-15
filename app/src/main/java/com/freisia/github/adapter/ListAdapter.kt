package com.freisia.github.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.github.detail.DetailActivity
import com.freisia.github.R
import com.freisia.github.api.model.Users
import kotlinx.android.synthetic.main.layout_item_list.view.*
import kotlin.collections.ArrayList

class ListAdapter(user: ArrayList<Users?>,recyclerView: RecyclerView) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private var filtered : ArrayList<Users?> = ArrayList()
    private var loading: Boolean = false
    lateinit var onLoadMoreListener: OnLoadMoreListener
    init {
        filtered = user
        if(recyclerView.layoutManager is LinearLayoutManager){
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if(!loading && totalItemCount - 1 <= lastVisible && lastVisible > filtered.size - 2){
                        onLoadMoreListener.onLoadMore()
                        loading = true
                    }
                }
            })
        }
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v),View.OnClickListener{
        private val avatarUser = v.image
        private val nameUser = v.nama
        private val usernameUser = v.username
        private val followingUser = v.following
        private val followersUser = v.follower
        private val repoUser = v.repo
        init{
            v.setOnClickListener(this)
        }
        fun bindUser(user : Users){
            Glide.with(itemView.context).load(user.avatarUrl).error(R.mipmap.ic_launcher_round).into(avatarUser)
            nameUser.text = user.name
            usernameUser.text = user.login
            followersUser.text = itemView.context.getString(R.string.followers,user.followers.toString())
            followingUser.text = itemView.context.getString(R.string.following,user.following.toString())
            repoUser.text = itemView.context.getString(R.string.repo,user.publicRepo.toString())
        }

        override fun onClick(v: View?) {
            val intent = Intent(itemView.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, filtered[adapterPosition])
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_list,parent,false))
    }

    override fun getItemCount(): Int = filtered.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        filtered[position]?.let { holder.bindUser(it) }
    }

    fun searchResult(result : ArrayList<Users?>){
        filtered = result
        notifyDataSetChanged()
    }

    fun setLoad(){
        loading = false
    }
    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}