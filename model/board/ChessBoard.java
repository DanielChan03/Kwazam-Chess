package model.board;

import java.util.ArrayList;
import java.util.Arrays;

import model.chess.ChessPieceFactory;
import model.chess.ChessPieceType;
import model.chess.RamPiece;
import model.chess.TorPiece;
import model.chess.XorPiece;
import model.chess.ChessPiece;

public class ChessBoard {

    ArrayList<ChessPiece> chess;
    private ChessPiece[][] board;
    private int movementCounter;
    private final int MAX_ROW = 8;
    private final int MAX_COL = 5;

    public ChessBoard() {
        board = new ChessPiece[MAX_ROW][MAX_COL];
        chess = new ArrayList<>();
        this.movementCounter = 0;

    }

    public boolean checkPositionOccupied(int index, int newRow, int newCol) {

        return (chess.get(index).getRow() == newRow) && (chess.get(index).getCol() == newCol);

    }

    public boolean isPathBlocked(ChessPiece[][] board, int currentRow, int currentCol, int targetRow, int targetCol, String pieceColor) {
        int rowStep = Integer.compare(targetRow, currentRow); // Direction in rows (-1, 0, 1)
        int colStep = Integer.compare(targetCol, currentCol); // Direction in columns (-1, 0, 1)
    
        // Start from the square just after the current position
        int row = currentRow + rowStep;
        int col = currentCol + colStep;
    
        // Traverse the path, stopping before the target position
        while (row != targetRow || col != targetCol) {
            if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
                return true; // Path is blocked due to out-of-bounds movement
            }
            
            ChessPiece blockingPiece = board[row][col];
// If there's a piece on the path
if (blockingPiece != null) {

    // If the blocking piece is the same color, the path is blocked
    if (blockingPiece.getColour().equals(pieceColor)) {
        return true; // Cannot move further if the piece is of the same color
    }

    // If the blocking piece is an opponent's piece, we check if it's the target
    if (!blockingPiece.getColour().equals(pieceColor)) {
        if (row == targetRow && col == targetCol) {
            // If the opponent's piece is on the target square, capture it
            return false;
        } else {
            // If the opponent's piece is not on the target, block the move
            return true;
        }
    }
}
        // Move to the next square in the path
        row += rowStep;
        col += colStep;
        }

        return false;
    }
    
    public boolean validMovePiece(ChessPiece piece, int newRow, int newCol) {
        if (piece.getRow() == newRow && piece.getCol() == newCol) {
            return false;
        }
         // Handle XorPiece and TorPiece path-blocking logic
    if (piece.getType() == ChessPieceType.XOR || piece.getType() == ChessPieceType.TOR) {

        if (isPathBlocked(board, piece.getRow(), piece.getCol(), newRow, newCol, piece.getColour())) {
            return false; // Movement is invalid if the path is blocked
        }
    }

        for (int index = 0; index < chess.size(); index++) {
            ChessPiece otherPiece = chess.get(index);
            if (checkPositionOccupied(index, newRow, newCol)) {
                
                return !piece.getColour().equals(otherPiece.getColour()) && piece.isvalidMove(newRow, newCol);
            }
        }

        return piece.isvalidMove(newRow, newCol);
    }

    public void movePiece(ChessPiece piece, int newRow, int newCol) {

        int initRow = piece.getRow();
        int initCol = piece.getCol();

        ChessPiece capturedPiece = getPieceAt(newRow, newCol);

        if (capturedPiece != null && !capturedPiece.getColour().equals(piece.getColour())) {
            chess.remove(capturedPiece);
        }

        board[initRow][initCol] = null;
        board[newRow][newCol] = piece; // killing opponent chess
        piece.move(newRow, newCol);

        chess.set(chess.indexOf(piece), piece);

        movementCounter++;

    }

    public void switchTo_TOR(XorPiece xorPiece) {

        int row = xorPiece.getRow();
        int col = xorPiece.getCol();
        String colour = xorPiece.getColour();
        int index = chess.indexOf(xorPiece);

        if (movementCounter % 2 == 0 && movementCounter != 0) {

            ChessPiece torPiece = ChessPieceFactory.createPiece(ChessPieceType.TOR, row, col, colour);

            this.board[row][col] = torPiece;
            chess.set(index, torPiece);
        }

    }

    public void switchTo_XOR(TorPiece torPiece) {

        int row = torPiece.getRow();
        int col = torPiece.getCol();
        String colour = torPiece.getColour();
        int index = chess.indexOf(torPiece);

        if (movementCounter % 2 == 0 && movementCounter != 0) {
            ChessPiece xorPiece = ChessPieceFactory.createPiece(ChessPieceType.XOR, row, col, colour);

            this.board[row][col] = xorPiece;
            chess.set(index, xorPiece);
        }
    }

    public boolean getRamIsReversed(RamPiece ramPiece){
        return ramPiece.getReverse();
    }

    public boolean isCorrectTurn(ChessPiece piece) {

        if (movementCounter % 2 == 0)
            return piece.getColour().equals("Blue");
        else
            return piece.getColour().equals("Red");

    }

    public int getMovementCounter() {
        return movementCounter;
    }

    public void setMovementCounter(int count){
        this.movementCounter = count;
    }

    public void resetMovementCounter(){
        this.movementCounter = 0;
    }

    public boolean isGameOver() {

        boolean blueSauExist = false;
        boolean redSauExist = false;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] != null && (board[i][j].getType() == ChessPieceType.SAU)) {

                    if (board[i][j].getColour().equals("Blue")) {
                        blueSauExist = true;
                    } else if (board[i][j].getColour().equals("Red"))
                        redSauExist = true;
                }

            }
        }
        return !(blueSauExist && redSauExist);
    }

    public ChessPiece getPieceAt(int row, int col) {

        return board[row][col];
    }

    public ArrayList<ChessPiece> getChessList() {
        return chess;
    }

    public ChessPiece[][] getPieceBoard() {
        return board;
    }

    public void initialiseBoard() {

        for (int col = 0; col < 5; col++)
            chess.add(ChessPieceFactory.createPiece(ChessPieceType.RAM, 6, col, "Blue"));

        chess.add(ChessPieceFactory.createPiece(ChessPieceType.XOR, 7, 0, "Blue"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.BIZ, 7, 1, "Blue"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.SAU, 7, 2, "Blue"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.BIZ, 7, 3, "Blue"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.TOR, 7, 4, "Blue"));

        for (int col = 0; col < 5; col++)
            chess.add(ChessPieceFactory.createPiece(ChessPieceType.RAM, 1, col, "Red"));

        chess.add(ChessPieceFactory.createPiece(ChessPieceType.TOR, 0, 0, "Red"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.BIZ, 0, 1, "Red"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.SAU, 0, 2, "Red"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.BIZ, 0, 3, "Red"));
        chess.add(ChessPieceFactory.createPiece(ChessPieceType.XOR, 0, 4, "Red"));

        for (int i = 0; i < chess.size(); i++) {
            int row = chess.get(i).getRow();
            int col = chess.get(i).getCol();

            board[row][col] = chess.get(i);
        }
    }

    public void clearBoard() {
        for (int row = 0; row < board.length; row++) {
            Arrays.fill(board[row], null);
        }
    }

    public void placePiece(ChessPiece piece) {
    
        int row = piece.getRow();
        int col = piece.getCol();
        board[row][col] = piece;
        chess.add(piece);
    }
}
