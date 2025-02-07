package com.example.postapplication.ui.details

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer

import com.example.postapplication.R
import com.example.postapplication.data.api.ApiClient
import com.example.postapplication.data.db.PostDatabase
import com.example.postapplication.data.model.Post
import com.example.postapplication.data.repository.PostRepository
import com.example.postapplication.data.repository.PostViewModelFactory
import com.example.postapplication.ui.main.PostViewModel

class PostDetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var bodyTextView: TextView
    private lateinit var btnDelete: Button


    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(PostRepository(ApiClient.apiService, PostDatabase.getDatabase(this).postDao()))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_post_detail)

        titleTextView = findViewById(R.id.textTitleDetail)
        bodyTextView = findViewById(R.id.textBodyDetail)
        btnDelete = findViewById(R.id.btnDelete)

        val postId = intent.getIntExtra("POST_ID", -1)
        if (postId == -1) {
            finish()
            return
        }

        viewModel.getPostDetail(postId)

        viewModel.postDetails.observe(this, Observer { post ->
            post?.let { updateUI(it) }
        })


    }
    private fun updateUI(post: Post) {
        titleTextView.text = post.title
        bodyTextView.text = post.body

        btnDelete.setOnClickListener {
            viewModel.deletePost(post.id)
            showToast("Post Deleted!")
            finish()
        }
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}