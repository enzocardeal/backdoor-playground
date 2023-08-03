package br.usp.pcs.back.api;

//import br.usp.pcs.back.error.ApplicationExceptions;
import br.usp.pcs.back.error.GlobalExceptionHandler;
import br.usp.pcs.back.error.InvalidRequestException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
//import io.vavr.control.Try;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public abstract class Controller {

    private final ObjectMapper objectMapper;
    private final GlobalExceptionHandler exceptionHandler;

    public Controller(ObjectMapper objectMapper,
                   GlobalExceptionHandler exceptionHandler) {
        this.objectMapper = objectMapper;
        this.exceptionHandler = exceptionHandler;
    }

    public void handle(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
    
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            try {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                execute(exchange);
            } catch (Exception e) {
                exceptionHandler.handle(e, exchange);
            }
        }
    }
    
    protected abstract void execute(HttpExchange exchange) throws Exception;


    protected <T> T readRequest(InputStream is, Class<T> type) {
//        return Try.of(() -> objectMapper.readValue(is, type))
//                .getOrElseThrow(ApplicationExceptions.invalidRequest());
        try {
            return objectMapper.readValue(is, type);
        } catch (IOException e) {
            throw new InvalidRequestException(400, e.getMessage());
        }
    }

    protected <T> byte[] writeResponse(T response) {
//        return Try.of(() -> objectMapper.writeValueAsBytes(response))
//                .getOrElseThrow(ApplicationExceptions.invalidRequest());
        try {
            return objectMapper.writeValueAsBytes(response);
        } catch (JsonProcessingException e) {
            throw new InvalidRequestException(400, e.getMessage());
        }
    }

    protected static Headers getHeaders(String key, String value) {
        Headers headers = new Headers();
        headers.set(key, value);
        return headers;
    }
}
