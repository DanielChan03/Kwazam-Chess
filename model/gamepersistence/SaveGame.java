package model.gamepersistence;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.board.ChessBoard;
import model.chess.ChessPiece;
import model.chess.ChessPieceType;
import model.chess.RamPiece;

public class SaveGame {
    private String saveFile;

    SaveGame(String saveFile) {
        this.saveFile = saveFile;
    }

    public void saveGame(int movementCounter, ChessBoard board) {

        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = currentDateTime.format(formatter);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {

            writer.write("Data saved: " + formattedDateTime);
            writer.write("\nPlayer 1: Blue");
            writer.write("\nPlayer 2: Red");

            String playerTurn = (movementCounter % 2 == 0) ? "Player 1" : "Player 2";

            writer.write("\nCurrent turn: " + playerTurn);
            writer.write("\nMovement Count: " + movementCounter);

            writer.write("\n\nPieces:");
            writer.newLine();

            int totalRedPieces = 0;
            int totalBluePieces = 0;

            ChessPiece[][] pieceBoard = board.getPieceBoard();
            for (int row = 0; row < pieceBoard.length; row++) {
                for (int col = 0; col < pieceBoard[row].length; col++) {
                    ChessPiece piece = pieceBoard[row][col];

                    if (piece != null) {
                        String pieceDescription = convertPieceToString(piece);
                        writer.write(pieceDescription);
                        writer.newLine();

                        if (piece.getColour().equals("Blue")) {
                            totalBluePieces++;
                        } else {
                            totalRedPieces++;
                        }
                    }
                }
            }
            writer.newLine();
            writer.write(printLogicBoard(board));

            writer.write("Summary: ");
            writer.write("\n- Blue Pieces: " + totalBluePieces);
            writer.write("\n- Red Pieces: " + totalRedPieces);

            System.out.println("Game saved to " + saveFile);
        } catch (IOException e) {
            System.out.println("Error saving the game: " + e.getMessage());
        }
    }

    private String convertPieceToString(ChessPiece piece) {
        int row = piece.getRow();
        int col = piece.getCol();
        String position = "Row: " + row + " Col: " + col;
        if (piece.getType() == ChessPieceType.RAM) {
            RamPiece ramPiece = (RamPiece) piece;

            String reverseDirection = ramPiece.getColour().equals("Blue") ? ((ramPiece.getReverse()) ? "Down" : "Up")
                    : ((ramPiece.getReverse()) ? "Up" : "Down");
            return position + " " + ramPiece.getType() + ", " + ramPiece.getColour() + ", " + reverseDirection;
        }
        return position + " " + piece.getType() + ", " + piece.getColour();
    }

    public String printLogicBoard(ChessBoard board) {
        StringBuilder sb = new StringBuilder(); // Use StringBuilder to construct the board string

        sb.append("Current Board: \n");
        ChessPiece[][] pieceBoard = board.getPieceBoard();
        for (int row = 0; row < pieceBoard.length; row++) {
            for (int col = 0; col < pieceBoard[row].length; col++) {
                ChessPiece piece = board.getPieceAt(row, col);
                if (piece == null) {
                    sb.append("[   ]"); // Append empty space for null pieces
                } else {
                    String color = piece.getColour().equals("Red") ? "R" : "B";
                    String type = piece.getClass().getSimpleName().substring(0, 1).toUpperCase();
                    sb.append("[").append(color).append("_").append(type).append("]"); // Append piece details
                }
            }
            sb.append("\n"); // Add a newline after each row
        }
        sb.append("\n"); // Add an extra newline at the end
        return sb.toString(); // Return the constructed string
    }
}