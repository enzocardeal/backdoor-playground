package br.usp.pcs.back.api.user;

import br.usp.pcs.back.api.Controller;
import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Constants;
import br.usp.pcs.back.domain.models.RegistrationRequest;
import br.usp.pcs.back.domain.models.StatusCode;
import br.usp.pcs.back.error.ApplicationExceptions;
import br.usp.pcs.back.error.GlobalExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static br.usp.pcs.back.domain.models.ResponseModel.Response;
import static br.usp.pcs.back.domain.models.RegistrationModel.RegistrationResponse;

public class RegistrationController extends Controller {

    private final UserDataSource datasource;

    public RegistrationController(UserDataSource datasource, ObjectMapper objectMapper,
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
        } else {
            throw ApplicationExceptions.methodNotAllowed(
                    "Method " + exchange.getRequestMethod() + " is not allowed for " + exchange.getRequestURI()).get();
        }

        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }

    private Response<RegistrationResponse> doPost(InputStream is) {
        RegistrationRequest registerRequest = super.readRequest(is, RegistrationRequest.class);


//        TODO: encode password
        UserEntity userEntity = datasource.create(registerRequest.getUsername(), registerRequest.getPassword());

        RegistrationResponse response = new RegistrationResponse(userEntity.getId());

        return new Response<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }
}
