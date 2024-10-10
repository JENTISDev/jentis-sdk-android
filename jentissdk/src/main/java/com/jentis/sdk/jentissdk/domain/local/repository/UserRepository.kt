package com.jentis.sdk.jentissdk.domain.local.repository

import com.jentis.sdk.jentissdk.data.local.db.UserDao
import com.jentis.sdk.jentissdk.data.local.model.UserModel

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(userId: String) {
        val user = UserModel(userId = userId)
        userDao.insertUser(user)
    }

    suspend fun getUserId(): String? {
        return userDao.getUserId()
    }
}
