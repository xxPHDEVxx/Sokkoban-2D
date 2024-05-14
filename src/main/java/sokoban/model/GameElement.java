package sokoban.model;

import javafx.scene.image.Image;

public abstract class GameElement {
    protected int x;
    protected int y;
    protected CellValue value;
    protected Image image;
    public  Image getImage() {
        return image;
    }

    public GameElement() {
    }

    public abstract void draw();

    // Getters et setters
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {this.y = y;}

    public CellValue getValue() {
        return value;
    }


}
