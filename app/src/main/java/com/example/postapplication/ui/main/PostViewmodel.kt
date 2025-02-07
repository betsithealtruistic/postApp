package com.example.postapplication.ui.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postapplication.data.model.Post
import com.example.postapplication.data.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _allPosts = MutableLiveData<List<Post>?>()
    val allPosts: LiveData<List<Post>?> get() = _allPosts

    private val _paginatedPosts = MutableLiveData<List<Post>>()
    val paginatedPosts: LiveData<List<Post>> get() = _paginatedPosts

    private val _postDetails = MutableLiveData<Post?>()
    val postDetails: LiveData<Post?> get() = _postDetails

    private val _isPostCreated = MutableLiveData<Boolean>()
    val isPostCreated: LiveData<Boolean> get() = _isPostCreated

    private val _isPostDeleted = MutableLiveData<Boolean>()
    val isPostDeleted: LiveData<Boolean> get() = _isPostDeleted

    private var currentPage = 1
    private val pageSize = 10
    private var isFetching = false


    fun fetchAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val posts = repository.getAllPosts()
                repository.insertPostsToDb(posts)

                withContext(Dispatchers.Main) {
                    _allPosts.postValue(posts)
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching all posts", e)
            }
        }
    }



    fun fetchPaginatedPosts(context: Context) {
        if (isFetching) return
        isFetching = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newPosts = repository.getPaginatedPosts(currentPage, pageSize)
                withContext(Dispatchers.Main) {
                    _paginatedPosts.postValue(newPosts)
                }
                currentPage++
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching paginated posts", e)
            } finally {
                isFetching = false
            }
        }
    }


    fun refreshPosts(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.refreshPostsFromApi(context)
                fetchPaginatedPosts(context)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error refreshing posts", e)
            }
        }
    }


    fun getPostDetail(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val post = repository.getPostDetail(postId)
                withContext(Dispatchers.Main) {
                    _postDetails.postValue(post)
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error fetching post details", e)
            }
        }
    }


    fun createPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.createPost(post) // Returns Boolean

                if (success) {
                    val refreshedPosts = repository.getAllPosts() // Fetch latest posts

                    withContext(Dispatchers.Main) {
                        _allPosts.postValue(refreshedPosts)
                        _isPostCreated.postValue(true)
                    }
                } else {
                    _isPostCreated.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error creating post", e)
                _isPostCreated.postValue(false)
            }
        }
    }

    fun deletePost(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val success = repository.deletePost(postId) // API Call
                if (success) {
                    repository.deletePostFromDb(postId) // Delete locally

                    withContext(Dispatchers.Main) {
                        val updatedList = _allPosts.value?.filter { it.id != postId }
                        _allPosts.postValue(updatedList)
                        _isPostDeleted.postValue(true)
                    }
                } else {
                    _isPostDeleted.postValue(false)
                }
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error deleting post", e)
                _isPostDeleted.postValue(false)
            }
        }
    }
}
