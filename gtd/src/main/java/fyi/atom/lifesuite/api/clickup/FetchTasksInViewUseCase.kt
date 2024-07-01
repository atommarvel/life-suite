package fyi.atom.lifesuite.api.clickup

import fyi.atom.lifesuite.model.server.clickup.TasksResponse
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import timber.log.Timber
import javax.inject.Inject

/**
 * https://clickup.com/api/clickupreference/operation/GetViewTasks/
 */
class FetchTasksInViewUseCase @Inject constructor(
    private val client: ClickUpHttpClient
) {
    suspend operator fun invoke(viewId: String): TasksResponse {
        val response: HttpResponse = client.get("view/${viewId}/task") {
            url {
                // TODO: pagination
                parameters.append("page", "0")
            }
        }
        // TODO: httpclient logging
        Timber.d(response.bodyAsText())
        return response.body()
    }
}