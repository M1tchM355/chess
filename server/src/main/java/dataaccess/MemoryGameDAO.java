package dataaccess;

import chess.ChessGame;
import model.GameData;
import service.AlreadyTakenException;
import service.BadRequestException;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private int nextID = 1;

    @Override
    public int createGame(String gameName) throws DataAccessException {
        games.put(nextID, new GameData(nextID,null,null,gameName,new ChessGame()));
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
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException, AlreadyTakenException {
        GameData oldGame = games.get(gameID);
        if(oldGame==null){
            throw new DataAccessException("Invalid gameID");
        }
        GameData newGame;
        if(playerColor.equals("WHITE")){
            if(oldGame.whiteUsername() != null){
                throw new AlreadyTakenException("Color already taken");
            }
            newGame = new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
        } else if(playerColor.equals("BLACK")){
            if(oldGame.blackUsername() != null){
                throw new AlreadyTakenException("Color already taken");
            }
            newGame = new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
        } else {
            throw new BadRequestException("Invalid player color");
        }
        games.replace(gameID,newGame);
    }

    @Override
    public void clearGames() throws DataAccessException {
        games.clear();
    }
}
