package com.jentis.sdk.jentissdk.domain.local.usercase

import com.jentis.sdk.jentissdk.domain.local.repository.UserRepository

class GetUserIdUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): String? {
        return userRepository.getUserId()
    }
}
