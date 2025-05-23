package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int i = row - 1;i <= row + 1;i++){
            for(int j = col - 1;j <= col + 1;j++) {
                if (isInBounds(i,j)){
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if(piece==null || piece.getTeamColor()!=pieceColor) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                    }
                }
            }
        }
        return moves;
    }

}
