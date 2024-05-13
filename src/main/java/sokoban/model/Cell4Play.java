package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Cell4Play extends Cell{
    private final ObjectProperty<GameElement> value = new SimpleObjectProperty<>(new Ground());
    GameElement getValue() {return value.getValue();}
    public void setValue(GameElement value) {this.value.setValue(value);}
    boolean isEmpty() {return value.get() instanceof Ground;}
    boolean isBox() {return value.get() instanceof Box /*|| value.get() instanceof BOX_ON_GOAL*/;}
    boolean isPlayer() {return value.get() instanceof Player /*|| value.get() == CellValue.PLAYER_ON_GOAL*/;}
    boolean isGoal() {return value.get() instanceof Goal  /*|| value.get() == CellValue.BOX_ON_GOAL || value.get() == CellValue.PLAYER_ON_GOAL*/;}
    boolean isBoxInTarget() {
        return false;//return value.get() == CellValue.BOX_ON_GOAL;
    }

    ReadOnlyObjectProperty<GameElement> valueProperty() {return value;}
    public Cell getCell(){
        return this;
    }

}
