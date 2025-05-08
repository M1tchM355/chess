package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        //left
        iterate(moves,0,-1,board,myPosition,pieceColor);
        //right
        iterate(moves,0,1,board,myPosition,pieceColor);
        //down
        iterate(moves,-1,0,board,myPosition,pieceColor);
        //up
        iterate(moves,1,0,board,myPosition,pieceColor);
        return moves;
    }
}
