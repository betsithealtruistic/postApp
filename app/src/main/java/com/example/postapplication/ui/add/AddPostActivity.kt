package com.example.postapplication.ui.add

import android.media.Image
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.postapplication.R
import com.example.postapplication.data.api.ApiClient
import com.example.postapplication.data.db.PostDatabase
import com.example.postapplication.data.model.Post
import com.example.postapplication.data.repository.PostRepository
import com.example.postapplication.data.repository.PostViewModelFactory
import com.example.postapplication.ui.main.PostViewModel

class AddPostActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etBody: EditText
    private lateinit var btnSubmit: Button

    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(
            PostRepository(
                ApiClient.apiService,
                PostDatabase.getDatabase(this).postDao()
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_post)
        etTitle = findViewById(R.id.textTitle)
        etBody = findViewById(R.id.textBody)
        btnSubmit = findViewById(R.id.btnSubmit)

        setupListeners()


    }

    private fun setupListeners() {
        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val body = etBody.text.toString().trim()

            if (validateInput(title, body)) {
                createNewPost(title, body)
            }
        }
    }

    private fun validateInput(title: String, body: String): Boolean {
        return if (title.isEmpty() || body.isEmpty()) {
            showToast("Fields cannot be empty")
            false
        } else {
            true
        }
    }

    private fun createNewPost(title: String, body: String) {
        val newPost = Post(id = 0, title = title, body = body)
        viewModel.createPost(newPost)
        showToast("Post Created!")
        setResult(RESULT_OK)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }



}