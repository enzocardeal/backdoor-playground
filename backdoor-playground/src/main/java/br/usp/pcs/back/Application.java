package br.usp.pcs.back;

import br.usp.pcs.back.api.user.RegistrationController;
import com.sun.net.httpserver.BasicAuthenticator;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

import static br.usp.pcs.back.Configuration.getUserDatasource;
import static br.usp.pcs.back.Configuration.getErrorHandler;
import static br.usp.pcs.back.Configuration.getObjectMapper;
import static br.usp.pcs.back.utils.ApiUtils.splitQuery;

public class Application {

    public static void main(String[] args) throws IOException {
        int serverPort = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(serverPort), 0);

        RegistrationController registrationController = new RegistrationController(getUserDatasource(), getObjectMapper(),
                getErrorHandler());
        server.createContext("/api/users/register", registrationController::handle);

//        HttpContext context =server.createContext("/api/hello", (exchange -> {
//
//            if ("GET".equals(exchange.getRequestMethod())) {
//                Map<String, List<String>> params = splitQuery(exchange.getRequestURI().getRawQuery());
//                String noNameText = "Anonymous";
//                String name = params.getOrDefault("name", List.of(noNameText)).stream().findFirst().orElse(noNameText);
//                String respText = String.format("Hello %s!", name);
//                exchange.sendResponseHeaders(200, respText.getBytes().length);
//                OutputStream output = exchange.getResponseBody();
//                output.write(respText.getBytes());
//                output.flush();
//            } else {
//                exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
//            }
//            exchange.close();
//        }));
//        context.setAuthenticator(new BasicAuthenticator("user_realm") {
//            @Override
//            public boolean checkCredentials(String user, String pwd) {
//                return user.equals("admin") && pwd.equals("admin");
//            }
//        });

        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
