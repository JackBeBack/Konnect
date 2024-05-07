import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
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
import data.di.platformModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import viewmodels.DesktopSettings

fun main() = application {
    startKoin {
        modules(platformModule)
    }
    Window(onCloseRequest = ::exitApplication, state = rememberWindowState(size = DpSize(1280.dp, 720.dp))) {
        enableMultiTouch()
        Box(Modifier.fillMaxSize()) {
            Desktop(Modifier.fillMaxSize())
            Row(modifier = Modifier.padding(8.dp).align(Alignment.TopStart),
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DesktopTransformButton()
            }

        }
    }
}
