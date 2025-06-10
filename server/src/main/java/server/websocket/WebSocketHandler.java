package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
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
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

            String username = getUsername(command.getAuthToken());

            connections.add(username, command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leave(session, username, (LeaveCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void connect(Session session, String username, ConnectCommand cmd) throws IOException, DataAccessException {
        String game = new Gson().toJson(daoRecord.gameDAO().getGame(cmd.getGameID()).game());
        session.getRemote().sendString(game);

        String message = String.format("%s joined the game as " + cmd.getRole(), username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
    }

    private void makeMove(Session session, String username, MakeMoveCommand cmd) throws IOException {
        String message = String.format("%s made a move: " + cmd.getMove(), username);
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcast(username, notification);
    }

    private void leave(Session session, String username, LeaveCommand cmd) {

    }

    private void resign(Session session, String username, ResignCommand cmd) {

    }

    private String getUsername(String authToken) throws DataAccessException {
        return daoRecord.authDAO().getAuth(authToken).username();
    }
}