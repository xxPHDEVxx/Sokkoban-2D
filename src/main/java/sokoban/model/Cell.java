package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

public abstract class Cell {
    protected  ListProperty<GameElement> values = new SimpleListProperty<>(FXCollections.observableArrayList());
    public Cell() {}
    abstract void addElement(GameElement value);;
    abstract boolean isEmpty();
    abstract boolean isBox();
    abstract boolean isPlayer();
    abstract boolean isGoal();
    abstract boolean isBoxInTarget();
    abstract ReadOnlyListProperty<GameElement> valueProperty();

    abstract void remove(GameElement element);

}
