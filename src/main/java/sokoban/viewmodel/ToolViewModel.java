package sokoban.viewmodel;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sokoban.model.CellValue;
import sokoban.model.Tool;
import sokoban.view.ToolView;

public class ToolViewModel {
    private static ToolView toolView;
    private static Tool tool;
    private static ObjectProperty<CellValue> value = new SimpleObjectProperty<>(CellValue.GROUND);

    private static CellValue toolSelected;

    public ToolViewModel(ToolView toolView) {
        this.tool = new Tool();
        this.toolView = toolView;
    }

    public CellValue getValue() {
        return value.getValue();
    }

    public void setValue(CellValue value) {
        this.value.setValue(value);
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty() {
        return value;
    }

    public  static void setToolSelected(CellValue toolSelected) {
        ToolViewModel.toolSelected = toolSelected;
    }

    public static CellValue getToolSelected() {
        return toolSelected;
    }
}


