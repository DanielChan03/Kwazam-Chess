package model.chess;

// Factory Method Design Pattern: Provides a method for creating different types of ChessPiece
// objects based on the specified ChessPieceType, encapsulating object creation logic.

public class ChessPieceFactory {
    
    public static ChessPiece createPiece(ChessPieceType type, int row, int col, String colour) {
        switch (type) {
            case RAM:
                return new RamPiece(row , col, colour);
            case BIZ:
                return new BizPiece(row, col, colour);
            case TOR:
                return new TorPiece(row, col, colour);
            case XOR:
                return new XorPiece(row, col, colour);
            case SAU:
                return new SauPiece(row, col, colour);
            default:
                return null;
        }
    }
}