package com.finalproject.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * @author Deepak_Lokwani
 * 
 * NUID: 001316769
 * 
 * Project name: Finalproject
 * Package name: com.finalproject.utils
 *
 */
public class Hash {

	public Hash() {
		// TODO Auto-generated constructor stub
	}
	public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPasssword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
