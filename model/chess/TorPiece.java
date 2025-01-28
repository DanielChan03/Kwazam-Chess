package model.chess;

public class TorPiece extends ChessPiece {

    TorPiece(int row, int cols, String colour) {
        super(row, cols, colour);
       
    }

    @Override
    public void move(int newRow, int newCol) {

        setRow(newRow);
        setCols(newCol);
    }

    @Override
    public ChessPieceType getType(){
        return ChessPieceType.TOR;
    }

    @Override
    public boolean isvalidMove(int newRow, int newCol) {

        if (isOutOfBounds(newRow, newCol))
            return false;
        else 
            return (newRow == getRow() || newCol == getCol());
    
    }
    
}
