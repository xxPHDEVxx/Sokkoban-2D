package sokoban.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sokoban.model.*;

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
        return values.stream().anyMatch(value -> value instanceof Box || value instanceof BoxOnGoal);
    }

    boolean isPlayer() {
        for (GameElement element : values) {
            if (element instanceof Player) {
                return true; // Si un joueur est trouvé, retourne true
            }
        }
        return false; // Si aucun joueur n'est trouvé, retourne false
    }


    boolean isGoal() {
        return values.stream().anyMatch(value -> value instanceof Goal || value instanceof BoxOnGoal || value instanceof PlayerOnGoal);
    }

    boolean isBoxInTarget() {
        return values.stream().anyMatch(value -> value instanceof BoxOnGoal);
    }
    ReadOnlyListProperty<GameElement> valueProperty() {return values;}


    public Cell getCell() {
        return this;
    }
}
