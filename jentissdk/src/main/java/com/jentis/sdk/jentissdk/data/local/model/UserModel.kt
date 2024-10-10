package com.jentis.sdk.jentissdk.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserModel(
    @PrimaryKey val id: Int = 0,
    val userId: String
)
