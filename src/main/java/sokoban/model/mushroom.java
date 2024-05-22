package sokoban.model;

import javafx.scene.image.Image;

public class mushroom extends GameElement{
    public mushroom(){
        super();
        image = new Image("mushroom.png");
    }

    @Override
    public Image getImage() {
        return super.getImage();
    }

    @Override
    public GameElement copy() {
        return new mushroom();
    }
}
