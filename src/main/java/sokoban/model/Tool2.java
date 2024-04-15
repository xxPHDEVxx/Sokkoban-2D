package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public abstract class Tool2 {
    private ObjectProperty<String> type;

    public Tool2(String type) {
        this.type = new SimpleObjectProperty<>(type);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public ReadOnlyObjectProperty<String> typeProperty() {
        return type;
    }
}
