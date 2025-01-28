package controller.handlers;

import model.chess.ChessPiece;
import java.util.List;

import controller.gamecontroller.GameController;

public class HighlightHandler {
    private GameController controller;

    public HighlightHandler(GameController controller) {
        this.controller = controller;
    }

    public void highlightValidMoves(ChessPiece piece) {
        List<int[]> validMoves = controller.getModel().getValidMoves(piece);
        for (int[] move : validMoves) {
            int row = move[0];
            int col = move[1];
            ChessPiece targetPiece = controller.getModel().getPieceAt(row, col);

            boolean isAttackMove = targetPiece != null && !targetPiece.getColour().equals(piece.getColour());
            controller.getView().highlightPossibleMove(row, col, piece.getColour(), isAttackMove);
            
        }
        controller.getView().refreshGUI(); // Only for highlights, not full board rendering
    }

    public void highlightSelectedPiece(int row, int col) {
        controller.getView().highlightSelectedPiece(row, col);
    }

    public void resetHighlights() {
        controller.getView().resetHighlights();
    }
}
