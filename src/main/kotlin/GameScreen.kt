import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CellItem(
    cell: Cell,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .size(60.dp)
            .background(color = if (cell.isOpened) Color.LightGray else Color.White)
            .border(BorderStroke(1.dp, Color.Black)),
        contentAlignment = Alignment.Center
    ) {
        if (cell.isOpened) {
            if (cell.isBomb)
                Icon(Icons.Default.Favorite, contentDescription = "Bomb")
            else {
                val color: Color = when (cell.value) {
                    1 -> Color.Blue
                    2 -> Color.Green
                    3 -> Color.Red
                    else -> Color.Black
                }
                Text(
                    cell.value.toString(),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.W700, color = color)
                )
            }
        }
        if (cell.flag) {
            Icon(Icons.Default.Warning, tint = Color.Red, contentDescription = "Flag")
        }
    }
}