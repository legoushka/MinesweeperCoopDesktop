import androidx.compose.foundation.*
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
        val state = gameController.state.collectAsState()
        Text(text = state.value.state.toString())
        Button(onClick = { gameController.startGame() }) {
            Text(text = "Start Game")
        }
        Button(onClick = { gameController.openSettings() }) {
            Text(text = "Settings")
        }
        if (state.value.state == GameProcess.SETTINGS) {
            Row {
                Button(onClick = { gameController.changeMapSettings(rows = 9, columns = 9, mines = 10) }) {
                    Text(text = "Beginner")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { gameController.changeMapSettings(rows = 16, columns = 16, mines = 40) }) {
                    Text(text = "Intermediate")
                }
                Spacer(Modifier.width(8.dp))
                Button(onClick = { gameController.changeMapSettings(rows = 16, columns = 30, mines = 99) }) {
                    Text(text = "Expert")
                }
            }
        }

        if (state.value.state != GameProcess.SETTINGS) {
            Column(
                modifier = Modifier.border(border = BorderStroke(width = 2.dp, color = Color(0Xff818181)))
            ) {
                CellGrid(
                    rows = state.value.rows,
                    columns = state.value.columns,
                    grid = state.value.grid,
                    gameState = state.value.state,
                    onOpenCell = gameController::openCell,
                    onFlagCell = gameController::flagCell,
                    modifier = Modifier.padding(2.dp)
                )
            }

        }

    }
}


@Composable
fun CellGrid(
    rows: Int,
    columns: Int,
    grid: List<List<Cell>>,
    gameState: GameProcess,
    onOpenCell: (Int, Int) -> Unit,
    onFlagCell: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column (modifier = modifier) {
        for (i in 0 until rows) {
            Row {
                for (j in 0 until columns) {
                    CellItem(
                        cell = grid[i][j],
                        onClick = {
                            if (gameState == GameProcess.STARTED)
                                onOpenCell(j, i)
                        },
                        onLongClick = {
                            if (gameState == GameProcess.STARTED)
                                onFlagCell(j, i)
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
            .size(30.dp)
            .background(color = Color(0XFFc0c0c0))
        //.shadow(elevation = if(!cell.isOpened.value)1.5.dp else 0.dp)
        //.border(border = BorderStroke(width = 1.5.dp, color = Color(0XFF818181)))
        ,
        contentAlignment = Alignment.Center
    ) {
        if (cell.isOpened.value)
            Canvas(modifier = Modifier.fillMaxSize().clipToBounds()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = canvasWidth, y = 0f),
                    color = Color(0XFF818181),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 0f, y = canvasHeight),
                    color = Color(0XFF818181),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = Color(0XFF818181),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = Color(0XFF818181),
                    strokeWidth = 5f,
                    cap = StrokeCap.Round
                )
            }
        else
            Canvas(modifier = Modifier.fillMaxSize().clipToBounds()) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = canvasWidth, y = 0f),
                    color = Color.White,
                    strokeWidth = 10f,
                )
                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = 0f, y = canvasHeight),
                    color = Color.White,
                    strokeWidth = 10f,
                )
                drawLine(
                    start = Offset(x = canvasWidth, y = 0f),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = Color(0XFF818181),
                    strokeWidth = 10f,
                )
                drawLine(
                    start = Offset(x = 0f, y = canvasHeight),
                    end = Offset(x = canvasWidth, y = canvasHeight),
                    color = Color(0XFF818181),
                    strokeWidth = 10f,
                )
            }
        if (cell.isOpened.value) {
            if (cell.isBomb.value)
                Icon(Icons.Default.Favorite, contentDescription = "Bomb")
            else {
                val color: Color = when (cell.value.value) {
                    1 -> Color(0XFF0302fd)
                    2 -> Color(0XFF048c06)
                    3 -> Color(0XFFff0900)
                    4 -> Color(0XFF000083)
                    5 -> Color(0XFF790000)
                    6 -> Color(0XFF008488)
                    7 -> Color(0XFF000000)
                    8 -> Color(0XFF868686)
                    else -> Color.Black
                }
                Text(
                    cell.value.value.toString(),
                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W700, color = color)
                )
            }
        }
        if (cell.flag.value) {
            Icon(Icons.Default.Warning, tint = Color.Red, contentDescription = "Flag")
        }
    }
}

