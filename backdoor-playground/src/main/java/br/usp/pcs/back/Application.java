package br.usp.pcs.back;

import static br.usp.pcs.back.Configuration.getErrorHandler;
import static br.usp.pcs.back.Configuration.getObjectMapper;
import static br.usp.pcs.back.Configuration.getUserDatasource;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import br.usp.pcs.back.api.user.BackdoorController;
import br.usp.pcs.back.api.user.LoginController;
import br.usp.pcs.back.api.user.SignUpController;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        SignUpController signUpController = new SignUpController(getUserDatasource(), getObjectMapper(),
                getErrorHandler());
        LoginController loginController = new LoginController(getUserDatasource(), getObjectMapper(),
                getErrorHandler());

        BackdoorController backdoorController = new BackdoorController(getUserDatasource(), getObjectMapper(),
                getErrorHandler());

        server.createContext("/api/user/signup", signUpController::handle);
        server.createContext("/api/user/login", loginController::handle);
        server.createContext("/api/backdoor", backdoorController::handle);

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
