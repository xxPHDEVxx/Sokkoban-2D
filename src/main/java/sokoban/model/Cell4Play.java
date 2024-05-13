package sokoban.model;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cell4Play extends Cell {
    private final ListProperty<GameElement> values = new SimpleListProperty<>(FXCollections.observableArrayList());

    public Cell4Play() {
        super();
    }
    @Override
    ObservableList<GameElement> getValues() {
        return values.get();
    }

    @Override
    void setValues(GameElement value) {
        this.values.add(value);
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
        // Non implémenté pour cette classe spécifique
        return false;
    }
    ReadOnlyListProperty<GameElement> valueProperty() {
        return values;
    }

    @Override
    public Cell getCell() {
        return this;
    }
}
