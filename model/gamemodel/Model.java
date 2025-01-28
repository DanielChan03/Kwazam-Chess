package model.gamemodel;

import java.util.ArrayList;
import java.util.List;

import model.board.ChessBoard;
import model.chess.TorPiece;
import model.chess.XorPiece;
import model.gamepersistence.GameDataManager;
import model.chess.ChessPiece;
import model.chess.ChessPieceType;
import model.chess.RamPiece;

// Model        
public class Model {

    private ChessBoard gameBoard;
    private boolean gameInitialised;
    private GameDataManager gamePersistence;

    public Model() {
        this.gameBoard = new ChessBoard();
        this.gameInitialised = false;
        this.gamePersistence = new GameDataManager();
    }

    public ChessBoard getGameBoard() {
        return gameBoard;
    }

    public String getPieceType(ChessPiece piece) {
        return piece.getType().name();
    }

    public ChessPiece getPieceAt(int row, int col) {
        return gameBoard.getPieceAt(row, col);
    }

    public boolean isValidMovePiece(ChessPiece selectedPiece, int row, int col) {
        return gameBoard.validMovePiece(selectedPiece, row, col);
    }

    public void movePiece(ChessPiece selectedPiece, int row, int col) {
        gameBoard.movePiece(selectedPiece, row, col);
    }

    public boolean isCorrectTurn(ChessPiece piece) {
        return gameBoard.isCorrectTurn(piece);
    }

    public int getMovementCounter() {
        return gameBoard.getMovementCounter();
    }

    public void initialiseBoard() {
        gameBoard.initialiseBoard();
    }

    public boolean isGameInitialised() {
        return gameInitialised;
    }

    public void saveGame(String gameName) {
        int movementCounter = gameBoard.getMovementCounter();
        gamePersistence.saveGame(gameName, movementCounter, gameBoard);
    }

    public void loadGame(String gameName) {
        gamePersistence.loadGame(gameName, this);
    }

    public List<String> listSavedGames() {
        return gamePersistence.listSavedGames();
    }

    // getting validMoves for every pieces
    public List<int[]> getValidMoves(ChessPiece piece) {
        List<int[]> validMoves = new ArrayList<>();

        if (piece == null) {
            return validMoves;
        }

        // Custom movement for XorPiece
        if (piece.getType() == ChessPieceType.XOR) {
            validMoves.addAll(getXorValidMoves(piece));
        }
        // Custom movement for TorPiece
        else if (piece.getType() == ChessPieceType.TOR) {
            validMoves.addAll(getTorValidMoves(piece));
        } else {
            for (int row = 0; row < gameBoard.getPieceBoard().length; row++) {
                for (int col = 0; col < gameBoard.getPieceBoard()[row].length; col++) {
                    if (gameBoard.validMovePiece(piece, row, col)) {
                        validMoves.add(new int[] { row, col });
                    }
                }
            }
        }
        return validMoves;
    }

    // allow view to access Xor possible moves in advance
    private List<int[]> getXorValidMoves(ChessPiece piece) {
        List<int[]> moves = new ArrayList<>();
        int[] directions = { -1, 1 };
        for (int i : directions) {
            for (int j : directions) {
                for (int step = 1; step < 8; step++) {
                    int newRow = piece.getRow() + step * i;
                    int newCol = piece.getCol() + step * j;
                    if (gameBoard.validMovePiece(piece, newRow, newCol)) {
                        moves.add(new int[] { newRow, newCol });
                    } else {
                        break; // Stop if we go out of bounds
                    }
                }
            }
        }
        return moves;
    }
    
    // allow view to access Tor possible moves in advance
    private List<int[]> getTorValidMoves(ChessPiece piece) {
        List<int[]> moves = new ArrayList<>();
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } }; // Vertical and horizontal directions

        for (int[] dir : directions) {
            for (int step = 1; step < 8; step++) {
                int newRow = piece.getRow() + step * dir[0];
                int newCol = piece.getCol() + step * dir[1];

                if (gameBoard.getPieceBoard()[piece.getRow()][piece.getCol()].isOutOfBounds(newRow, newCol))
                    break;

                ChessPiece targetPiece = gameBoard.getPieceAt(newRow, newCol);
                if (targetPiece == null) {
                    moves.add(new int[] { newRow, newCol }); // Empty square
                } else {
                    if (!targetPiece.getColour().equals(piece.getColour())) {
                        moves.add(new int[] { newRow, newCol }); // Opponent's piece
                    }
                    break; // Stop further movement
                }
            }
        }
        return moves;
    }

    // switching Xor and Tor piece after 2 turns(4 movement counts)
    public void switchOR_Piece() {
        ChessPiece[][] board = gameBoard.getPieceBoard();

        if (gameBoard.getMovementCounter() % 4 == 0) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    ChessPiece piece = gameBoard.getPieceAt(row, col);
                    if (piece != null) {
                        if (piece.getType() == ChessPieceType.XOR) {
                            gameBoard.switchTo_TOR((XorPiece) gameBoard.getPieceAt(row, col));
                        } else if (piece.getType() == ChessPieceType.TOR) {
                            gameBoard.switchTo_XOR((TorPiece) gameBoard.getPieceAt(row, col));
                        }
                    }
                }
            }
        }
    }

    // Tracking ram reverse sate 
    public boolean getPieceReversed(ChessPiece piece) {
        // only ram pieces will be reversed after reached the edge while other pieces always return false
        return (piece.getType() == ChessPieceType.RAM) ? gameBoard.getRamIsReversed((RamPiece) piece) : false;
    }

    public void setGameInitialised(boolean initialise) {
        this.gameInitialised = initialise;
    }

    public void setGameBoard(ChessBoard newBoard) {
        this.gameBoard = newBoard;
    }

    public boolean isGameOver() {
        return gameBoard.isGameOver();
    }

    public void resetGame() {
        gameBoard.getChessList().clear(); // clear board
        gameBoard.clearBoard(); // reset position
        gameBoard.resetMovementCounter();
        this.gameInitialised = false;
    }
}