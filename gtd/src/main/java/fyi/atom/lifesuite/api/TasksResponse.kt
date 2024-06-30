package fyi.atom.lifesuite.api


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import androidx.annotation.Keep

@Keep
@Serializable
data class TasksResponse(
    @SerialName("last_page")
    val lastPage: Boolean? = null,
    val tasks: List<Task>? = null
)

// TODO: commented out fields
@Keep
@Serializable
data class Task(
    val archived: Boolean? = null,
    val assignees: List<Assignee>? = null,
//    val checklists: List<Any?>? = null,
    val creator: Creator? = null,
//    @SerialName("custom_fields")
//    val customFields: List<Any?>? = null,
//    @SerialName("custom_id")
//    val customId: Any? = null,
    @SerialName("custom_item_id")
    val customItemId: Int? = null,
    @SerialName("date_closed")
    val dateClosed: String? = null,
    @SerialName("date_created")
    val dateCreated: String? = null,
    @SerialName("date_done")
    val dateDone: String? = null,
    @SerialName("date_updated")
    val dateUpdated: String? = null,
//    val dependencies: List<Any?>? = null,
    @SerialName("due_date")
    val dueDate: String? = null,
    val folder: Folder? = null,
//    @SerialName("group_assignees")
//    val groupAssignees: List<Any?>? = null,
    val id: String? = null,
    // TODO
//    @SerialName("linked_tasks")
//    val linkedTasks: List<Any?>? = null,
    val list: ListInfo? = null,
    val locations: List<Location?>? = null,
    val name: String,
    val orderindex: String? = null,
//    val parent: Any? = null,
    @SerialName("permission_level")
    val permissionLevel: String? = null,
//    val points: Any? = null,
    val priority: Priority? = null,
    val project: Project? = null,
    val sharing: Sharing? = null,
    val space: Space? = null,
    @SerialName("start_date")
    val startDate: String? = null,
    val status: Status? = null,
    @SerialName("subtasks_count")
    val subtasksCount: Int? = null,
    val tags: List<Tag?>? = null,
    @SerialName("team_id")
    val teamId: String? = null,
//    @SerialName("time_estimate")
//    val timeEstimate: Any? = null,
    val url: String? = null,
//    val watchers: List<Any?>? = null
) {
    @Keep
    @Serializable
    data class Assignee(
        val color: String? = null,
        val email: String? = null,
        val id: Int? = null,
        val initials: String? = null,
        val profilePicture: String? = null,
        val username: String? = null
    )

    @Keep
    @Serializable
    data class Creator(
        val color: String? = null,
        val email: String? = null,
        val id: Int? = null,
        val profilePicture: String? = null,
        val username: String? = null
    )

    @Keep
    @Serializable
    data class Folder(
        val access: Boolean? = null,
        val hidden: Boolean? = null,
        val id: String? = null,
        val name: String? = null
    )

    @Keep
    @Serializable
    data class ListInfo(
        val access: Boolean? = null,
        val id: String? = null,
        val name: String? = null
    )

    @Keep
    @Serializable
    data class Location(
        val id: String? = null,
        val name: String? = null
    )

    @Keep
    @Serializable
    data class Priority(
        val color: String? = null,
        val id: String? = null,
        val orderindex: String? = null,
        val priority: String? = null
    )

    @Keep
    @Serializable
    data class Project(
        val access: Boolean? = null,
        val hidden: Boolean? = null,
        val id: String? = null,
        val name: String? = null
    )

    @Keep
    @Serializable
    data class Sharing(
        val `public`: Boolean? = null,
        @SerialName("public_fields")
        val publicFields: List<String?>? = null,
        @SerialName("public_share_expires_on")
        val publicShareExpiresOn: String? = null,
        @SerialName("seo_optimized")
        val seoOptimized: Boolean? = null,
        val token: String? = null
    )

    @Keep
    @Serializable
    data class Space(
        val id: String? = null
    )

    @Keep
    @Serializable
    data class Status(
        val color: String? = null,
        val id: String? = null,
        val orderindex: Int? = null,
        val status: String? = null,
        val type: String? = null
    )

    @Keep
    @Serializable
    data class Tag(
        val creator: Int? = null,
        val name: String? = null,
        @SerialName("tag_bg")
        val tagBg: String? = null,
        @SerialName("tag_fg")
        val tagFg: String? = null
    )
}
