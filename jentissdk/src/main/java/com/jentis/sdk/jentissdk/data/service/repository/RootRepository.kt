package com.jentis.sdk.jentissdk.data.service.repository

import com.jentis.sdk.jentissdk.data.service.model.Root

interface RootRepository {
    suspend fun sendRootData(root: Root): Result<Unit>
}
