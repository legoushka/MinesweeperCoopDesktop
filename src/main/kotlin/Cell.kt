import androidx.compose.runtime.MutableState

data class Cell (
    val isBomb: Boolean,
    var value: MutableState<Int>,
    var isOpened: MutableState<Boolean>,
    var flag: MutableState<Boolean>,
)