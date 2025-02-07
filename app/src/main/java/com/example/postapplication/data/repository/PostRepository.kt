package com.example.postapplication.data.repository


import android.content.Context
import com.example.postapplication.data.api.ApiService
import com.example.postapplication.data.db.PostDao
import com.example.postapplication.data.model.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PostRepository(private val apiService: ApiService, private val postDao: PostDao) {


    suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getAllPosts()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getPaginatedPosts(page: Int, pageSize: Int): List<Post> = withContext(Dispatchers.IO) {
        return@withContext try {
            postDao.getPaginatedPosts(page, pageSize)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun refreshPostsFromApi(context: Context) = withContext(Dispatchers.IO) {
        try {
            val posts = apiService.getAllPosts()
            postDao.clearAllPosts()
            postDao.insertPosts(posts)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getPostDetail(postId: Int): Post? = withContext(Dispatchers.IO) {
        return@withContext try {
            apiService.getPostDetail(postId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createPost(post: Post): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.createPost(post)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun insertPostsToDb(posts: List<Post>) {
        postDao.insertPosts(posts)
    }

    suspend fun deletePost(postId: Int): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            val response = apiService.deletePost(postId)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun deletePostFromDb(postId: Int) = withContext(Dispatchers.IO) {
        try {
            postDao.deletePostById(postId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}