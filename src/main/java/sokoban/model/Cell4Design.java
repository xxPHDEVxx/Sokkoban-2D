package sokoban.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;

import java.util.List;


public class Cell4Design extends Cell {

    public Cell4Design() {
        super();
    }

    @Override
    void setValues(GameElement value) {
        values.add(value);
    }
    public void remove(GameElement value) {
        this.values.remove(value);
    }

    boolean isEmpty() {
        return values.isEmpty() || values.stream().allMatch(value -> value instanceof Ground);
    }

    boolean isBox() {
        return values.stream().anyMatch(value -> value instanceof Box);
    }

    boolean isPlayer() {
        return values.stream().anyMatch(value -> value instanceof Player);
    }


    boolean isGoal() {
        return values.stream().anyMatch(value -> value instanceof Goal);
    }

    boolean isBoxInTarget() {
        return values.stream().anyMatch(value -> value instanceof Box)
                && values.stream().anyMatch(value -> value instanceof Goal);
    }

    ReadOnlyListProperty<GameElement> valueProperty() {return values;}


    public Cell getCell() {
        return this;
    }
}
