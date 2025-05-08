package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition,
                                                   ChessPiece.PieceType type, ChessGame.TeamColor pieceColor) {
        if (type.equals(ChessPiece.PieceType.KING)) {
            return KingMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else if (type.equals(ChessPiece.PieceType.QUEEN)) {
            return QueenMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else if (type.equals(ChessPiece.PieceType.BISHOP)) {
            return BishopMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else if (type.equals(ChessPiece.PieceType.KNIGHT)) {
            return KnightMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else if (type.equals(ChessPiece.PieceType.ROOK)) {
            return RookMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        } else {
            return PawnMovesCalculator.pieceMoves(board, myPosition, pieceColor);
        }
    }

    public static boolean isInBounds(int row, int col){
        return row>0 && row<9 && col>0 && col<9;
    }

    public static void iterate(ArrayList<ChessMove> moves, int rowChange, int colChange,
                                               ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor){
        int newRow = myPosition.getRow()+rowChange;
        int newCol = myPosition.getColumn()+colChange;
        while(newRow>0 && newRow<9 && newCol>0 && newCol<9 && board.getPiece(new ChessPosition(newRow,newCol))==null){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            newRow+=rowChange;
            newCol+=colChange;
        }
        if(newRow>0 && newRow<9 && newCol>0 && newCol<9 &&
                board.getPiece(new ChessPosition(newRow,newCol)).getTeamColor()!=pieceColor){
            moves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
        }
    }
}
