package fyi.atom.lifesuite.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import fyi.atom.lifesuite.api.clickup.FetchTasksInViewUseCase
import fyi.atom.lifesuite.ui.theme.WidgetTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActiveTasksWidget : GlanceAppWidget() {

    @[EntryPoint InstallIn(SingletonComponent::class)]
    interface WidgetEntryPoint {
        fun fetchTasksInViewUseCase(): FetchTasksInViewUseCase
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext ?: throw IllegalStateException()
        val widgetEntryPoint = EntryPointAccessors.fromApplication(appContext, WidgetEntryPoint::class.java)
        val tasks = withContext(Dispatchers.Default) {
            widgetEntryPoint.fetchTasksInViewUseCase().invoke().tasks?.map { TaskHod(it.name, it.id.orEmpty()) }
        }

        provideContent {
            WidgetTheme {
                ActiveTasks(tasks.orEmpty())
            }
        }
    }
}
