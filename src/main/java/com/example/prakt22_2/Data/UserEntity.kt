package com.example.prakt22_2.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey val username: String,
    val password: String
)


