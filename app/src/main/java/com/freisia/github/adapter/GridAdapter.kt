package com.freisia.github.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.github.detail.DetailActivity
import com.freisia.github.R
import com.freisia.github.api.model.Users
import kotlinx.android.synthetic.main.layout_item_grid.view.*

class GridAdapter(user: ArrayList<Users?>, recyclerView: RecyclerView) : RecyclerView.Adapter<GridAdapter.ViewHolder>() {
    private var filtered : ArrayList<Users?> = ArrayList()
    private var loading: Boolean = false
    lateinit var onLoadMoreListener: OnLoadMoreListener
    init {
        filtered = user
        if(recyclerView.layoutManager is GridLayoutManager){
            val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = linearLayoutManager.itemCount
                    val lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if(!loading && totalItemCount - 1 <= lastVisible && lastVisible > filtered.size - 2){
                        onLoadMoreListener.onGridLoadMore()
                        loading = true
                    }
                }
            })
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_item_grid,parent,false))
    }

    override fun getItemCount(): Int = filtered.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        filtered[position]?.let { holder.bindUser(it) }
    }
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener{
        private val avatar = v.imageView
        private val name = v.nameGrid
        init {
            v.setOnClickListener(this)
        }
        fun bindUser(user : Users) {
            Glide.with(itemView.context).load(user.avatarUrl).error(R.mipmap.ic_launcher_round).into(avatar)
            name.text = user.login
        }
            override fun onClick(v: View?) {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER,filtered[adapterPosition])
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                itemView.context.startActivity(intent)
            }
    }
    fun searchResult(result : ArrayList<Users?>){
        filtered = result
        notifyDataSetChanged()
    }

    fun setLoad(){
        loading = false
    }
    interface OnLoadMoreListener {
        fun onGridLoadMore()
    }
}