package fyi.atom.lifesuite.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.LazyListScope
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.color.ColorProviders
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.glance.text.TextDefaults
import androidx.glance.text.TextStyle
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
            widgetEntryPoint.fetchTasksInViewUseCase().invoke().tasks?.map { it.name }
        }

        provideContent {
            WidgetTheme {
                LazyColumn(
                    modifier = GlanceModifier
                        .padding(4.dp)
                        .background(GlanceTheme.colors.primary)
                        .fillMaxSize()
                ) {
                    items(tasks.orEmpty(), { LazyListScope.UnspecifiedItemId }) { task ->
                        Text(
                            text = task,
                            style = TextStyle(color = GlanceTheme.colors.onPrimary)
                        )
                    }
                }
            }
        }
    }
}
