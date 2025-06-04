package server;

import com.google.gson.Gson;
import request.*;
import result.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class);
    }

    public void clear(ClearRequest req) throws ResponseException {
        String path = "/db";
        this.makeRequest("DELETE", path, req, null);
    }

    public LogoutResult logout(LogoutRequest req) throws ResponseException {
        String path = "/session";
        return this.makeRequest("DELETE", path, req, LogoutResult.class);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ResponseException {
        String path = "/game";
        return this.makeRequest("GET", path, req, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException {
        String path = "/game";
        return this.makeRequest("POST", path, req, CreateGameResult.class);
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws ResponseException {
        String path = "/game";
        return this.makeRequest("PUT", path, req, JoinGameResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}

