package br.usp.pcs.control;

import static br.usp.pcs.control.User.getUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
	public void testGetUser(String user, String password) {
		role = getUser(user, password);
		assertEquals(null, role);
	}
}
