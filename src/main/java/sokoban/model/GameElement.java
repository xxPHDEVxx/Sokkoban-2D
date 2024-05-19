package sokoban.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public abstract class GameElement {

    protected Image image;
    public  Image getImage() {
        return image;
    }

    public GameElement() {
    }

    abstract GameElement copy();

}
