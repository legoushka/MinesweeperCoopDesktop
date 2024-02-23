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
        if (_state.value.grid[y][x].isBomb.value) {
            _state.value = _state.value.copy(state = GameProcess.LOSE)
            for (i in _state.value.grid.flatMap { it -> it.map { it -> if (it.isBomb.value) it.isOpened.value = true } })
                return
        }
        var bombsAround = 0
        for (row in (y - 1).coerceIn(0, state.value.rows - 1)..(y + 1).coerceIn(0, state.value.rows - 1)) {
            for (column in (x - 1).coerceIn(0, state.value.columns - 1)..(x + 1).coerceIn(0, state.value.columns - 1)) {
                if (state.value.grid[row][column].isBomb.value)
                    bombsAround++
            }
        }
        _state.value.grid[y][x].isOpened.value = true
        _state.value = _state.value.copy(openedCells = _state.value.openedCells + 1)
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
        if (state.value.openedCells == (state.value.columns * state.value.rows) - state.value.mines ){
            _state.value = _state.value.copy(state = GameProcess.WIN)
        }

    }

    fun flagCell(x: Int, y: Int) {
        if (!_state.value.grid[y][x].isOpened.value)
            _state.value.grid[y][x].flag.value = !_state.value.grid[y][x].flag.value
    }

    fun changeMapSettings(
        rows: Int = _state.value.rows,
        columns: Int = _state.value.columns,
        mines: Int = _state.value.mines
    ) {
        _state.value = _state.value.copy(
            rows = rows,
            columns = columns,
            mines = mines
        )
    }

    fun openSettings() {
        _state.value = _state.value.copy(state = GameProcess.SETTINGS)
    }

    private fun mapGenerator(rows: Int, columns: Int, mines: Int): List<List<Cell>> {
        var minesCounter = mines
        val map = List(rows) {
            List(columns) { _ ->
                Cell(
                    isBomb = mutableStateOf(false),
                    value = mutableStateOf(-1),
                    flag = mutableStateOf(false),
                    isOpened = mutableStateOf(
                        false
                    )
                )
            }
        }
        while (minesCounter != 0){
            val randomRow = (0 until rows).random()
            val randomColumn = (0 until columns).random()
            if(!map[randomRow][randomColumn].isBomb.value){
                map[randomRow][randomColumn].isBomb.value = true
                minesCounter--
            }
        }
        return map

    }
}