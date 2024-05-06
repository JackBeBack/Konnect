package composables

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.Colors
import data.Typography
import viewmodels.GlobalStateProvider

@Composable
fun toggleContentButton(modifier: Modifier, closed: @Composable () -> Unit, opened: @Composable () -> Unit) {
    val isOpen = remember { mutableStateOf(false) }
    Card(
        modifier = modifier.animateContentSize().clickable { isOpen.value = !isOpen.value }
            .background(Colors.currentScheme.background),
        shape = RoundedCornerShape(5.dp)
    ) {
        Box(Modifier.padding(2.dp)) {
            if (isOpen.value) {
                opened()
            } else {
                closed()
            }
        }
    }
}

@Composable
fun DesktopTransformButton(modifier: Modifier) {
    val transform = GlobalStateProvider.desktopTransform.collectAsState()
    toggleContentButton(
        modifier = modifier,
        closed = {
            smallText("DesktopTransform")
        },
        opened = {
            Row {
                smallText(transform.value.offset.toString())
                Spacer(Modifier.size(8.dp))
                smallText(transform.value.scale.toString())
                Spacer(Modifier.size(8.dp))
                smallText(transform.value.density.toString())
            }
        }
    )
}