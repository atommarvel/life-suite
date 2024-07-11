package fyi.atom.lifesuite.widget

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.appwidget.lazy.items
import androidx.glance.background
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextStyle

@Composable
fun ActiveTasks(tasks: List<TaskHod>) {
    LazyColumn(
        modifier =
            GlanceModifier
                .padding(4.dp)
                .background(GlanceTheme.colors.surface)
                .fillMaxSize()
    ) {
        items(tasks, { LazyListScope.UnspecifiedItemId }) { task ->
            TaskRow(task)
        }
    }
}

data class TaskHod(
    val name: String,
    val id: String
)

@Composable
fun TaskRow(
    hod: TaskHod,
    modifier: GlanceModifier = GlanceModifier
) {
    Text(
        text = hod.name,
        style = TextStyle(color = GlanceTheme.colors.onSurface),
        modifier =
            modifier
                .padding(8.dp)
                .clickable(actionStartActivity(taskIntent(hod.id)))
                .fillMaxWidth()
    )
}

private fun taskIntent(id: String): Intent =
    Intent(Intent.ACTION_VIEW).apply {
        data = "https://app.clickup.com/t/$id".toUri()
    }
