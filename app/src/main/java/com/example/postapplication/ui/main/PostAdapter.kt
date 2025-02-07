package com.example.postapplication.ui.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.postapplication.R
import com.example.postapplication.data.model.Post

class PostsAdapter(private val onItemClick: (Post) -> Unit) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    private val posts = mutableListOf<Post>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<Post>) {
        posts.clear()
        posts.addAll(newList)
        notifyDataSetChanged()
    }

    fun addItems(newPosts: List<Post>) {
        val startPosition = posts.size
        posts.addAll(newPosts)
        notifyItemRangeInserted(startPosition, newPosts.size)
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {
            itemView.findViewById<TextView>(R.id.textTitle).text = post.title
            itemView.findViewById<TextView>(R.id.textBody).text = post.body
            itemView.setOnClickListener { onItemClick(post) }
        }
    }


}