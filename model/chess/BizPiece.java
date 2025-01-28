package model.chess;

public class BizPiece extends ChessPiece {
    
    private int [][] possibleMoves = {
        {-1,-2},
        {-1,2},
        {1,-2},
        {1,2},
        {-2,-1},
        {-2,1},
        {2,-1},
        {2,1}
    };

    BizPiece(int x, int y, String colour){
        super(x,y,colour);

    }

    @Override
    public void move(int newRow,int newCol){

        setRow(newRow);
        setCols(newCol);
    }

    @Override 
    public boolean isvalidMove(int newRow, int newCol){

        if(isOutOfBounds(newRow, newCol))
        return false;

        for(int i = 0 ; i < possibleMoves.length; i++)
        {
            int validRow = getRow() + possibleMoves[i][0];
            int validCol = getCol() + possibleMoves[i][1]; 

            if(validRow == newRow && validCol == newCol)
            return true;
        }

        return false;

    }

    @Override
    public ChessPieceType getType(){
        return ChessPieceType.BIZ;
    }

}
