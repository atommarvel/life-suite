package fyi.atom.lifesuite.api.clickup

import fyi.atom.lifesuite.model.server.clickup.TasksResponse
import io.ktor.client.call.body
import javax.inject.Inject

/**
 * https://clickup.com/api/clickupreference/operation/GetViewTasks/
 */
class FetchTasksInViewUseCase
    @Inject
    constructor(
        private val client: ClickUpHttpClient
    ) {
        suspend operator fun invoke(viewId: String = ACTIVE_TASKS_VIEW_ID): TasksResponse =
            client
                .get("view/$viewId/task") {
                    url {
                        // TODO: pagination
                        parameters.append("page", "0")
                    }
                }.body()

        companion object {
            private const val ACTIVE_TASKS_VIEW_ID = "a45kv-514"
        }
    }
