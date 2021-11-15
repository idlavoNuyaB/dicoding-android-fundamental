package com.example.githubuser

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_row_user.view.*

class UserListAdapter(private val user: ArrayList<User>) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v){
        private var users: User? = null
        private val view = v
        private val avatarUser = v.coveruser
        private val nameUser = v.name
        private val followingUser = v.following
        private val followersUser = v.followers
        private val parentLayout = v.parentLayout

        fun bindUser(user : User){
            this.users = user
            Glide.with(itemView.context).load(user.avatar).into(avatarUser)
            nameUser.text = user.name
            followingUser.text = user.following?.let { Integer.toString(it) } + " Following"
            followersUser.text = user.followers?.let { Integer.toString(it) } + " Followers"
            parentLayout.setOnClickListener{
                val intent = Intent(view.context,DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER,users)
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                view.context.startActivity(intent)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_row_user,parent,false))
    }

    override fun getItemCount(): Int = user.size

    override fun onBindViewHolder(holder: UserListAdapter.ViewHolder, position: Int) {
        holder.bindUser(user[position])
    }

}