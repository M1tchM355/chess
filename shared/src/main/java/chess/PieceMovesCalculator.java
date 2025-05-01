package chess;

import java.util.Collection;

public class PieceMovesCalculator {

    public PieceMovesCalculator(){}

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
        if (type.equals(ChessPiece.PieceType.KING)) {
            return KingMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
            return QueenMovesCalculator.pieceMoves(board, myPosition);
        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
            return BishopMovesCalculator.pieceMoves(board, myPosition);
        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
            return KnightMovesCalculator.pieceMoves(board, myPosition);
        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
            return RookMovesCalculator.pieceMoves(board, myPosition);
        } else {
            return PawnMovesCalculator.pieceMoves(board, myPosition);
        }
    }
}
