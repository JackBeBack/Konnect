package composables

import androidx.compose.runtime.DisposableEffect
import com.apple.eawt.event.GestureUtilities
import com.apple.eawt.event.MagnificationEvent
import com.apple.eawt.event.MagnificationListener
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.window.FrameWindowScope
import kotlinx.coroutines.*
import javax.swing.JComponent
import javax.swing.JRootPane
import kotlin.coroutines.CoroutineContext
import kotlin.math.abs

fun FrameWindowScope.enableMultiTouch() {
    window.rootPane.enableMultiTouch()
}

fun JRootPane.enableMultiTouch() {
    MultiTouchListener.enable(this)
}

object MultiTouchListener : MagnificationListener, CoroutineScope {

    private var registered = false

    private var inertiaJob: Job? = null

    private const val dampening = 0.94

    fun enable(component: JComponent) {
        if (!registered) {
            GestureUtilities.addGestureListenerTo(component, this)
            registered = true
        }
    }

    private val listeners: MutableList<(Double) -> Unit> = mutableListOf()

    override fun magnify(e: MagnificationEvent) {
        magnify(e.magnification)
        inertiaJob?.cancel()
        inertiaJob = scheduleIntertiaJob(e.magnification)
    }

    fun magnify(magnification: Double) {
        listeners.forEach {
            it(magnification)
        }
    }

    fun scheduleIntertiaJob(magnification: Double): Job {
        var currentMagnification = magnification * dampening
        return launch {
            while (abs(currentMagnification) > 0.001) {
                delay(10)
                magnify(currentMagnification)
                currentMagnification *= dampening
            }
        }
    }

    fun addListener(listener: (Double) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (Double) -> Unit) {
        listeners.remove(listener)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default
}

fun Modifier.zoomInput(block: (magnification: Double) -> Unit): Modifier = composed {
    DisposableEffect(Unit) {
        MultiTouchListener.addListener(block)
        onDispose {
            MultiTouchListener.removeListener(block)
        }
    }
    this
}