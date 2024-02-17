data class GameState(
    val grid: List<List<Cell>>,
    val state: GameProcess,
    val mines: Int,
    val rows: Int,
    val columns: Int
)

enum class GameProcess{
    STARTED,
    ENDED
}
