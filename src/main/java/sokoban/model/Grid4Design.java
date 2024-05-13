package sokoban.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.util.Arrays;

public class Grid4Design {
    private static int GRID_WIDTH = 15;
    private static int GRID_HEIGHT = 10;
    private final Cell4Design[][] matrix;
    private LongBinding filledCellsCount;
    private LongBinding boxCount;
    private LongBinding playerCount;
    private LongBinding goalCount;
    private LongBinding boxInTargetCount;


    public Grid4Design() {
        this(GRID_WIDTH, GRID_HEIGHT);
    }

    public Grid4Design(int width, int height) {
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        matrix = new Cell4Design[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrix[i] = new Cell4Design[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrix[i][j] = new Cell4Design();
            }
        }
        setFilledCellsCount();
        countCell();

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
    public static int getGridWidth(){return GRID_WIDTH;}
    public static int getGridHeight(){return GRID_HEIGHT;}

    ReadOnlyObjectProperty<GameElement> valueProperty(int line, int col) {
        return matrix[line][col].valueProperty();    }

    void play(int line, int col, GameElement player) {
        matrix[line][col].setValue(player);
        filledCellsCount.invalidate();
        playerCount.invalidate();
        goalCount.invalidate();
        boxCount.invalidate();
    }

    void remove(int line, int col, GameElement ground){
        matrix[line][col].setValue(ground);
        filledCellsCount.invalidate();
        playerCount.invalidate();
        goalCount.invalidate();
        boxCount.invalidate();
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

    public boolean isEmpty(int line, int col) {return matrix[line][col].isEmpty();}
    public void countCell () {
        setBoxCellsCount();
        setPlayerCount();
        setGoalCount();
        setBoxInTargetCount();

    }

    public GameElement getValue ( int line, int col){
        return matrix[line][col].getValue();
    }

    // Vérifie si la position (line, col) est valide dans la grille
    public boolean isValidPosition ( int line, int col){
        return line >= 0 && line < GRID_HEIGHT && col >= 0 && col < GRID_WIDTH;
    }

    public void setValue(int row, int col, GameElement value) {
        if (isValidPosition(row, col)) {
            matrix[row][col].getCell().setValue(value);
        } else {
            throw new IllegalArgumentException("Invalid row or column index");
        }
    }

    public GameElement getElement(int line, int col) {
        return matrix[line][col].getValue() ;
    }

}
