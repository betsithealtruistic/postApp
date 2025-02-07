package com.example.postapplication.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.postapplication.R
import com.example.postapplication.data.api.ApiClient
import com.example.postapplication.data.db.PostDatabase
import com.example.postapplication.data.repository.PostRepository
import com.example.postapplication.data.repository.PostViewModelFactory
import com.example.postapplication.ui.add.AddPostActivity
import com.example.postapplication.ui.details.PostDetailActivity
import com.example.postapplication.utils.DarkModeManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostsAdapter
    private lateinit var fabAddPost: FloatingActionButton
    private lateinit var toggleDarkMode: ImageView
    private var REQUEST_ADD_POST= 1001
    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(
            PostRepository(
                ApiClient.apiService,
                PostDatabase.getDatabase(this).postDao()
            )
        )
    }

    private val addPostLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.fetchAllPosts()
        }
    }

    private var isLoading = false
    private var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerViewPosts)
        fabAddPost = findViewById(R.id.fabAddPost)
        toggleDarkMode = findViewById(R.id.toggleDarkMode)
        adapter = PostsAdapter { post ->
            val intent = Intent(this, PostDetailActivity::class.java).apply {
                putExtra("POST_ID", post.id)
            }
            startActivity(intent)
        }
        setupObservers()
        setupRecyclerViewPagination()
        updateToggleIcon()
        viewModel.fetchAllPosts()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter



        fabAddPost.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            addPostLauncher.launch(intent)         }

        toggleDarkMode.setOnClickListener {
            DarkModeManager.toggleDarkMode(this)
            updateToggleIcon()
        }

    }

    private fun setupObservers() {
        viewModel.allPosts.observe(this) { posts ->
            Log.d("MainActivity", "Total Posts: ${posts!!.size}")
            adapter.updateList(posts)
        }

        viewModel.paginatedPosts.observe(this) { newPosts ->
            if (newPosts.isNotEmpty()) {
                adapter.addItems(newPosts)
                isLoading = false
            } else {
                isLastPage = true
            }
        }
    }


    private fun setupRecyclerViewPagination() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && !isLastPage &&
                    (visibleItemCount + firstVisibleItemPosition >= totalItemCount) &&
                    firstVisibleItemPosition >= 0) {

                    isLoading = true
                    viewModel.fetchPaginatedPosts(this@MainActivity)
                }
            }
        })
    }

    private fun updateToggleIcon() {
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        toggleDarkMode.setImageResource(
            if (isDarkMode) R.drawable.baseline_toggle_on_24 else R.drawable.baseline_toggle_off_24
        )
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_POST && resultCode == RESULT_OK) {
            viewModel.fetchAllPosts()

        }
    }



}