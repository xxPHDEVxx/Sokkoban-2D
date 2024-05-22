package sokoban.model;

import javafx.scene.image.Image;

public class Mushroom extends GameElement{
    public Mushroom(){
        super();
        image = new Image("mushroom.png");
    }

    @Override
    public Image getImage() {
        return super.getImage();
    }

    @Override
    public GameElement copy() {
        return new Mushroom();
    }
}
