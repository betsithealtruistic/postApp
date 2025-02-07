package com.example.postapplication.data.api

import com.example.postapplication.data.model.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getAllPosts(): List<Post>

    @GET("posts/{id}")
    suspend fun getPostDetail(@Path("id") postId: Int): Post

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: Int): Response<Unit>
}