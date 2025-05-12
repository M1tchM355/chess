package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor turn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();

    public ChessGame() {
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return turn == chessGame.turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(turn, board);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece==null){
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> invalidMoves = new ArrayList<>();
        for(ChessMove move : moves){
            ChessBoard newBoard = board.clone();
            if(move.getPromotionPiece()==null){
                newBoard.addPiece(move.getEndPosition(),piece);
            } else {
                newBoard.addPiece(move.getEndPosition(),new ChessPiece(piece.getTeamColor(),move.getPromotionPiece()));
            }
            newBoard.addPiece(move.getStartPosition(),null);
            if(isInCheckHelper(piece.getTeamColor(), newBoard)){
                System.out.println(move);
                invalidMoves.add(move);
            }
        }
        moves.removeAll(invalidMoves);
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promote = move.getPromotionPiece();
        Collection<ChessMove> validMoves = validMoves(start);
        ChessPiece piece = board.getPiece(start);
        if(validMoves==null){
            throw new InvalidMoveException("No valid moves");
        }
        if(turn!=piece.getTeamColor()){
            throw new InvalidMoveException("Not your turn");
        }
        if(!validMoves.contains(move)){
            throw new InvalidMoveException("Invalid Move");
        }
        if(promote==null){
            board.addPiece(end,piece);
        } else {
            board.addPiece(end,new ChessPiece(piece.getTeamColor(),promote));
        }
        board.addPiece(start,null);
        if(turn==TeamColor.WHITE){
            turn = TeamColor.BLACK;
        } else {
            turn = TeamColor.WHITE;
        }
    }

    private ChessPosition findKing(TeamColor color, ChessBoard board){
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if(piece==null){
                    continue;
                }
                if(piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor()==color){
                    return pos;
                }
            }
        }
        //Shouldn't ever return null
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckHelper(teamColor, board);
    }

    private boolean isInCheckHelper(TeamColor teamColor, ChessBoard newBoard) {
        ChessPosition kingPos = findKing(teamColor, newBoard);
        for(int i=1;i<9;i++){
            for(int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = newBoard.getPiece(pos);
                if(piece==null){
                    continue;
                }
                Collection<ChessMove> moves = piece.pieceMoves(newBoard, pos);
                if(moves.contains(new ChessMove(pos,kingPos,null)) ||
                        moves.contains(new ChessMove(pos,kingPos, ChessPiece.PieceType.QUEEN))){
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }

        return hasMoves(teamColor);
    }

    private boolean hasMoves(TeamColor color){
        for(int i=1;i<9;i++){
            for(int j=1;j<9;j++){
                ChessPosition pos = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(pos);
                if(piece==null || piece.getTeamColor()!=color){
                    continue;
                }
                Collection<ChessMove> moves = validMoves(pos);
                if(!moves.isEmpty()){
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)){
            return false;
        }

        return hasMoves(teamColor);
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
