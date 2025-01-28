package model.chess;

public class XorPiece extends ChessPiece {

    XorPiece(int row, int cols, String colour){
        super(row, cols, colour);
    }
    
    @Override
    public void move(int newRow, int newCol){
        setRow(newRow);
        setCols(newCol);
    }

    @Override
    public ChessPieceType getType(){
        return ChessPieceType.XOR;
    }
    
    @Override
    public boolean isvalidMove(int newRow, int newCol){

        if(isOutOfBounds(newRow, newCol))
        return false;

            int currentRow = getRow();
            int currentCols = getCol();
        

        if(Math.abs(newRow - currentRow) == Math.abs(newCol - currentCols))
            return true;
        
        
        return false;
    }   
}

