import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.glance.layout.Column
import fyi.atom.lifesuite.model.server.clickup.Task

enum class PrioLevel {
    URGENT, HIGH, NORMAL, LOW, NONE;

    companion object {
        operator fun invoke(priority: Task.Priority?): PrioLevel {
            return when(priority?.priority) {
                "urgent" -> URGENT
                "high" -> HIGH
                "normal" -> NORMAL
                "low" -> LOW
                else -> NONE
            }
        }
    }
}

@Composable
fun PrioFlagIcon(
    level: PrioLevel,
    modifier: Modifier = Modifier
) {
    val imageVector = remember(level) { level.imageVector }
    Image(imageVector, null, modifier = modifier)
}

@Preview
@Composable
private fun Preview() {
    Column {
        PrioFlagIcon(PrioLevel.URGENT)
    }
}

val PrioLevel.imageVector: ImageVector
    get() = when (this) {
        PrioLevel.URGENT -> UrgentPrioFlagImageVector
        PrioLevel.HIGH -> HighPrioFlagImageVector
        PrioLevel.NORMAL -> NormalPrioFlagImageVector
        PrioLevel.LOW -> LowPrioFlagImageVector
        PrioLevel.NONE -> ImageVector.Builder(
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).build()
    }

private val UrgentPrioFlagImageVector: ImageVector by lazy { prioFlag(Color.Red) }
private val HighPrioFlagImageVector: ImageVector by lazy { prioFlag(Color.Yellow) }
private val NormalPrioFlagImageVector: ImageVector by lazy { prioFlag(Color.Blue) }
private val LowPrioFlagImageVector: ImageVector by lazy { prioFlag(Color.Gray) }

/**
 * Provided by https://www.composables.com/vector-drawable-to-compose
 */
private fun prioFlag(color: Color): ImageVector =
    ImageVector.Builder(
        name = "PrioFlag",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFFFFF)),
            fillAlpha = 1.0f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(5f, 21f)
            lineTo(5f, 4f)
            lineTo(14f, 4f)
            lineTo(14.4f, 6f)
            lineTo(20f, 6f)
            lineTo(20f, 16f)
            lineTo(13f, 16f)
            lineTo(12.6f, 14f)
            lineTo(7f, 14f)
            lineTo(7f, 21f)
            lineTo(5f, 21f)
            close()
            moveTo(14.65f, 14f)
            lineTo(18f, 14f)
            lineTo(18f, 8f)
            lineTo(12.75f, 8f)
            lineTo(12.35f, 6f)
            lineTo(7f, 6f)
            lineTo(7f, 12f)
            lineTo(14.25f, 12f)
            lineTo(14.65f, 14f)
            close()
        }
        path(
            fill = SolidColor(color),
            fillAlpha = 1.0f,
            stroke = null,
            strokeAlpha = 1.0f,
            strokeLineWidth = 1.0f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Miter,
            strokeLineMiter = 1.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(14.65f, 14f)
            lineTo(18f, 14f)
            lineTo(18f, 8f)
            lineTo(12.75f, 8f)
            lineTo(12.35f, 6f)
            lineTo(7f, 6f)
            lineTo(7f, 12f)
            lineTo(14.25f, 12f)
            lineTo(14.65f, 14f)
            close()
        }
    }.build()

