package br.usp.pcs.back.api.user;

import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.*;
import com.sun.net.httpserver.HttpServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static br.usp.pcs.back.Configuration.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static br.usp.pcs.back.domain.models.ResponseModel.Response;

@TestInstance(Lifecycle.PER_CLASS)
public class ApiTest {
    private String defaultUsername;
    private String userNotSignedUp;
    private String failedUserSignUp;
    private String adminUserName;
    private String password;
    private String hashedPassword;
    private HttpServer server = null;
    private int serverPort = 8000;
    private String baseUrl = "http://localhost";
    private UserDataSource userDataSourceMock;
    private JSONParser parser = new JSONParser();
    private LoginController loginController;
    private SignUpController signUpController;

    @BeforeAll
    public void beforeAll(){
        try {
            defaultUsername = "username";
            userNotSignedUp = "notAUser";
            failedUserSignUp = "failedUserSignUp";
            adminUserName = "admin";
            password = "123456";
            hashedPassword = "$argon2id$v=19$m=1048576,t=4,p=8$mKSTdK1csbCCRgmiCh/9lw$SsOq1K8++MDziplzjnFN5HwXarntBMFwsKz7eNQVKnQ";

            userDataSourceMock = mock(UserDataSource.class);
            when(userDataSourceMock.get(eq(defaultUsername))).thenReturn(new UserEntity(UUID.randomUUID(), defaultUsername, hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.get(eq(userNotSignedUp))).thenReturn(null);
            when(userDataSourceMock.get(eq(adminUserName))).thenReturn(new UserEntity(UUID.randomUUID(), adminUserName, hashedPassword, Role.ADMIN));

            when(userDataSourceMock.create(eq(defaultUsername), any())).thenReturn(new UserEntity(UUID.randomUUID(), defaultUsername, hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.create(eq(failedUserSignUp), any())).thenReturn(null);

            loginController = new LoginController(userDataSourceMock, getObjectMapper(),
                    getErrorHandler());
            signUpController = new SignUpController(userDataSourceMock, getObjectMapper(),
                    getErrorHandler());

            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            server.createContext("/api/user/login", loginController::handle);
            server.createContext("/api/user/signup", signUpController::handle);
            server.setExecutor(null);
            server.start();

        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void loginDefaultUser() {
        JSONObject response = postRequest("/api/user/login", defaultUsername, password);
        assertEquals((String)response.get("role"),  Role.DEFAULT.toString());
    }

    @Test
    public void loginAdminUser(){
        JSONObject response = postRequest("/api/user/login", adminUserName, password);
        assertEquals((String)response.get("role"),  Role.ADMIN.toString());
    }

    @Test
    public void loginNotSignedUp() {
        JSONObject response = postRequest("/api/user/login", userNotSignedUp, password);
        assertEquals((String)response.get("role"), "");
    }

    @Test
    public void loginWrongPassword() {
        String wrongPassword = "12345";
        JSONObject response = postRequest("/api/user/login", defaultUsername, wrongPassword);
        assertEquals((String)response.get("role"), "");
    }

    @Test
    public void successfulSignUp(){
        JSONObject response = postRequest("/api/user/signup", defaultUsername, password);
        assertNotEquals((String)response.get("id"), "");
        assertTrue((boolean) response.get("success"));
    }

    @Test
    public void failedSignUp(){
        JSONObject response = postRequest("/api/user/signup", failedUserSignUp, password);
        assertFalse((boolean) response.get("success"));
    }

    @Test
    public void getOnLogin(){
        int responseCode = getRequest("/api/user/login");
        assertEquals(StatusCode.METHOD_NOT_ALLOWED.getCode(), responseCode);
    }

    @Test
    public void getOnSignUp(){
        int responseCode = getRequest("/api/user/signup");
        assertEquals(StatusCode.METHOD_NOT_ALLOWED.getCode(), responseCode);
    }

    @Test
    public void directPostLogin() throws IOException {
        BufferedInputStream requestStream = new BufferedInputStream(new ByteArrayInputStream((
                " {\"password\":\""+password+"\",\"username\":\""+defaultUsername+"\"}").getBytes(StandardCharsets.UTF_8)));
        requestStream.read();
        Response<UserResponseModel.LoginResponse> response = loginController.doPost(new FilterInputStreamImpl(requestStream));

        assertEquals(StatusCode.OK, response.statusCode());
        assertEquals("DEFAULT", response.body().role());
        assertEquals("User found.", response.body().message());
    }

//    @Test
//    public void userLoginTest() throws IOException {
//        HttpExchange httpExchangeMock = mock(HttpExchange.class);
//        BufferedInputStream requestStream = new BufferedInputStream(new ByteArrayInputStream((
//                " {\"password\":\""+password+"\",\"username\":\""+defaultUsername+"\"}").getBytes(StandardCharsets.UTF_8)));
//        requestStream.read();
//        when(httpExchangeMock.getRequestMethod()).thenReturn("POST");
//        when(httpExchangeMock.getRequestBody()).thenReturn(new FilterInputStreamImpl(requestStream));
//        when(httpExchangeMock.getResponseHeaders()).thenReturn(new Headers());
//        when(httpExchangeMock.getResponseBody()).thenReturn(OutputStream.nullOutputStream());
//        loginController.execute(httpExchangeMock);
//    }

    private int getRequest(String endpoint){
        try{
            URL url = new URL(baseUrl+":"+serverPort+endpoint);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("GET");
            int responseCode = http.getResponseCode();

            return responseCode;
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject postRequest(String endpoint, String username, String password){
        try {
            URL url = new URL(baseUrl+":"+serverPort+endpoint);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST");
            http.setRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATION_JSON);
            http.setRequestProperty("Accept", Constants.APPLICATION_JSON);
            http.setDoOutput(true);

            Map<String,String> arguments = new HashMap<>();
            arguments.put("username", username);
            arguments.put("password", password);
            String jsonResp = getObjectMapper().writeValueAsString(arguments);
            byte[] out = jsonResp.toString().getBytes(StandardCharsets.UTF_8);
            int length = out.length;

            http.setFixedLengthStreamingMode(length);
            http.connect();
            try(OutputStream os = http.getOutputStream()) {
                os.write(out);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(http.getInputStream(), "utf-8"))) {
                StringBuilder responseString = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    responseString.append(responseLine.trim());
                }
                JSONObject response = (JSONObject) parser.parse(responseString.toString());

                return response;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
