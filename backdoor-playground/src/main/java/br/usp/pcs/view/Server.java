package br.usp.pcs.view;

import com.sun.net.httpserver.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static br.usp.pcs.control.User.addUser;
import static br.usp.pcs.control.User.getUser;

public class Server {
    public static void start() {
        HttpServer server = null;
        try {
            server = HttpServer.create(new InetSocketAddress(5500), 0);
            HttpContext contextLogin = server.createContext("/login", Server::handleLoginRequest);
            HttpContext contextSignup = server.createContext("/signup", Server::handleSignupRequest);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleLoginRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        String role = login(exchange);
        String response = "{\"role\":\""+role+"\"}";

        //Start response
        //System.out.println("Servidor recebeu resposta");
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleSignupRequest(HttpExchange exchange) throws IOException {
        URI requestURI = exchange.getRequestURI();
        Boolean success = signup(exchange);
        String response = "{\"success\":\""+success+"\"}";

        //Start response
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String login(HttpExchange exchange) throws IOException {
        JSONObject jsonObject = parseExchangeToJson(exchange);

        String password = (String) jsonObject.get("password");
        String username = (String) jsonObject.get("username");

        String role = getUser(username, password);
        return role;
    }

    private static boolean signup(HttpExchange exchange) throws IOException {
        JSONObject jsonObject = parseExchangeToJson(exchange);

        String password = (String) jsonObject.get("password");
        String username = (String) jsonObject.get("username");

        Boolean success = addUser(username, password);
        return success;
    }

    private static JSONObject parseExchangeToJson(HttpExchange exchange) throws IOException {
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

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject = (JSONObject) parser.parse(String.valueOf(buf));
            return jsonObject;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
