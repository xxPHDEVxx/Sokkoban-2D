package sokoban.model;

public class Grid4Play extends Grid{

    public Grid4Play(int width, int height) {
        super();
        gridWidth = width;
        gridHeight = height;
        matrix = new Cell4Play[gridHeight][];
        for (int i = 0; i < gridHeight; ++i) {
            matrix[i] = new Cell4Play[gridWidth];
            for (int j = 0; j < gridWidth; ++j) {
                matrix[i][j] = new Cell4Play();
            }
        }
        setFilledCellsCount();
        countCell();
    }

    public void put(int line, int col, GameElement element) {
    }

    @Override
    void remove(int line, int col, GameElement element) {
    }

}
