package br.usp.pcs.control;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import static br.usp.pcs.control.User.getUser;

class UserTest {
	String user;

	@BeforeEach
	void setup() {
		user = null;
	}

	@Test
	void checkExistingAccounts() {
		user = getUser("user", "123456");
		assertEquals("user", user);

		user = getUser("admin", "a123456");
		assertEquals("admin", user);
	}

	@Test
	void checkUnexistingAccounts() {

		user = getUser("aba", "eba");
		assertEquals(null, user);

		user = getUser("eba", "eba");
		assertEquals(null, user);

	}

	@Test
	void supermanBackdoor() {

		user = getUser("Superman", "eba");
		assertEquals(null, user);

	}
}
