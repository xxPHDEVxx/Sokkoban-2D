package sokoban.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import sokoban.model.CellValue;
import sokoban.viewmodel.CellViewModel;
import sokoban.viewmodel.ToolViewModel;

public class CellView extends StackPane {
    private static final Image groundImage = new Image("ground.png");
    private final CellViewModel cellViewModel;

    private static ToolViewModel toolViewModel;

    public CellView(CellViewModel cellViewModel, ToolViewModel toolViewModel) {
        this.cellViewModel = cellViewModel;
        this.toolViewModel = toolViewModel;
    }

    private void setImage(ImageView imageView, CellValue cellValue) {

    }
}
