package sokoban.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Cell {
    protected  ListProperty<GameElement> values = new SimpleListProperty<>(FXCollections.observableArrayList());
    public Cell() {
        values.add(new Ground());
    }
    abstract ObservableList<GameElement> getValues();
    abstract void setValues(GameElement value);

    abstract boolean isEmpty();
    abstract boolean isBox();
    abstract boolean isPlayer();
    abstract boolean isGoal();
    abstract boolean isBoxInTarget();
    abstract ReadOnlyListProperty<GameElement> valueProperty();
    public abstract Cell getCell();

}
