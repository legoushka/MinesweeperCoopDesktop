import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GameScreen(gameController: GameController) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { gameController.startGame(5, 10, 8) }) {
            Text(text = "Start Game")
        }
        val state = gameController.state.collectAsState()

        Text(text = state.value.state.toString())
        for (i in 0 until state.value.rows) {
            Row {
                for (j in 0 until state.value.columns) {
                    CellItem(
                        cell = state.value.grid[i][j],
                        onClick = {
                            if (state.value.state == GameProcess.STARTED)
                                gameController.openCell(j, i)
                        },
                        onLongClick = {
                            if (state.value.state == GameProcess.STARTED)
                                gameController.flagCell(j, i)
                        }
                    )
                }
            }
        }
    }
}

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
            //.background(color = if (cell.value.isOpened) Color.LightGray else Color.White)
            .border(BorderStroke(1.dp, Color.Black)),
        contentAlignment = Alignment.Center
    ) {
        if (cell.isOpened.value) {
            if (cell.isBomb)
                Icon(Icons.Default.Favorite, contentDescription = "Bomb")
            else {
                val color: Color = when (cell.value.value) {
                    1 -> Color.Blue
                    2 -> Color.Green
                    3 -> Color.Red
                    else -> Color.Black
                }
                Text(
                    cell.value.value.toString(),
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.W700, color = color)
                )
            }
        }
        if (cell.flag.value) {
            Icon(Icons.Default.Warning, tint = Color.Red, contentDescription = "Flag")
        }
    }
}