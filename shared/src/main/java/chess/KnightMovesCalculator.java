package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator{
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor pieceColor) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        ChessPosition[] possibilities ={new ChessPosition(row+1,col-2),
                                        new ChessPosition(row+2,col-1),
                                        new ChessPosition(row+2,col+1),
                                        new ChessPosition(row+1,col+2),
                                        new ChessPosition(row-2,col-1),
                                        new ChessPosition(row-1,col-2),
                                        new ChessPosition(row-2,col+1),
                                        new ChessPosition(row-1,col+2)};

        for(ChessPosition pos : possibilities){
            row = pos.getRow();
            col = pos.getColumn();
            if(isInBounds(row,col) && (board.getPiece(pos)==null
                    || board.getPiece(pos).getTeamColor()!=pieceColor)){
                moves.add(new ChessMove(myPosition,pos,null));
            }
        }

        return moves;
    }
}
