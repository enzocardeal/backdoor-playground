package br.usp.pcs.back.api.user;

import br.usp.pcs.back.data.datasource.UserDataSource;
import br.usp.pcs.back.data.entity.UserEntity;
import br.usp.pcs.back.domain.models.Constants;
import br.usp.pcs.back.domain.models.Role;
import com.sun.net.httpserver.HttpServer;
import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static br.usp.pcs.back.Configuration.getErrorHandler;
import static br.usp.pcs.back.Configuration.getObjectMapper;
import static br.usp.pcs.utils.StringUtils.convertInputStreamToString;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JQF.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ApiFuzzTest {
    private UserDataSource userDataSourceMock;
    private String hashedPassword = "$argon2id$v=19$m=1048576,t=4,p=8$mKSTdK1csbCCRgmiCh/9lw$SsOq1K8++MDziplzjnFN5HwXarntBMFwsKz7eNQVKnQ";
    private HttpServer server = null;
    private int serverPort = 8000;
    private String baseUrl = "http://localhost";
    @BeforeAll
    public void beforeAll(){
        try{
            userDataSourceMock = mock(UserDataSource.class);

            when(userDataSourceMock.get(eq("user1"))).thenReturn(new UserEntity(UUID.randomUUID(), "user1", hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.get(eq("user2"))).thenReturn(new UserEntity(UUID.randomUUID(), "user2", hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.get(eq("user3"))).thenReturn(new UserEntity(UUID.randomUUID(), "user3", hashedPassword, Role.DEFAULT));

            when(userDataSourceMock.get(eq("user4"))).thenReturn(null);

            when(userDataSourceMock.get(eq("user5"))).thenReturn(new UserEntity(UUID.randomUUID(), "user5", hashedPassword, Role.ADMIN));

            when(userDataSourceMock.create(eq("user1"), any())).thenReturn(new UserEntity(UUID.randomUUID(), "user1", hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.create(eq("user2"), any())).thenReturn(new UserEntity(UUID.randomUUID(), "user2", hashedPassword, Role.DEFAULT));
            when(userDataSourceMock.create(eq("user3"), any())).thenReturn(new UserEntity(UUID.randomUUID(), "user3", hashedPassword, Role.DEFAULT));

            when(userDataSourceMock.create(eq("user4"), any())).thenReturn(null);
            when(userDataSourceMock.create(eq("user5"), any())).thenReturn(null);
            when(userDataSourceMock.create(eq("user6"), any())).thenReturn(null);

            LoginController loginController = new LoginController(userDataSourceMock, getObjectMapper(),
                    getErrorHandler());
            SignUpController signUpController = new SignUpController(userDataSourceMock, getObjectMapper(),
                    getErrorHandler());

            server = HttpServer.create(new InetSocketAddress(serverPort), 0);
            server.createContext("/api/user/login", loginController::handle);
            server.createContext("/api/user/signup", signUpController::handle);
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Fuzz
    public void userLoginTest(InputStream userInput){
        List<String> userInputSplit = parseUserInput(userInput);
        String username = userInputSplit.get(0);
        String password = userInputSplit.get(1);

        postRequest("/api/user/login", username, password);
    }
    @Fuzz
    public void userSignUpTest(InputStream userInput){
        List<String> userInputSplit = parseUserInput(userInput);
        String username = userInputSplit.get(0);
        String password = userInputSplit.get(1);

        postRequest("/api/user/signup", username, password);
    }
    @Fuzz
    public void wrongMethodTest(InputStream url){
        String urlString = "";
        try {
            urlString = convertInputStreamToString(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        getRequest(urlString);
    }

    public List<String> parseUserInput(InputStream inputStream){
        String userInputString = null;
        String user = null;
        String password = null;

        try {
            userInputString = convertInputStreamToString(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean hasComma = userInputString.contains(",");

        if(hasComma) {
            String[] userInputSplit = userInputString.split(",");
            user = userInputSplit[0];
            password = userInputSplit[1];
        }
        else {
            user = userInputString.substring(0, (userInputString.length()/2));
            password = userInputString.substring((userInputString.length()/2));
        }
        return Arrays.asList(user, password);
    }
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
                JSONParser parser = new JSONParser();
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
