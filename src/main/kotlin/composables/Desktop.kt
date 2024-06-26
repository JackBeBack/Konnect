package composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import data.*
import org.koin.core.context.KoinContext
import org.koin.java.KoinJavaComponent.inject
import viewmodels.DesktopSettings
import viewmodels.GlobalStateProvider
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.random.Random

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Desktop(modifier: Modifier) {
    val density = LocalDensity.current.density
    var transform by remember { mutableStateOf(Transform(density = density)) }
    val desktopSettings: DesktopSettings by inject()

    val node = remember { OutputNode() }

    LaunchedEffect(transform) {
        GlobalStateProvider.desktopTransform.emit(transform)
    }

    Box(modifier = modifier.onPointerEvent(PointerEventType.Scroll) {
        val change = it.changes.first()
        val speed = 5f
        val scrollX = change.scrollDelta.x
        val scrollY = change.scrollDelta.y

        transform =
            transform.copy(offset = transform.offset + Offset(-scrollX, -scrollY) * speed / transform.scale)
    }.zoomInput { magnification ->
        val zoomSensitivity = 0.2f
        val zoom = if (magnification > 0) {
            1 + abs(magnification.toFloat() * zoomSensitivity)
        } else {
            1 - abs(magnification.toFloat() * zoomSensitivity)
        }
        transform = transform.copy(scale = transform.scale * zoom)
    }) {
        Canvas(modifier = Modifier.fillMaxSize().background(Colors.currentScheme.background)) {
            drawDots(transform)
        }
        node.render(Modifier.align(Alignment.Center))
    }
}

inline fun <reified T> inject(): Lazy<T> {
    return inject(T::class.java)
}

fun DrawScope.drawDotsWithScale(transform: Transform) {
    val stepSize = 100f  // Make this a float for later
    val lineColor = Color.White
    val strokeWidth = 1.5f

    // Calculate visible bounds based on current viewport and transform
    val left = (0f - transform.offset.x) / (transform.scale * stepSize)
    val bottom = (0f - transform.offset.y) / (transform.scale * stepSize)
    // Calculate starting points by aligning to grid and using Ints for efficiency
    val startX = (left.toInt() * stepSize * transform.scale) + transform.offset.x
    val startY = (bottom.toInt() * stepSize * transform.scale) + transform.offset.y

    // Draw grid of dots
    for (x in startX.toInt() until size.width.toInt() step (stepSize * transform.scale).toInt()) {
        for (y in startY.toInt() until size.height.toInt() step (stepSize * transform.scale).toInt()) {
            drawCircle(
                color = lineColor, center = Offset(x.toFloat(), y.toFloat()), radius = strokeWidth
            )
        }
    }
}

fun DrawScope.drawDots(transform: Transform) {
    val stepSize = 100f  // Make this a float for later
    val lineColor = Color.White
    val strokeWidth = 1.5f

    // Calculate visible bounds based on current viewport and transform
    val left = (0f - transform.offset.x) / (stepSize)
    val bottom = (0f - transform.offset.y) / (stepSize)
    // Calculate starting points by aligning to grid and using Ints for efficiency
    val startX = (left.toInt() * stepSize) + transform.offset.x
    val startY = (bottom.toInt() * stepSize) + transform.offset.y

    // Draw grid of dots
    for (x in startX.toInt() until size.width.toInt() step (stepSize).toInt()) {
        for (y in startY.toInt() until size.height.toInt() step (stepSize).toInt()) {
            drawCircle(
                color = lineColor, center = Offset(x.toFloat(), y.toFloat()), radius = strokeWidth
            )
        }
    }
}



