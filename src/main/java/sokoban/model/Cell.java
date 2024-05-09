package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Cell {
    abstract GameElement getValue();
    abstract void setValue(GameElement value);
    abstract boolean isEmpty();
    abstract boolean isBox();
    abstract boolean isPlayer();
    abstract boolean isGoal();
    abstract boolean isBoxInTarget();
    abstract ReadOnlyObjectProperty<GameElement> valueProperty();
    public abstract Cell getCell();

}
