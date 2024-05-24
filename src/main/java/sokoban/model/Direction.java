package sokoban.model;

/**
 * The Direction enum represents the four possible movement directions in the Sokoban game.
 * Each direction is associated with a change in row and column values that can be used to
 * move elements on the game grid.
 */
public enum Direction {
    UP(-1, 0),   // Represents movement upwards in the grid.
    DOWN(1, 0),  // Represents movement downwards in the grid.
    LEFT(0, -1), // Represents movement to the left in the grid.
    RIGHT(0, 1); // Represents movement to the right in the grid.

    // Delta values for row and column that define the direction of movement.
    private final int deltaRow;
    private final int deltaCol;

    /**
     * Constructor for the Direction enum.
     *
     * @param deltaRow the change in the row index when moving in this direction.
     * @param deltaCol the change in the column index when moving in this direction.
     */
    Direction(int deltaRow, int deltaCol) {
        this.deltaRow = deltaRow;
        this.deltaCol = deltaCol;
    }

    /**
     * Gets the change in the row index for this direction.
     *
     * @return the delta row value.
     */
    public int getDeltaRow() {
        return deltaRow;
    }

    /**
     * Gets the change in the column index for this direction.
     *
     * @return the delta column value.
     */
    public int getDeltaCol() {
        return deltaCol;
    }
}
