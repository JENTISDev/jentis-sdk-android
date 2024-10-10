package com.jentis.sdk.jentissdk.domain.local.usercase

import com.jentis.sdk.jentissdk.domain.local.repository.UserRepository

class SaveUserIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String) {
        userRepository.insertUser(userId)
    }
}
