package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //Diagonally up left
        int newRow = row+1;
        int newCol = col-1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow++;
            newCol--;
        }
        //Diagonally up right
        newRow = row+1;
        newCol = col+1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow++;
            newCol++;
        }
        //Diagonally down left
        newRow = row-1;
        newCol = col-1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow--;
            newCol--;
        }
        //Diagonally down right
        newRow = row-1;
        newCol = col+1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow--;
            newCol++;
        }
        return moves;
    }
}
