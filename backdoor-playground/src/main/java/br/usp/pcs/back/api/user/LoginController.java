package br.usp.pcs.back.api.user;

import br.usp.pcs.back.api.Controller;
import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Constants;
import br.usp.pcs.back.domain.models.LoginRequest;
import br.usp.pcs.back.domain.models.StatusCode;
import br.usp.pcs.back.error.ApplicationExceptions;
import br.usp.pcs.back.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static br.usp.pcs.back.utils.SecurityUtils.unhashPassword;
import static br.usp.pcs.back.domain.models.ResponseModel.Response;
import static br.usp.pcs.back.domain.models.UserResponseModel.LoginResponse;

public class LoginController extends Controller {
    private final UserDataSource datasource;

    public LoginController(UserDataSource datasource, ObjectMapper objectMapper,
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

        else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private Response<LoginResponse> doPost(InputStream is) {
        LoginRequest loginRequest = super.readRequest(is, LoginRequest.class);

        UserEntity userEntity = datasource.get(loginRequest.getUsername());
        LoginResponse response;
        if(userEntity != null && unhashPassword(userEntity.getPassword(), loginRequest.getPassword())){
            response = new LoginResponse(
                    userEntity.getRole().toString(),
                    "User found."
            );
        }
        else{
            response = new LoginResponse(
                    "",
                    "User not found."
            );
        }

        return new Response<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

}
