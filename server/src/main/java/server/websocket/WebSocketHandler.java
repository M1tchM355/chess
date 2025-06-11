package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.ResponseException;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
    DAORecord daoRecord;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(DAORecord daoRecord) {
        this.daoRecord = daoRecord;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws ResponseException {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = getUsername(command.getAuthToken());

            connections.add(username, command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, new Gson().fromJson(message, ConnectCommand.class));
                case MAKE_MOVE -> makeMove(session, username, new Gson().fromJson(message, MakeMoveCommand.class));
                case LEAVE -> leave(session, username, new Gson().fromJson(message, LeaveCommand.class));
                case RESIGN -> resign(session, username, new Gson().fromJson(message, ResignCommand.class));
            }
        } catch (DataAccessException e) {
            throw new ResponseException(401, "Unauthorized");
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void connect(Session session, String username, ConnectCommand cmd) throws IOException, DataAccessException {
        LoadGameMessage loadGame = new LoadGameMessage(new Gson().toJson(daoRecord.gameDAO().getGame(cmd.getGameID()).game()));
        String game = new Gson().toJson(loadGame);
        session.getRemote().sendString(game);

        String message = String.format("%s joined the game as " + getRole(username, cmd.getGameID()), username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcastNotification(username, new Gson().toJson(notification));
    }

    private void makeMove(Session session, String username, MakeMoveCommand cmd) throws IOException {
        String message = String.format("%s made a move: " + cmd.getMove(), username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcastNotification(username, new Gson().toJson(notification));
    }

    private void leave(Session session, String username, LeaveCommand cmd) {

    }

    private void resign(Session session, String username, ResignCommand cmd) {

    }

    private String getUsername(String authToken) throws DataAccessException {
        return daoRecord.authDAO().getAuth(authToken).username();
    }

    private String getRole(String username, int gameID) throws DataAccessException {
        String whiteUser = daoRecord.gameDAO().getGame(gameID).whiteUsername();
        String blackUser = daoRecord.gameDAO().getGame(gameID).blackUsername();
        if (username.equals(whiteUser)) {
            return "white";
        } else if (username.equals(blackUser)) {
            return "black";
        }
        return "an observer";
    }
}