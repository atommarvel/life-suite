package fyi.atom.lifesuite.api

import fyi.atom.lifesuite.auth.ClickUpAuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

/**
 * https://clickup.com/api/clickupreference/operation/GetViewTasks/
 */
class FetchTasksInViewUseCase @Inject constructor(
    private val clickUpAuthRepository: ClickUpAuthRepository
) {
    suspend fun invoke(viewId: String): TasksResponse {
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
        val token = clickUpAuthRepository.authState.first()?.accessToken.orEmpty()
        val response: HttpResponse = client.get("https://api.clickup.com/api/v2/view/${viewId}/task") {
            headers {
                append("Authorization", "Bearer $token")
            }
            url {
                parameters.append("page", "0")
            }
        }
        Timber.d(response.bodyAsText())
        return response.body()
    }
}
/*




 */