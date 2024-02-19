data class GameState(
    val grid: List<List<Cell>> = emptyList(),
    val state: GameProcess = GameProcess.SETTINGS,
    val mines: Int = 5,
    val rows: Int = 5,
    val columns: Int = 5
)

enum class GameProcess{
    STARTED,
    ENDED,
    SETTINGS
}
