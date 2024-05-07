package data

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import composables.Anchor

class BasicNode(): Node {
    override val id: String = "This is my ID"
    override val name: String = "BasicNode"
    override var input = mutableStateOf(NodeInput(listOf("input1", "input2")))
    override var output = mutableStateOf(NodeOutput(listOf(1)))
    override val render =
        @Composable
    {
        Anchor(){
            Card(Modifier.align(Alignment.Center).size(100.dp).clickable{
                println("Clicked")
                output.value = NodeOutput(listOf(2))
            }.clip(RoundedCornerShape(15.dp))) {
                Box(Modifier.fillMaxSize().background(Colors.currentScheme.surface)) {
                    Row(Modifier.fillMaxWidth().align(Alignment.TopCenter), horizontalArrangement = Arrangement.Center) {
                        input.value.values.forEach {
                            Text(it.toString())
                        }
                    }
                    Row(Modifier.fillMaxWidth().align(Alignment.BottomCenter), horizontalArrangement = Arrangement.Center) {
                        output.value.values.forEach {
                            Text(it.toString())
                        }
                    }
                }
            }
        }
    }

}

interface Node{
    val id: String
    val name: String
    var input: MutableState<NodeInput>
    var output: MutableState<NodeOutput>
    val render: @Composable () -> Unit
}


data class NodeInput(
    val values: List<Any>
){

}

data class NodeOutput(
    val values: List<Any>
){

}