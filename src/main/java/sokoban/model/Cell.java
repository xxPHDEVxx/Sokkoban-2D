package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Cell {
    abstract CellValue getValue();
    public abstract void setValue(CellValue value);
    abstract boolean isEmpty();
    abstract boolean isBox();
    abstract boolean isPlayer();
    abstract boolean isGoal();
    abstract ReadOnlyObjectProperty<CellValue> valueProperty();
    public abstract Cell getCell();



}
