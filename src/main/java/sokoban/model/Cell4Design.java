package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Cell4Design extends Cell{

    private final ObjectProperty<CellValue> value = new SimpleObjectProperty<>(CellValue.GROUND);
    @Override
    CellValue getValue() {return value.getValue();}
    public void setValue(CellValue value) {this.value.setValue(value);}
    boolean isEmpty() {return value.get() == CellValue.GROUND;}
    boolean isBox() {return value.get() == CellValue.BOX || value.get() == CellValue.BOX_ON_GOAL;}
    boolean isPlayer() {return value.get() == CellValue.PLAYER || value.get() == CellValue.PLAYER_ON_GOAL;}
    boolean isGoal() {return value.get() == CellValue.GOAL  || value.get() == CellValue.BOX_ON_GOAL || value.get() == CellValue.PLAYER_ON_GOAL;}
    boolean isBoxInTarget() {
        return value.get() == CellValue.BOX_ON_GOAL;
    }
    ReadOnlyObjectProperty<CellValue> valueProperty() {return value;}
    public Cell getCell(){
        return this;
    }
}
