package sokoban.model;

import java.util.List;

public class Grid4Play extends Grid{

    public Grid4Play() {
        this(GRID_WIDTH, GRID_HEIGHT);

    }

    public Grid4Play(int width, int height) {
        super();
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        matrix = new Cell4Play[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrix[i] = new Cell4Play[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrix[i][j] = new Cell4Play();
            }
        }
        setFilledCellsCount();
        countCell();
    }

    @Override
    void put(int line, int col, GameElement element) {
        Cell cell = matrix[line][col];
        List<GameElement> cellItems = cell.valueProperty();

        // Remove existing element if present
        if (cellItems.contains(element)) {
            cellItems.remove(element);
        }

        // Insert a new instance of the element
        if (element instanceof Mushroom) {
            cell.addElement(new Mushroom());
        }

        // Invalidate counters
        filledCellsCount.invalidate();
        playerCount.invalidate();
        goalCount.invalidate();
        boxCount.invalidate();
    }

    @Override
    void remove(int line, int col, GameElement element) {
    }
}
