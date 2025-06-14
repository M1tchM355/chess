package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {
    DAORecord daoRecord;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(DAORecord daoRecord) {
        this.daoRecord = daoRecord;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
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
        } catch (Exception e) {
            sendError(session, "Unauthorized");
        }
    }

    private void connect(Session session, String username, ConnectCommand cmd) throws IOException {
        try {
            sendLoadGame(session, cmd, false);

            String message = String.format("%s joined the game as " + getRole(username, cmd.getGameID()), username);
            sendNotification(message, username, cmd.getGameID());
        } catch (Exception e) {
            sendError(session, "Error connecting");
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand cmd) throws IOException {
        try {
            ChessMove move = cmd.getMove();
            int gameID = cmd.getGameID();

            String role = getRole(username, gameID);
            ChessGame.TeamColor color;
            if (role.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (role.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new InvalidMoveException("Observer can't make moves");
            }
            var turn = daoRecord.gameDAO().getGame(gameID).game().getTeamTurn();
            if (!(turn == color)) {
                throw new InvalidMoveException("Not your turn");
            }

            updateGame(move, gameID);

            sendLoadGame(session, cmd, true);

            String message = String.format("%s made a move: " + cmd.getMove(), username);
            sendNotification(message, username, gameID);

            checkGame(session, gameID);
        } catch (InvalidMoveException e) {
            sendError(session, e.getMessage());
        } catch (DataAccessException e) {
            sendError(session, "Error updating move");
        }
    }

    private void leave(Session session, String username, LeaveCommand cmd) throws IOException, DataAccessException {
        int gameID = cmd.getGameID();
        removePlayer(username, gameID);

        sendNotification(username + " left the game", username, gameID);
    }

    private void resign(Session session, String username, ResignCommand cmd) throws IOException, DataAccessException {
        try {
            int gameID = cmd.getGameID();
            String role = getRole(username, gameID);

            if (role.equals("an observer")) {
                sendError(session, "An observer can't resign");
            } else {
                resignGame(username, gameID);

                sendNotification(username + " resigned the game", null, gameID);
            }
        } catch (DataAccessException e) {
            sendError(session, "Can't resign");
        }
    }

    private String getUsername(String authToken) throws DataAccessException, NullPointerException {
        return daoRecord.authDAO().getAuth(authToken).username();
    }

    private void sendError(Session session, String errorMessage) throws IOException {
        ErrorMessage err = new ErrorMessage(errorMessage);
        session.getRemote().sendString(new Gson().toJson(err));
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

    private void updateGame(ChessMove move, int gameID) throws DataAccessException, InvalidMoveException {
        ChessGame game = daoRecord.gameDAO().getGame(gameID).game();
        game.makeMove(move);
        daoRecord.gameDAO().updateGame(gameID,null, null, game);
    }

    private void sendLoadGame(Session session, UserGameCommand cmd, boolean toEveryone) throws DataAccessException, IOException {
        LoadGameMessage loadGame = new LoadGameMessage(new Gson().toJson(daoRecord.gameDAO().getGame(cmd.getGameID()).game()));
        if (toEveryone) {
            connections.broadcastNotification(null, new Gson().toJson(loadGame), cmd.getGameID());
        } else {
            String game = new Gson().toJson(loadGame);
            session.getRemote().sendString(game);
        }
    }

    private void sendNotification(String message, String excludedUsername, int gameID) throws IOException {
        NotificationMessage notification = new NotificationMessage(message);
        connections.broadcastNotification(excludedUsername, new Gson().toJson(notification), gameID);
    }

    private void checkGame(Session session, int gameID) throws DataAccessException, IOException {
        ChessGame game = daoRecord.gameDAO().getGame(gameID).game();
        var turn = game.getTeamTurn();
        boolean isInCheck = game.isInCheck(turn);
        boolean isInCheckmate = game.isInCheckmate(turn);
        boolean isInStalemate = game.isInStalemate(turn);
        if (isInCheckmate) {
            sendNotification(turn.name() + " is in checkmate.", null, gameID);
        } else if (isInCheck) {
            sendNotification(turn.name() + " is in check", null, gameID);
        } else if (isInStalemate) {
            sendNotification("stalemate", null, gameID);
        }
    }

    private void removePlayer(String username, int gameID) throws DataAccessException {
        GameDAO gameDAO = daoRecord.gameDAO();
        String role = getRole(username, gameID);
        if (role.equals("white")) {
            gameDAO.updateGame(gameID, "WHITE", null, null);
        } else if (role.equals("black")) {
            gameDAO.updateGame(gameID, "BLACK", null, null);
        }
        connections.remove(username);
    }

    private void resignGame(String username, int gameID) throws DataAccessException {
        ChessGame game = daoRecord.gameDAO().getGame(gameID).game();
        if (game.isOver()) {
            throw new DataAccessException("Game is already over");
        }
        game.setOver(true);

        daoRecord.gameDAO().updateGame(gameID,null, null, game);
    }
}