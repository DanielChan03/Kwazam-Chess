package model.chess;

public abstract class ChessPiece{
    
    private int row;
    private int col;
    private String chessColour;
    protected static final int MAX_ROW = 7;
    protected static final int MAX_COL = 4;

    ChessPiece(int row, int col, String colour){
        this.row = row;
        this.col = col;
        this.chessColour = colour;
    }

    public void setRow(int row){
        this.row = row;
    }

    public void setCols(int col){
        this.col = col;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public String getColour(){
        return chessColour;
    }

    public boolean isOutOfBounds(int newRow, int newCol){

        return newRow < 0 || newRow > MAX_ROW || newCol < 0 || newCol > MAX_COL; 
    }
    
    public abstract ChessPieceType getType();

    public abstract void move(int newRow, int newCol);

    public abstract boolean isvalidMove(int newRow,int newCol);

}