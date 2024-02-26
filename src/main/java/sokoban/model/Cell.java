package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class Cell {
    private final ObjectProperty<CellValue> value = new SimpleObjectProperty<>(CellValue.GROUND);
    CellValue getValue() {return value.getValue();}
    void setValue(CellValue value) {this.value.setValue(value);}
    boolean isEmpty() {return value.get() == CellValue.GROUND;}
    ReadOnlyObjectProperty<CellValue> valueProperty() {return value;}
}
