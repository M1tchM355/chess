package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //left
        int newRow = row;
        int newCol = col-1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newCol--;
        }
        if(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor()!=pieceColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
        //right
        newRow = row;
        newCol = col+1;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newCol++;
        }
        if(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor()!=pieceColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
        //down
        newRow = row-1;
        newCol = col;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow--;
        }
        if(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor()!=pieceColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
        //up
        newRow = row+1;
        newCol = col;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow++;
        }
        if(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor()!=pieceColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
        return moves;
    }
}
