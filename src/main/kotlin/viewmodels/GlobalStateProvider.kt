package viewmodels

import data.Transform
import kotlinx.coroutines.flow.MutableStateFlow

class GlobalStateProvider {
    companion object {
        val desktopTransform = MutableStateFlow(Transform())
    }

}