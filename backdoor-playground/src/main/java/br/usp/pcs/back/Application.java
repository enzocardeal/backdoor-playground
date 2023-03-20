package br.usp.pcs.back;

import br.usp.pcs.back.api.user.UserController;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static br.usp.pcs.back.Configuration.getUserDatasource;
import static br.usp.pcs.back.Configuration.getErrorHandler;
import static br.usp.pcs.back.Configuration.getObjectMapper;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        UserController userController = new UserController(getUserDatasource(), getObjectMapper(),
                getErrorHandler());
        server.createContext("/api/user", userController::handle);

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
