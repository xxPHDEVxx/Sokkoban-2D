package sokoban.model;
import javafx.scene.image.Image;
public class PlayerOnGoal extends Goal{
    private static Image image2;
    public PlayerOnGoal() {
        this.value = CellValue.PLAYER_ON_GOAL;
        image2 = new Image("player.png");
    }
}
