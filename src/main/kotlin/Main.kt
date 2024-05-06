import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import composables.Desktop
import composables.DesktopTransformButton
import composables.enableMultiTouch

fun main() = application {
    Window(onCloseRequest = ::exitApplication, state = rememberWindowState(size = DpSize(1280.dp, 720.dp))) {
        enableMultiTouch()
        Box(Modifier.fillMaxSize()) {
            Desktop(Modifier.fillMaxSize())
            DesktopTransformButton(Modifier.padding(8.dp).align(Alignment.TopStart))
        }
    }
}
