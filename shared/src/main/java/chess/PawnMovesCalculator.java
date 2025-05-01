package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        boolean hasMoved = pieceColor == ChessGame.TeamColor.WHITE && row != 2 || pieceColor == ChessGame.TeamColor.BLACK && row != 7;
        if(pieceColor== ChessGame.TeamColor.WHITE) {
            ChessPiece piece1 = board.getPiece(new ChessPosition(row + 1, col));
            ChessPiece piece2 = board.getPiece(new ChessPosition(row + 2, col));
            //Move forward
            if (piece1 == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row + 1, col), null));
                //Move forward 2 if it hasn't moved
                if (!hasMoved && piece2 == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row + 2, col), null));
                }
            }
            //Take diagonally
        } else {
            ChessPiece piece1 = board.getPiece(new ChessPosition(row - 1, col));
            ChessPiece piece2 = board.getPiece(new ChessPosition(row - 2, col));
            //Move forward
            if (piece1 == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(row - 1, col), null));
                //Move forward 2 if it hasn't moved
                if (!hasMoved && piece2 == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(row - 2, col), null));
                }
            }
            //Take diagonally
        }
        return moves;
    }
}
