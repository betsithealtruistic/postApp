package com.example.postapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "posts")
data class Post(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerializedName("title")
    val title: String,

    @SerializedName("body")
    val body: String
)
