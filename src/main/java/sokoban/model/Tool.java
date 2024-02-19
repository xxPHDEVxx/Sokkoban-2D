package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Tool {
    private static ObjectProperty<CellValue> value = new SimpleObjectProperty<>(CellValue.GROUND);
    CellValue getValue() {return value.getValue();}
    void setValue(CellValue value) {this.value.setValue(value);}
    ReadOnlyObjectProperty<CellValue> valueProperty() {return value;}
}
