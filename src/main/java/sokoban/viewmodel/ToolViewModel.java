package sokoban.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sokoban.model.*;
import sokoban.view.ToolView;

public class ToolViewModel {
    private static ToolView toolView;

    private static GameElement toolSelected;

    public ToolViewModel(ToolView toolView) {
        this.toolView = toolView;
    }

    public  static void setToolSelected(GameElement toolSelected) {
        ToolViewModel.toolSelected = toolSelected;
    }

    public static GameElement getToolSelected() {
        return toolSelected;
    }

}


