package sokoban.model;

public class Grid4Play extends Grid{

    private Cell4Play[][] matrixPlay;
    public Grid4Play() {
        this(GRID_WIDTH, GRID_HEIGHT);

    }
    public Grid4Play(int width, int height) {
        GRID_WIDTH = width;
        GRID_HEIGHT = height;
        matrixPlay = new Cell4Play[GRID_HEIGHT][];
        for (int i = 0; i < GRID_HEIGHT; ++i) {
            matrixPlay[i] = new Cell4Play[GRID_WIDTH];
            for (int j = 0; j < GRID_WIDTH; ++j) {
                matrixPlay[i][j] = new Cell4Play();
            }
        }

    }
}
