package controller.handlers;

import model.chess.ChessPiece;
import java.awt.Toolkit;

import controller.gamecontroller.GameController;

public class MovementHandler {
    private GameController controller;

    public MovementHandler(GameController controller) {
        this.controller = controller;
    }

    public String getIconNameForPiece(ChessPiece piece) {
        if (piece == null)
            return null;

        return piece.getColour().toLowerCase().charAt(0) + "_" + piece.getType().toString().toLowerCase();

    }

    public void handlePieceMovement(int row, int col, ChessPiece selectedPiece) {

        if (controller.getModel().isValidMovePiece(selectedPiece, row, col)) {
            int initRow = selectedPiece.getRow();
            int initCol = selectedPiece.getCol();
            boolean isReversed = controller.getModel().getPieceReversed(selectedPiece);

            if (controller.getModel().getPieceAt(row, col) != null) {
                controller.getView().setPieceImage(row, col, null);
            }

                controller.getModel().movePiece(selectedPiece, row, col);

                controller.getView().setReverse(row, col, isReversed);
                controller.getView().setReverse(initRow, initCol, false);

            controller.getModel().switchOR_Piece();
            controller.getView().updatePieceImagePosition(getIconNameForPiece(selectedPiece), initRow, initCol, row,
                    col);

            String piecePlacementSoundName = "piece_placement";
            controller.playSound(piecePlacementSoundName, false);

            controller.refreshBoard();
            controller.resetSelection();

        } else {
            Toolkit.getDefaultToolkit().beep();
            controller.resetSelection();
        }
    }
}
