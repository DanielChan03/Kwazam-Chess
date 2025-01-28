package model.chess;

public class RamPiece extends ChessPiece {

    private boolean reverse = false;

    RamPiece(int row, int col, String colour) {
        super(row, col, colour);
    }

    public boolean getReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
    }

    @Override
    public void move(int newRow, int newCol) {
        setRow(newRow);
        setCols(newCol);
        
        updateReverseDirection(newRow);
    }

    private void updateReverseDirection(int newRow) {
        if (getColour().equals("Blue")) {
            if (!reverse && newRow == 0) reverse = true;
            else if (reverse && newRow == MAX_ROW) reverse = false;
        } else if (getColour().equals("Red")) {
            if (!reverse && newRow == MAX_ROW) reverse = true;
            else if (reverse && newRow == 0) reverse = false;
        }
    }

    @Override
    public boolean isvalidMove(int newRow, int newCol) {
        if (newCol != getCol() || isOutOfBounds(newRow, newCol)) 
        return false;

        if (getColour().equals("Blue")) {
            return (!reverse && newRow == getRow() - 1) || (reverse && newRow == getRow() + 1);
        } else if (getColour().equals("Red")) {
            return (!reverse && newRow == getRow() + 1) || (reverse && newRow == getRow() - 1);
        }

        return false;
    }

    @Override
    public ChessPieceType getType() {
        return ChessPieceType.RAM;
    }

    public boolean reverseDirection() {
        if (getColour().equals("Blue")) {
            return getRow() == 0 || (getRow() == MAX_ROW && reverse);
        } else if (getColour().equals("Red")) {
            return getRow() == MAX_ROW || (getRow() == 0 && reverse);
        }
        return false;
    }
}