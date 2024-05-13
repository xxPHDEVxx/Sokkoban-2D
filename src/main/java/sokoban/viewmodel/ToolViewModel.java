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
    private static Tool tool;
    private static ObjectProperty<GameElement> value = new SimpleObjectProperty<>();

    private static GameElement toolSelected;

    public ToolViewModel(ToolView toolView) {
        this.tool = new Tool();
        this.toolView = toolView;
    }

    public GameElement getValue() {
        return value.getValue();
    }

    public void setValue(GameElement value) {
        this.value.setValue(value);
    }

    public ReadOnlyObjectProperty<GameElement> valueProperty() {
        return value;
    }

    public  static void setToolSelected(GameElement toolSelected) {
        ToolViewModel.toolSelected = toolSelected;
    }

    public static GameElement getToolSelected() {
        return toolSelected;
    }
}


