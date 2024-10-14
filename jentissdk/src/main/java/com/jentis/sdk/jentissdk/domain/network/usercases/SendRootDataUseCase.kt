package com.jentis.sdk.jentissdk.domain.network.usercases

import com.jentis.sdk.jentissdk.data.service.model.Root
import com.jentis.sdk.jentissdk.data.service.repository.RootRepository

class SendRootDataUseCase(private val repository: RootRepository) {
    suspend fun execute(root: Root): Result<Unit> {
        return repository.sendRootData(root)
    }
}