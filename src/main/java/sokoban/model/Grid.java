package sokoban.model;


import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

import java.util.Arrays;
import java.util.List;

public abstract class Grid {
     // Grid dimensions
     protected static int GRID_WIDTH = 15;
     protected static int GRID_HEIGHT = 10;

     // Matrix holding the cells of the grid
     protected  Cell[][] matrix;

     // Bindings for counting various elements in the grid
     protected LongBinding filledCellsCount;
     protected LongBinding boxCount;
     protected LongBinding playerCount;
     protected LongBinding goalCount;
     protected LongBinding boxInTargetCount;
     public Grid() {
     }

     /**
      * Copies the contents of another Grid4Design object into this one.
      * @param copy The grid to copy from.
      */
     public void copyFill(Grid copy) {
          // Check if dimensions match
          if (this.getGridHeight() != copy.getGridHeight() || this.getGridWidth() != copy.getGridWidth()) {
               throw new IllegalArgumentException("Grid dimensions do not match.");
          }

          // Clear current elements
          for (int i = 0; i < this.getGridHeight(); i++) {
               for (int j = 0; j < this.getGridWidth(); j++) {
                    this.valueProperty(i, j).clear();
               }
          }

          // Copy elements from source grid
          for (int i = 0; i < copy.getGridHeight(); i++) {
               for (int j = 0; j < copy.getGridWidth(); j++) {
                    List<GameElement> copyElements = copy.valueProperty(i, j);
                    for (GameElement element : copyElements) {
                         this.valueProperty(i, j).add(element.copy()); // Ensure to copy elements to avoid cross-references
                    }
               }
          }
     }

     // Getters for grid dimensions
     public static int getGridWidth() { return GRID_WIDTH; }
     public static int getGridHeight() { return GRID_HEIGHT; }

     /**
      * Returns the value property of the cell at the specified position.
      * @param line The row index.
      * @param col The column index.
      * @return The value property of the cell.
      */
     ReadOnlyListProperty<GameElement> valueProperty(int line, int col) {
          return matrix[line][col].valueProperty();
     }

     abstract void put(int line, int col, GameElement element);

     abstract void remove(int line, int col, GameElement element);

    /**
     * Returns the list of game elements at the specified position.
     * @param line The row index.
     * @param col The column index.
     * @return The list of game elements at the specified position.
     */
    public ObservableList<GameElement> getValues(int line, int col) {
        return matrix[line][col].valueProperty();
    }

    /**
     * Checks if the specified position is valid within the grid.
     * @param line The row index.
     * @param col The column index.
     * @return True if the position is valid, false otherwise.
     */
    public boolean isValidPosition(int line, int col) {
        return line >= 0 && line < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH;
    }

    public void setFilledCellsCount() {
        filledCellsCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> !cell.isEmpty())
                .count());
    }
    public void setBoxInTargetCount() {
        boxInTargetCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isBoxInTarget())
                .count());
    }
    public void setBoxCellsCount() {
        boxCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isBox())
                .count());
    }
    public void setPlayerCount() {
        playerCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isPlayer())
                .count());
    }
    public void setGoalCount() {
        goalCount = Bindings.createLongBinding(() -> Arrays
                .stream(matrix)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.isGoal())
                .count());
    }

    public LongBinding filledCellsCountProperty() {
        return filledCellsCount;
    }
    public LongBinding boxCountProperty() {
        return boxCount;
    }
    public LongBinding goalCountProperty() {
        return goalCount;
    }
    public LongBinding playerCountProperty() {
        return playerCount;
    }
    public LongBinding boxInTargetCountProperty() {
        return boxInTargetCount;
    }

    public void countCell () {
        setBoxCellsCount();
        setPlayerCount();
        setGoalCount();
        setBoxInTargetCount();

    }

    abstract public void addElement(int row, int col, GameElement gameElement);



}