package sokoban.model;
import javafx.scene.image.Image;
public class BoxOnGoal extends Goal{
    private static Image image2;
    public BoxOnGoal() {
        this.value = CellValue.BOX_ON_GOAL;
        image2 = new Image("Box.png");
    }
}
