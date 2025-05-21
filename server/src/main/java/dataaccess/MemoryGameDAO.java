package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public int createGame(String gameName) {
        games.put(nextID, new GameData(nextID,"","",gameName,new ChessGame()));
        nextID++;
        return nextID-1;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData game = games.get(gameID);
        if(game==null){
            throw new DataAccessException("Invalid gameID");
        }
        return game;
    }

    @Override
    public Collection<GameData> listGames() {
        return games.values();
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData oldGame = games.get(gameID);
        if(oldGame==null){
            throw new DataAccessException("Invalid gameID");
        }
        GameData newGame;
        if(playerColor.equals("WHITE")){
            newGame = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else if(playerColor.equals("BLACK")){
            newGame = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        } else {
            throw new DataAccessException("Invalid player color");
        }
        games.replace(gameID,newGame);
    }

    @Override
    public void clearGames() {
        games.clear();
    }
}
