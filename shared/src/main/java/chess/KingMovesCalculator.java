package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator {

    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for(int i = row - 1;i <= row + 1;i++){
            for(int j = col - 1;j <= col + 1;j++) {
                if (i>0 && i<9 && j>0 && j<9 && board.getPiece(new ChessPosition(i,j))==null){
                    moves.add(new ChessMove(myPosition,new ChessPosition(i,j),null));
                }
            }
        }
        return moves;
    }

}
