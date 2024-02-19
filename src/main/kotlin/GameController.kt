import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameController {


    private val _state = MutableStateFlow<GameState>(GameState())
    val state = _state.asStateFlow()
    fun startGame(rows: Int = _state.value.rows, columns: Int = _state.value.columns, mines: Int = _state.value.mines) {
        _state.value = GameState(
            grid = mapGenerator(rows, columns, mines),
            state = GameProcess.STARTED,
            mines = mines,
            rows = rows,
            columns = columns
        )
    }


    fun openCell(x: Int, y: Int) {
        if (_state.value.grid[y][x].flag.value) {
            return
        }
        if (_state.value.grid[y][x].isBomb) {
            _state.value = _state.value.copy(state = GameProcess.ENDED)
            for (i in _state.value.grid.flatMap { it -> it.map { it -> if (it.isBomb) it.isOpened.value = true } })
                return
        }
        var bombsAround = 0
        for (row in (y - 1).coerceIn(0, state.value.rows - 1)..(y + 1).coerceIn(0, state.value.rows - 1)) {
            for (column in (x - 1).coerceIn(0, state.value.columns - 1)..(x + 1).coerceIn(0, state.value.columns - 1)) {
                if (state.value.grid[row][column].isBomb)
                    bombsAround++
            }
        }
        _state.value.grid[y][x].isOpened.value = true
        _state.value.grid[y][x].value.value = bombsAround
        if (bombsAround == 0) {
            for (row in (y - 1).coerceIn(0, state.value.rows - 1)..(y + 1).coerceIn(0, state.value.rows - 1)) {
                for (
                column in (x - 1).coerceIn(0, state.value.columns - 1)..
                        (x + 1).coerceIn(
                            0, state.value.columns - 1
                        )) {
                    if (!state.value.grid[row][column].isOpened.value && !state.value.grid[row][column].flag.value)
                        openCell(column, row)
                }
            }
        }
    }

    fun flagCell(x: Int, y: Int) {
        if (!_state.value.grid[y][x].isOpened.value)
            _state.value.grid[y][x].flag.value = !_state.value.grid[y][x].flag.value
    }

    fun changeMapSettings(
        rows: String = _state.value.rows.toString(),
        columns: String = _state.value.columns.toString(),
        mines: String = _state.value.mines.toString()
    ) {
        _state.value = _state.value.copy(
            rows = rows.toInt().coerceAtLeast(5),
            columns = columns.toInt().coerceAtLeast(5),
            mines = mines.toInt().coerceAtLeast(5)
        )
    }
    fun openSettings() {
        _state.value = _state.value.copy(state = GameProcess.SETTINGS)
    }

    private fun mapGenerator(rows: Int, columns: Int, mines: Int): List<List<Cell>> {
        var minescounter = 0
        return List(rows) {
            List(columns) { _ ->
                if ((1..100).random() < 10 && minescounter < mines) {
                    minescounter++
                    return@List Cell(
                        isBomb = true,
                        value = mutableStateOf(-1),
                        flag = mutableStateOf(false),
                        isOpened = mutableStateOf(
                            false
                        )
                    )
                } else
                    return@List Cell(
                        isBomb = false,
                        value = mutableStateOf(0),
                        flag = mutableStateOf(false),
                        isOpened = mutableStateOf(
                            false
                        )
                    )
            }
        }
    }



}