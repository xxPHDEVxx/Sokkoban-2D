package sokoban.model;

import javafx.beans.property.*;

public abstract class Cell {
    private final ListProperty<GameElement[]> values = new SimpleListProperty<>();
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
