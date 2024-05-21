package sokoban.model;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class Cell4Play extends Cell {
    public Cell4Play() {
        super();
    }

    @Override
    void addElement(GameElement value) {
        values.add(value);
    }

    @Override
    boolean isEmpty() {
        return values.isEmpty() || values.stream().allMatch(value -> value instanceof Ground);
    }

    @Override
    boolean isBox() {
        return values.stream().anyMatch(value -> value instanceof Box);
    }

    @Override
    boolean isPlayer() {
        return values.stream().anyMatch(value -> value instanceof Player);
    }

    @Override
    boolean isGoal() {
        return values.stream().anyMatch(value -> value instanceof Goal);
    }

    @Override
    boolean isBoxInTarget() {
        return values.stream().anyMatch(value -> value instanceof Box)
                && values.stream().anyMatch(value -> value instanceof Goal);
    }
    ReadOnlyListProperty<GameElement> valueProperty() {
        return values;
    }

    @Override
    void remove(GameElement element) {
        values.remove(element);
    }
}
