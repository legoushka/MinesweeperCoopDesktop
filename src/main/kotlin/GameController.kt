import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

class GameController {

    private var _gameState = GameState(emptyList(), GameProcess.ENDED, 0, 0, 0)
    val gameState by mutableStateOf(_gameState)
    fun startGame(rows: Int, columns: Int, mines: Int) {
        _gameState = GameState(
            grid = mapGenerator(rows, columns, mines),
            state =  GameProcess.STARTED,
            mines = mines,
            rows = rows,
            columns = columns
        )
    }

    fun openCell(x: Int, y: Int) {
        var bombsAround = 0
        for (row in (y - 1).coerceIn(0, gameState.rows - 1)..(y + 1).coerceIn(0, gameState.rows - 1)) {
            for (column in (x - 1).coerceIn(0, gameState.columns - 1)..(x + 1).coerceIn(0, gameState.columns - 1)) {
                if (gameState.grid[row][column].isBomb)
                    bombsAround++
            }
        }
        _gameState.grid[y][x].isOpened = true
        _gameState.grid[y][x].value = bombsAround

        if (bombsAround == 0) {
            for (row in (y - 1).coerceIn(0, gameState.rows - 1)..(y + 1).coerceIn(0, gameState.rows - 1)) {
                for (column in (x - 1).coerceIn(0, gameState.columns - 1)..(x + 1).coerceIn(0, gameState.columns - 1)) {
                    if (!gameState.grid[y][x].isOpened && !gameState.grid[y][x].flag)
                        openCell(x, y)
                }
            }
        }
    }

    fun flagCell(x: Int, y: Int) {
        _gameState.grid[y][x].flag = !_gameState.grid[y][x].flag
    }

    private fun mapGenerator(rows: Int, columns: Int, mines: Int): List<List<Cell>> {
        var minescounter = 0
        return List<List<Cell>>(rows) {
            List<Cell>(columns) { _ ->
                if ((1..100).random() < 10 && minescounter < mines)
                {
                    minescounter++
                    return@List Cell(isBomb = false, value = -1, flag = false, isOpened = false)
                }
                else
                    return@List Cell(isBomb = false, value = 0, flag = false, isOpened = false)
            }
        }
    }

}