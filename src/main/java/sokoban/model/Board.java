package sokoban.model;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import sokoban.viewmodel.ToolViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Board {
    public static int MAX_FILLED_CELLS = 75;
    private static Grid grid = new Grid();
    private final BooleanBinding isFull;

    //private final BooleanBinding CountOK;

    private SetProperty<String> errors = new SimpleSetProperty<>(FXCollections.observableSet());

    public Board(){
        isFull = grid.filledCellsCountProperty().isEqualTo(Board.MAX_FILLED_CELLS);
        System.out.println(grid.boxCountProperty());
    }

    public static int maxFilledCells() {
        MAX_FILLED_CELLS = (Grid.getGridHeight() * Grid.getGridWidth()) / 2;
        return Board.MAX_FILLED_CELLS;
    }


    public void play(int line, int col){
        grid.play(line, col, grid.getValue(line, col) != (CellValue.GROUND) ? CellValue.GROUND : ToolViewModel.getToolSelected());
        System.out.println(grid.boxCountProperty());

    }

    public Boolean isFull() {
        return isFull.get();
    }

    public ReadOnlyObjectProperty<CellValue> valueProperty(int line, int col) {
        return grid.valueProperty(line, col);
    }

    public LongBinding filledCellsCountProperty() {
        return grid.filledCellsCountProperty();
    }

    public boolean isEmpty(int line, int col) {
        return grid.isEmpty(line, col);
        //appelation cohérente? car ground n'est pas vide
    }

    public SetProperty<String> validate(){


        int playerCount = 0;
        int targetCount = 0;
        int boxCount = 0;

        int width = grid.getGridWidth();
        int height = grid.getGridHeight();

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {
                CellValue value = grid.getValue(line, col);

                switch (value) {
                    case PLAYER -> playerCount++;
                    case GOAL -> targetCount++;
                    case BOX -> boxCount++;
                    default -> {
                    }
                }
            }
        }

        if (playerCount != 1) {
            errors.add("- A player is required.");
        }

        if (targetCount < 1) {
            errors.add("- At least one target is required.");
        }

        if (boxCount < 1) {
            errors.add("- At least one box is required.");
        }
        if (targetCount != boxCount){
            errors.add("- Number of box and target must be equals.");
        }
        System.out.println(boxCount);
        return errors;
    }
    public boolean validCount() {
        int playerCount = 0;
        int targetCount = 0;
        int boxCount = 0;

        int width = grid.getGridWidth();
        int height = grid.getGridHeight();

        for (int line = 0; line < height; line++) {
            for (int col = 0; col < width; col++) {
                CellValue value = grid.getValue(line, col);

                switch (value) {
                    case PLAYER -> playerCount++;
                    case GOAL -> targetCount++;
                    case BOX -> boxCount++;
                    default -> {
                    }
                }
            }
        }
        if (boxCount < 1) {
            return true;
        }

        return false;
    }
    public static void setGrid(Grid newGrid) {
        grid = newGrid;
    }

    public Grid getGrid() {
        return grid;
    }
    public Board getBoard(){
        return this;
    }
    public Grid open(File file){
        try (Scanner scanner = new Scanner(file)) {
            int row = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    // Parcourir chaque caractère de la ligne
                    for (int col = 0; col < line.length(); col++) {
                        char symbol = line.charAt(col);
                        // Convertir le caractère en CellValue et ajouter à la grille
                        CellValue cellValue = convertSymbolToCellValue(symbol);
                        grid.setValue(row, col, cellValue);
                    }
                    row++;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return grid;
    }
    private static CellValue convertSymbolToCellValue(char symbol) {
        switch (symbol) {
            case '#':
                return CellValue.WALL;
            case '.':
                return CellValue.GOAL;
            case '$':
                return CellValue.BOX;
            case '@':
                return CellValue.PLAYER;
            default:
                return CellValue.GROUND;
        }
    }
}
