package data

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import composables.Anchor
import java.util.*

class BasicNode(): Node {
    override val id: UUID = UUID.randomUUID()
    override val name: String = "BasicNode"
    override var input = mutableStateOf(NodeInput(listOf("1", "2")))
    override var output = mutableStateOf(NodeOutput(listOf(1)))
    override val render =
        @Composable
    { modifier: Modifier ->
        Anchor(modifier){
            var size by remember { mutableStateOf(DpSize.Zero) }
            val density = LocalDensity.current.density
            BoxWithConstraints(Modifier.align(Alignment.Center).onGloballyPositioned {
                coordinates ->
                size = DpSize((coordinates.size.width / density).dp, (coordinates.size.height / density).dp)
            }) {
                Card(Modifier.size(100.dp).clickable{
                    println("Clicked")
                    output.value = NodeOutput(listOf(2))
                }.clip(RoundedCornerShape(15.dp))) {
                    Box(Modifier.fillMaxSize().background(Colors.currentScheme.surface).size(100.dp)) {
                        Row(Modifier.align(Alignment.TopCenter), horizontalArrangement = Arrangement.spacedBy(size.width/2)) {
                            input.value.values.forEach {
                                Text(it.toString())
                            }
                        }
                        Row(Modifier.align(Alignment.BottomCenter), horizontalArrangement = Arrangement.spacedBy(size.width/2)) {
                            output.value.values.forEach {
                                Text(it.toString())
                            }
                        }
                    }
                }
                Canvas(Modifier.offset(size.width/2, 0.dp)){
                    drawCircle(Colors.currentScheme.onBackground, radius = 15f)
                }
            }
        }
    }

}

class OutputNode(): Node{
    override val name: String = "Output"
    override var input = mutableStateOf(NodeInput(listOf()))
    override var output = mutableStateOf(NodeOutput(listOf(false)))
    override val render =
        @Composable
        { modifier: Modifier ->
            Anchor(modifier){
                var size by remember { mutableStateOf(DpSize.Zero) }
                val density = LocalDensity.current.density
                BoxWithConstraints(Modifier.align(Alignment.Center).onGloballyPositioned {
                        coordinates ->
                    size = DpSize((coordinates.size.width / density).dp, (coordinates.size.height / density).dp)
                }) {
                    Card(Modifier.size(100.dp, 25.dp).clickable{
                        println("Clicked")
                        output.value = NodeOutput(listOf(output.value.values[0]))
                    }.clip(RoundedCornerShape(15.dp))) {
                        Box(Modifier.fillMaxSize().background(Colors.currentScheme.surface).size(100.dp)) {
                            Row(Modifier.align(Alignment.TopCenter), horizontalArrangement = Arrangement.spacedBy(size.width/2)) {
                                input.value.values.forEach {
                                    Text(it.toString())
                                }
                            }
                            Row(Modifier.align(Alignment.BottomCenter), horizontalArrangement = Arrangement.spacedBy(size.width/2)) {
                                output.value.values.forEach {
                                    Text(it.toString())
                                }
                            }
                        }
                    }
                    Canvas(Modifier.offset(size.width/2, size.height)){
                        drawCircle(Colors.currentScheme.onBackground, radius = 15f)
                    }
                }
            }
        }

}


@Composable
fun Dp.toPx(): Float {
    val dpValue = this
    val density = LocalDensity.current
    return with(density) { dpValue.toPx() }
}

@Composable
fun Float.toDp(): Dp {
    val pxValue = this
    val density = LocalDensity.current
    return with(density) { pxValue.toDp() }
}

interface Node{
    val id: UUID
        get() = UUID.randomUUID()
    val name: String
    var input: MutableState<NodeInput>
    var output: MutableState<NodeOutput>
    val render: @Composable (Modifier) -> Unit
}


data class NodeInput(
    val values: List<Any>
){

}

data class NodeOutput(
    val values: List<Any>
){

}