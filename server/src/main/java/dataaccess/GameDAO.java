package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    public int createGame(String gameName);
    public GameData getGame(int gameID) throws DataAccessException;
    public Collection<GameData> listGames();
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException;
    public void clearGames();
}
