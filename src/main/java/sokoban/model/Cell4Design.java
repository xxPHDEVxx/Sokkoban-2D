package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Cell4Design extends Cell{

    private final ObjectProperty<GameElement> value = new SimpleObjectProperty<>(new Ground());
    @Override
    GameElement getValue() {return value.getValue();}
    public void setValue(GameElement value) {this.value.setValue(value);}
    boolean isEmpty() {return value.get() instanceof Ground;}
    boolean isBox() {return value.get() instanceof Box || value.get() instanceof BoxOnGoal;}
    boolean isPlayer() {return value.get() instanceof Player || value.get() instanceof PlayerOnGoal;}
    boolean isGoal() {return value.get() instanceof Goal  || value.get() instanceof BoxOnGoal || value.get() instanceof PlayerOnGoal;}
    boolean isBoxInTarget() {
        return value.get() instanceof BoxOnGoal;
    }
    ReadOnlyObjectProperty<GameElement> valueProperty() {return value;}

    public Cell getCell(){
        return this;
    }
}
