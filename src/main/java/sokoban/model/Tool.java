package sokoban.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Tool {
    private static ObjectProperty<GameElement> value = new SimpleObjectProperty<>(new Ground());
}