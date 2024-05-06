package composables

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import data.Typography

@Composable
fun smallText(text: String) {
    Text(text, fontSize = Typography.smallText.fontSize)
}