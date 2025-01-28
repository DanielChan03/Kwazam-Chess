package model.chess;

public class SauPiece extends ChessPiece {
    
    private int [][] possibleMoves = {
        {0,1},
        {0,-1},
        {1,0},
        {1,-1},
        {1,1},
        {-1,-1},
        {-1,0},
        {-1,1}
    };

    SauPiece(int row, int cols, String colour){
        super(row, cols, colour);
    }
    @Override
    public void move(int newRow, int newCol){
        setRow(newRow);
        setCols(newCol);
    }

    @Override
    public boolean isvalidMove(int newRow,int newCol){

        if(isOutOfBounds(newRow, newCol))
        return false;

        for(int i=0; i < possibleMoves.length; i++)
        {
            int validRow = getRow()+ possibleMoves[i][0];
            int validCols = getCol() + possibleMoves[i][1];

            if(validRow == newRow && validCols == newCol)
            return true;
        }

        return false;
    }

    @Override
    public ChessPieceType getType(){
        return ChessPieceType.SAU;
    }
    
}
