package model.gamepersistence;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import model.board.ChessBoard;
import model.chess.ChessPieceFactory;
import model.chess.ChessPieceType;
import model.chess.RamPiece;
import model.gamemodel.Model;
import model.chess.ChessPiece;

public class LoadGame {

    private String loadFile;

    LoadGame(String loadFile) {
        this.loadFile = loadFile;
    }

    public void loadGame(Model model) {
        try (BufferedReader reader = new BufferedReader(new FileReader(loadFile))) {

            String line;
            int loadedMovementCounter = 0;

             // Read lines until we find "Movement Count:"
             while ((line = reader.readLine()) != null) {
                if (line.startsWith("Movement Count: ")) {
                    // Extract the movement count
                    loadedMovementCounter = Integer.parseInt(line.split(":")[1].trim());
                    break; // Exit the loop once we find the movement count
                }
            }

            // Skip the "Board:" line
            line = reader.readLine(); // This should be "Board:"

            // Read and reconstruct the board state
            ChessBoard newBoard = new ChessBoard();
            newBoard.setMovementCounter(loadedMovementCounter);

            while ((line = reader.readLine()) != null) {
                
                // Stop reading if "Current Board:" is encountered
                if (line.startsWith("Current Board:")) {
                    break;
                }
            
                ChessPiece piece = parsePieceFromString(line);
                if (piece != null) {
                    newBoard.placePiece(piece); 
                }
            }

            // Update the model
            model.setGameBoard(newBoard);

            System.out.println("Game loaded from " + loadFile);
        } catch (IOException e) {
            System.out.println("Error loading the game: " + e.getMessage());
        }
    }

    private ChessPiece parsePieceFromString(String pieceDescription) {
        String[] parts = pieceDescription.split(" ");
        if (parts.length >= 6) {
            int row = Integer.parseInt(parts[1]);
            int col = Integer.parseInt(parts[3]);
            String type = parts[4].replace(",", "");
            String colour = parts[5].replace(",","");

            ChessPieceType pieceType = ChessPieceType.valueOf(type.toUpperCase());
            ChessPiece piece = ChessPieceFactory.createPiece(pieceType, row, col, colour);

             // Handle RamPiece reverse state
             if (piece.getType() == ChessPieceType.RAM && parts.length >= 6) {
                String reverseDirection = parts[6].replace(",", "");
                boolean isReversed = determineReverseState(colour, reverseDirection);
                RamPiece ramPiece = (RamPiece)piece;
                ramPiece.setReverse(isReversed);
            }

            return piece;
        }
        return null;
    }


    private boolean determineReverseState(String colour, String reverseDirection) {
        // Logic to determine reverse state based on colour and reverseDirection
        return colour.equals("Blue") ? ((reverseDirection.equals("Down")) ? true : false)
        : ((reverseDirection.equals("Up")) ? true : false);
    }
}