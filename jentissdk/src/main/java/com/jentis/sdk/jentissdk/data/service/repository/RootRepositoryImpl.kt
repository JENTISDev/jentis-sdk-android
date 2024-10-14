import com.jentis.sdk.jentissdk.data.service.ApiService
import com.jentis.sdk.jentissdk.data.service.model.Root
import com.jentis.sdk.jentissdk.data.service.repository.RootRepository

class RootRepositoryImpl(private val apiService: ApiService) : RootRepository {
    override suspend fun sendRootData(root: Root): Result<Unit> {
        return try {
            val response = apiService.sendRootData(root)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Throwable(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
