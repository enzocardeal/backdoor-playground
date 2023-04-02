package br.usp.pcs.back.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import de.mkammerer.argon2.Argon2Factory.Argon2Types;

public class SecurityUtils {
	private static final Argon2 argon2 = Argon2Factory.create(Argon2Types.ARGON2id);
	
	public static String hashPassword(String plainTextPassword) {
		
		char[] charArrayPassword = plainTextPassword.toCharArray();
		
		String hashedPassword = argon2.hash(4, 1024 * 1024, 8, charArrayPassword).replace("$", "\\$");
		
		return hashedPassword;
	}
	
	public static boolean unhashPassword(String hash, String plainTextPassword) {
		hash = hash.replace("\\$", "$");
		char[] charArrayPassword = plainTextPassword.toCharArray();
		
		return argon2.verify(hash, charArrayPassword);
	}
}
