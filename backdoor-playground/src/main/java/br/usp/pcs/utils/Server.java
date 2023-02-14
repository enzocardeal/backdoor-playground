package br.usp.pcs.utils;

import br.usp.pcs.view.AdminUser;
import br.usp.pcs.view.DefaultUser;
import com.sun.net.httpserver.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static br.usp.pcs.control.User.getUser;

public class Server {
    public static void start() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(5500), 0);
            HttpContext context = server.createContext("/login", Server::handleRequest);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        login(exchange);
        String response = "This is the response at " + requestURI;

        //Start response
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void login(HttpExchange exchange) throws IOException {
        // Read input
        InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        // From now on, the right way of moving from bytes to utf-8 characters:
        int b;
        StringBuilder buf = new StringBuilder(512);

        while ((b = br.read()) != -1) {
            buf.append((char) b);
        }

        br.close();
        isr.close();

        //TODO
        //Login logic
    }
}
