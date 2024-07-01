package fyi.atom.lifesuite.api.clickup

import fyi.atom.lifesuite.auth.ClickUpAuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.HttpResponse
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.flow.first

class ClickUpHttpClient(
    private val client: HttpClient,
    private val clickUpAuthRepository: ClickUpAuthRepository
) {
    suspend fun get(
        endpoint: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse {
        // TODO: decide on how to deal with lack of access token
        val token = clickUpAuthRepository.authState.first()?.accessToken.orEmpty()
        return client.get(BASE_URL) {
            url {
                appendPathSegments(endpoint)
            }
            headers {
                append("Authorization", "Bearer $token")
            }
            block()
        }
    }

    companion object {
        private const val BASE_URL = "https://api.clickup.com/api/v2/"
    }
}