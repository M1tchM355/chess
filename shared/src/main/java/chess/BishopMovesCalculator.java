package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        //Diagonally up left
        iterate(moves,1,-1,board,myPosition,pieceColor);
        //Diagonally up right
        iterate(moves,1,1,board,myPosition,pieceColor);
        //Diagonally down left
        iterate(moves,-1,-1,board,myPosition,pieceColor);
        //Diagonally down right
        iterate(moves,-1,1,board,myPosition,pieceColor);
        return moves;
    }
}
