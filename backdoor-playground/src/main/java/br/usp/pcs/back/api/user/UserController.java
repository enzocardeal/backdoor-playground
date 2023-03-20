package br.usp.pcs.back.api.user;

import br.usp.pcs.back.api.Controller;
import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.*;
import br.usp.pcs.back.error.ApplicationExceptions;
import br.usp.pcs.back.error.GlobalExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static br.usp.pcs.back.domain.models.ResponseModel.Response;
import static br.usp.pcs.back.domain.models.UserResponseModel.RegistrationResponse;
import static br.usp.pcs.back.domain.models.UserResponseModel.GetResponse;
import static br.usp.pcs.back.utils.SecurityUtils.hashPassword;
import static br.usp.pcs.back.utils.SecurityUtils.unhashPassword;

public class UserController extends Controller {

    private final UserDataSource datasource;

    public UserController(UserDataSource datasource, ObjectMapper objectMapper,
                          GlobalExceptionHandler exceptionHandler) {
        super(objectMapper, exceptionHandler);
        this.datasource = datasource;
    }

    @Override
    protected void execute(HttpExchange exchange) throws IOException {
        byte[] response;
        if ("POST".equals(exchange.getRequestMethod())) {
            Response e = doPost(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.headers());
            exchange.sendResponseHeaders(e.statusCode().getCode(), 0);
            response = super.writeResponse(e.body());
        }
        else if("GET".equals(exchange.getRequestMethod())){
            Response e = doGet(exchange.getRequestBody());
            exchange.getResponseHeaders().putAll(e.headers());
            exchange.sendResponseHeaders(e.statusCode().getCode(), 0);
            response = super.writeResponse(e.body());
        }
        else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private Response<RegistrationResponse> doPost(InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);


        UserEntity userEntity = datasource.create(registerRequest.getUsername(), hashPassword(registerRequest.getPassword()));

        RegistrationResponse response;
        if(userEntity != null){
            response = new RegistrationResponse(userEntity.getId().toString(), "User created successfully.");
        }
        else{
            response = new RegistrationResponse("", "User already exists.");
        }

        return new Response<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

    private Response<GetResponse> doGet(InputStream is) {
        GetRequest getRequest = super.readRequest(is, GetRequest.class);

        UserEntity userEntity = datasource.get(getRequest.getUsername());
        GetResponse response;
        if(userEntity != null && unhashPassword(userEntity.getPassword(), getRequest.getPassword())){
            response = new GetResponse(
                    userEntity.getRole().toString(),
                    "User found."
            );
        }
        else{
            response = new GetResponse(
                    "",
                    "User not found."
            );
        }

        return new Response<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
