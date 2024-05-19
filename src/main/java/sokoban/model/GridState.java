package sokoban.model;

import java.util.ArrayList;
import java.util.List;
import sokoban.model.Board;

public class GridState {
    private List<Board> boardHistory;
    private int currentIndex;

    public GridState() {
        boardHistory = new ArrayList<>();
        currentIndex = -1; // L'indice actuel dans l'historique est initialisé à -1 car il n'y a pas encore d'état enregistré.
    }

    // Méthode pour ajouter un nouvel état de la grille à l'historique
    public void addBoardState(Board board) {
        // clonage du board pour éviter de modifier l'objet original
        //Board clonedBoard = board.copy();
        // Lorsqu'un nouvel état est ajouté, tous les états suivants sont supprimés de l'historique.
        boardHistory.subList(currentIndex + 1, boardHistory.size()).clear();
        boardHistory.add(board);
        currentIndex = boardHistory.size() - 1; // L'indice actuel est mis à jour pour pointer vers le nouvel état ajouté.
    }

    // Méthode pour obtenir l'état précédent de la grille
    public Board getPreviousState() {
        if (currentIndex > 0) {
            currentIndex--;
            return boardHistory.get(currentIndex);
        }
        return null; // Renvoie null s'il n'y a pas d'état précédent disponible
    }

    // Méthode pour obtenir l'état suivant de la grille
    public Board getNextState() {
        if (currentIndex < boardHistory.size() - 1) {
            currentIndex++;
            return boardHistory.get(currentIndex);
        }
        return null; // Renvoie null s'il n'y a pas d'état suivant disponible
    }

    // Méthode pour vérifier si un état précédent est disponible
    public boolean hasPreviousState() {
        return currentIndex > 0;
    }

    // Méthode pour vérifier si un état suivant est disponible
    public boolean hasNextState() {
        return currentIndex < boardHistory.size() - 1;
    }

    public List<Board> getBoardHistory() {
        return boardHistory;
    }
}

