data class GameState(
    val grid: List<List<Cell>> = emptyList(),
    val state: GameProcess = GameProcess.IDLE,
    val mines: Int = 0,
    val rows: Int = 0,
    val columns: Int = 0
)

enum class GameProcess{
    STARTED,
    ENDED,
    IDLE
}
