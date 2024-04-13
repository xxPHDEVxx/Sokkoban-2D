package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.ReadOnlyObjectProperty;

import java.io.File;

// à compléter avec les méthodes nécessaires ( Board4Design peut t'aider)
// certaines méthodes overide sont pas necessaires, il faut les supprimer et ajuster la classe parente Board *Jamila
public class Board4Play extends Board {
    @Override
    public void play(int line, int col) {

    }

    @Override
    public Boolean isFull() {
        return null;
    }

    @Override
    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return null;
    }

    @Override
    public boolean isEmpty(int line, int col) {
        return false;
    }

    @Override
    public void configureBindings() {

    }

    @Override
    public BooleanBinding getRulesOK() {
        return null;
    }

    @Override
    public Grid getGrid() {
        return null;
    }

    @Override
    public Board getBoard() {
        return null;
    }

    @Override
    public Grid open(File file) {
        return null;
    }

    @Override
    public boolean isPlayerPlaced() {
        return false;
    }
}
