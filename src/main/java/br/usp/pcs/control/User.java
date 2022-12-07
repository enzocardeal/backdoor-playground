package br.usp.pcs.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class User {
    private static JSONParser jsonParser = new JSONParser();
    
	private static String filePath = new File("").getAbsolutePath();
	
	public static String getUser(String username, String password) {
		try (FileReader reader = new FileReader(filePath + "/resources/user-table.json")){
			String role = null;

            Object obj = jsonParser.parse(reader);
 
            JSONArray userList = (JSONArray) obj;
            
            for(int i = 0; i < userList.size(); i++) {
            	JSONObject user = (JSONObject) userList.get(i);
            	role = checkUserObject(user, username, password);
            	if(role != null) {
            		break;
            	}
            }
            
            return role;
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
		return null;
	}
	 
	private static String checkUserObject(JSONObject user, String usernameInput, String passwordInput) {
		String username = (String) user.get("name");
		String password = (String) user.get("password");

		
		if(usernameInput.equals(username) && passwordInput.equals(password)) {
			return (String) user.get("role");
			
		}
		else {
			return null;
		}
		
	}
}
