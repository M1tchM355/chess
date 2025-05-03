package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean hasMoved = pieceColor == ChessGame.TeamColor.WHITE && row != 2 || pieceColor == ChessGame.TeamColor.BLACK && row != 7;
        //White pawns
        if(pieceColor== ChessGame.TeamColor.WHITE) {
            ChessPiece piece1 = board.getPiece(new ChessPosition(row + 1, col));
            //Move forward
            if (piece1 == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                //Move forward 2 if it hasn't moved
                if (!hasMoved && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
                }
            }
            //Take diagonally
            if(col-1>0 && row+1<9 && board.getPiece(new ChessPosition(row+1,col-1))!=null && board.getPiece(new ChessPosition(row+1,col-1)).getTeamColor()!=pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(row+1,col-1),null));
            }
            if(col+1<9 && row+1<9 && board.getPiece(new ChessPosition(row+1,col+1))!=null && board.getPiece(new ChessPosition(row+1,col+1)).getTeamColor()!=pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(row+1,col+1),null));
            }
        //Black pawns
        } else {
            ChessPiece piece1 = board.getPiece(new ChessPosition(row - 1, col));
            //Move forward
            if (piece1 == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                //Move forward 2 if it hasn't moved
                if (!hasMoved && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
                }
            }
            //Take diagonally
            if(col-1>0 && row-1>0 && board.getPiece(new ChessPosition(row-1,col-1))!=null && board.getPiece(new ChessPosition(row-1,col-1)).getTeamColor()!=pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(row-1,col-1),null));
            }
            if(col+1<9 && row-1>0 && board.getPiece(new ChessPosition(row-1,col+1))!=null && board.getPiece(new ChessPosition(row-1,col+1)).getTeamColor()!=pieceColor){
                moves.add(new ChessMove(myPosition, new ChessPosition(row-1,col+1),null));
            }
        }
        return moves;
    }
}
