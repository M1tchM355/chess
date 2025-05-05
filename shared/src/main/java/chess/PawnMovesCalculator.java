package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean hasMoved = pieceColor == ChessGame.TeamColor.WHITE && row != 2 || pieceColor == ChessGame.TeamColor.BLACK && row != 7;
        int direction;
        if(pieceColor==ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }
        ChessPiece piece1 = board.getPiece(new ChessPosition(row + direction, col));
        //Move forward
        if (piece1 == null) {
            //Promote
            if(row+direction==1 || row+direction==8){
                promote(myPosition,row,col,direction,moves);
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col), null));
            }
            //Move forward 2 if it hasn't moved
            if (!hasMoved && board.getPiece(new ChessPosition(row + 2*direction, col)) == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 2*direction, col), null));
            }
        }
        //Take diagonally
        if(col-1>0 && row+direction<9 && board.getPiece(new ChessPosition(row+direction,col-1))!=null &&
                board.getPiece(new ChessPosition(row+direction,col-1)).getTeamColor()!=pieceColor){
            //Promote
            if(row+direction==1 || row+direction==8){
                promote(myPosition,row,col-1,direction,moves);
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col - 1), null));
            }
        }
        if(col+1<9 && row+direction<9 && board.getPiece(new ChessPosition(row+direction,col+1))!=null &&
                board.getPiece(new ChessPosition(row+direction,col+1)).getTeamColor()!=pieceColor){
            //Promote
            if(row+direction==1 || row+direction==8){
                promote(myPosition,row,col,direction,moves);
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + direction, col + 1), null));
            }
        }

        return moves;
    }

    private static void promote(ChessPosition myPos, int row, int col, int dir, Collection<ChessMove> moves){
        for(ChessPiece.PieceType type : ChessPiece.PieceType.values()){
            if(type!= ChessPiece.PieceType.PAWN && type!= ChessPiece.PieceType.KING){
                moves.add(new ChessMove(myPos, new ChessPosition(row + dir, col), type));
            }
        }
    }
}
