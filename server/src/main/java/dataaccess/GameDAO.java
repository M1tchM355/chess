package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public int createGame(String gameName);
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames();
    public GameData updateGame(int gameID, ChessGame.TeamColor playerColor, String username) throws DataAccessException;
}
