package sokoban.model;

public class Grid4Play extends Grid{

    public Grid4Play() {
        this(GRID_WIDTH, GRID_HEIGHT);

    }

    @Override
    void put(int line, int col, GameElement element){
    }

    @Override
    void remove(int line, int col, GameElement element) {
    }

    @Override
    public void addElement(int row, int col, GameElement gameElement) {
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
}
