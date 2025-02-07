package com.example.postapplication.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.postapplication.data.model.Post


@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY id DESC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts ORDER BY id DESC LIMIT :pageSize OFFSET (:page - 1) * :pageSize")
    suspend fun getPaginatedPosts(page: Int, pageSize: Int): List<Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>) // Batch insert for efficiency

    @Delete
    suspend fun deletePost(post: Post)

    @Query("DELETE FROM posts WHERE id = :postId")
    suspend fun deletePostById(postId: Int)

    @Query("DELETE FROM posts")
    suspend fun clearAllPosts()
}
