package br.usp.pcs.control_legacy;

import static br.usp.pcs.control_legacy.User.getUser;
import static br.usp.pcs.utils.StringUtils.convertInputStreamToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

@RunWith(JQF.class)
public class UserFuzzTest {
	String role;
	
	@BeforeEach
	void setup() {
		role = null;
	}
	
	@Fuzz
	public void testGetUser(InputStream userInput) {
		String userInputString = null;
		String user = null;
		String password = null;
		
		try {
			userInputString = convertInputStreamToString(userInput);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean hasComma = userInputString.contains(",");
		
		if(hasComma) {
			String[] userInputSplitted = userInputString.split(",");
			user = userInputSplitted[0];
			password = userInputSplitted[1];
		}
		else {
			user = userInputString.substring(0, (userInputString.length()/2));
			password = userInputString.substring((userInputString.length()/2));
		}
		
		role = getUser(user, password);
	}
}
