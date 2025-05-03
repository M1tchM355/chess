package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        Collection<ChessMove> moves = BishopMovesCalculator.pieceMoves(board,myPosition,pieceColor);
        moves.addAll(RookMovesCalculator.pieceMoves(board,myPosition,pieceColor));
        return moves;
    }
}
