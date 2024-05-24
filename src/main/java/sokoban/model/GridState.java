package sokoban.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The GridState class manages the history of board states in the Sokoban game.
 * It allows tracking and navigating between different states of the game board.
 */
public class GridState {
    private final List<Board> boardHistory; // List to store the history of board states
    private int currentIndex; // Index of the current board state in the history

    /**
     * Constructs a new GridState object with an empty history.
     */
    public GridState() {
        boardHistory = new ArrayList<>();
        currentIndex = -1; // The current index in the history is initialized to -1 as there are no states recorded yet.
    }

    /**
     * Adds a new board state to the history.
     * @param board The board state to add.
     */
    public void addBoardState(Board board) {
        // Cloning the board to avoid modifying the original object
        Board clonedBoard = board.copy();
        // When a new state is added, all subsequent states are removed from the history.
        if (currentIndex < boardHistory.size() - 1) {
            boardHistory.subList(currentIndex + 1, boardHistory.size()).clear();
        }
        boardHistory.add(clonedBoard);
        currentIndex = boardHistory.size() - 1; // The current index is updated to point to the newly added state.
    }

    /**
     * Retrieves the previous state of the board.
     * @return The previous board state.
     */
    public Board getPreviousState() {
        if (currentIndex >= 1) {
            currentIndex--;
            return boardHistory.get(currentIndex);
        }
        return null; // Returns null if there is no previous state available
    }

    /**
     * Retrieves the next state of the board.
     * @return The next board state.
     */
    public Board getNextState() {
        if (currentIndex < boardHistory.size() - 1) {
            currentIndex++;
            return boardHistory.get(currentIndex);
        }
        return null; // Returns null if there is no next state available
    }

    /**
     * Checks if a previous state is available.
     * @return True if a previous state is available, false otherwise.
     */
    public boolean hasPreviousState() {
        return currentIndex > 0;
    }

    /**
     * Checks if a next state is available.
     * @return True if a next state is available, false otherwise.
     */
    public boolean hasNextState() {
        return currentIndex < boardHistory.size() - 1;
    }

    /**
     * Retrieves the list of board states in the history.
     * @return The list of board states.
     */
    public List<Board> getBoardHistory() {
        return boardHistory;
    }

    /**
     * Sets the current index in the history.
     * @param currentIndex The index to set.
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
