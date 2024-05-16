package sokoban.model;

import javafx.beans.property.ReadOnlyListProperty;


public class Cell4Design extends Cell {

    public Cell4Design() {
        super();
    }

    public void setValues(GameElement value) {
        this.values.add(value);
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
