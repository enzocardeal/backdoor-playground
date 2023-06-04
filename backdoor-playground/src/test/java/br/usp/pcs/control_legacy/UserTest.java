package br.usp.pcs.control_legacy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import static br.usp.pcs.control_legacy.User.getUser;

class UserTest {
	String role;
	
	@BeforeEach
	void setup() {
		role = null;
	}
	
    @Test
    void checkExistingAccounts() {
    	role = getUser("user", "123456");
    	assertEquals("user", role);
    	
    	role = getUser("admin", "a123456");
    	assertEquals("admin", role);
    }
    
//    @Test
//    void checkUnexistingAccounts() {
//
//    	role = getUser("aba", "eba");
//    	assertEquals(null, role);
//
//    	role = getUser("eba", "eba");
//    	assertEquals(null, role);
//
//    }
}
