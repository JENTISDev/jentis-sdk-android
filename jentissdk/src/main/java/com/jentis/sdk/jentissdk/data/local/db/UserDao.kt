package com.jentis.sdk.jentissdk.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jentis.sdk.jentissdk.data.local.model.UserModel

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserModel)

    @Query("SELECT userId FROM user_table WHERE id = 0 LIMIT 1")
    suspend fun getUserId(): String?
}
