package sokoban.view;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.input.MouseButton;


import sokoban.model.CellValue;
import sokoban.model.Grid;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.GridViewModel;
import sokoban.viewmodel.ToolViewModel;

public abstract class CellView extends StackPane {
    public CellView() {

    }
}
