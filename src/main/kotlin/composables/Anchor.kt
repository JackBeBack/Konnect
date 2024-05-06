package composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.LayoutScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.NoInspectorInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.max

@Composable
inline fun Anchor(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    content: @Composable AnchorScope.() -> Unit
) {
    val measurePolicy = rememberAnchorMeasurePolicy(contentAlignment)
    Layout(
        content = { AnchorScopeInstance.content() },
        measurePolicy = measurePolicy,
        modifier = modifier
    )
}

@PublishedApi
@Composable
internal fun rememberAnchorMeasurePolicy(
    alignment: Alignment
) = remember(alignment) {
    if (alignment == Alignment.TopStart) {
        DefaultAnchorMeasurePolicy
    } else {
        anchorMeasurePolicy(alignment)
    }
}

internal val DefaultAnchorMeasurePolicy: MeasurePolicy = anchorMeasurePolicy(Alignment.TopStart)

internal fun anchorMeasurePolicy(alignment: Alignment) =
    MeasurePolicy { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@MeasurePolicy layout(
                constraints.minWidth,
                constraints.minHeight
            ) {}
        }

        val contentConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        if (measurables.size == 1) {
            val measurable = measurables[0]
            val anchorWidth: Int
            val anchorHeight: Int
            val placeable: Placeable = measurable.measure(contentConstraints)
            anchorWidth = max(constraints.minWidth, placeable.width)
            anchorHeight = max(constraints.minHeight, placeable.height)
            return@MeasurePolicy layout(0, 0) {
                placeOnAnchor(placeable, measurable, 0, 0, layoutDirection, alignment)
            }
        }

        val placeables = arrayOfNulls<Placeable>(measurables.size)
        // First measure non match parent size children to get the size of the Box.
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0
        var anchorWidth = constraints.minWidth
        var anchorHeight = constraints.minHeight
        measurables.forEachIndexed { index, measurable ->
            val childAlignment = measurable.alignment ?: alignment
            val placeable = measurable.measure(contentConstraints)
            placeables[index] = placeable
            val size = IntSize(placeable.width, placeable.height)
            val childOffset = childAlignment.align(
                size,
                size * 2,
                layoutDirection
            )
            left = max(left, -childOffset.x)
            right = max(right,  childOffset.x)
            top = max(top, -childOffset.y)
            bottom = max(bottom,  childOffset.y)
        }

        anchorWidth = max(anchorWidth, 2*max(left,right))
        anchorHeight = max(anchorHeight, 2*max(top,bottom))

        // Specify the size of the Anchor and position its children.
        layout(0, 0) {
            placeables.forEachIndexed { index, placeable ->
                placeable as Placeable
                val measurable = measurables[index]
                placeOnAnchor(placeable, measurable, anchorWidth/2, anchorHeight/2, layoutDirection, alignment)
            }
        }
    }

private fun Placeable.PlacementScope.placeOnAnchor(
    placeable: Placeable,
    measurable: Measurable,
    anchorX: Int,
    anchorY: Int,
    layoutDirection: LayoutDirection,
    alignment: Alignment
) {
    val childAlignment = measurable.anchorChildData?.alignment ?: alignment
    val size = IntSize(placeable.width, placeable.height)
    val position = IntOffset(size.width, size.height) - childAlignment.align(
        size,
        size * 2,
        layoutDirection
    )
    placeable.place(-position.x, -position.y)
}

/**
 * A box with no content that can participate in layout, drawing, pointer input
 * due to the [modifier] applied to it.
 *
 * Example usage:
 *
 * @sample androidx.compose.foundation.layout.samples.SimpleBox
 *
 * @param modifier The modifier to be applied to the layout.
 */
@Composable
fun Anchor(modifier: Modifier = Modifier) {
    Layout({}, measurePolicy = EmptyAnchorMeasurePolicy, modifier = modifier)
}

internal val EmptyAnchorMeasurePolicy = MeasurePolicy { _, constraints ->
    layout(constraints.minWidth, constraints.minHeight) {}
}

/**
 * A BoxScope provides a scope for the children of [Box] and [BoxWithConstraints].
 */
@LayoutScopeMarker
@Immutable
interface AnchorScope {
    /**
     * Pull the content element to a specific [Alignment] within the [Box]. This alignment will
     * have priority over the [Box]'s `alignment` parameter.
     */
    @Stable
    fun Modifier.align(alignment: Alignment): Modifier
}

internal object AnchorScopeInstance : AnchorScope {
    @Stable
    override fun Modifier.align(alignment: Alignment) = this.then(
        AnchorChildData(
            alignment = alignment,
            inspectorInfo = debugInspectorInfo {
                name = "align"
                value = alignment
            }
        )
    )
}

private val Measurable.anchorChildData: AnchorChildData? get() = parentData as? AnchorChildData
private val Measurable.alignment: Alignment? get() = anchorChildData?.alignment

private class AnchorChildData(
    var alignment: Alignment,
    inspectorInfo: InspectorInfo.() -> Unit = NoInspectorInfo
) : ParentDataModifier, InspectorValueInfo(inspectorInfo) {
    override fun Density.modifyParentData(parentData: Any?) = this@AnchorChildData

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        val otherModifier = other as? AnchorChildData ?: return false

        return alignment == otherModifier.alignment
    }

    override fun hashCode(): Int {
        return alignment.hashCode()
    }

    override fun toString(): String =
        "AnchorChildData(alignment=$alignment)"
}
