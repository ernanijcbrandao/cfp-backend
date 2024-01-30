package br.ejcb.cfp.seguranca.util;

import java.util.Random;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordHashingUtils {

	private static final Argon2 ARGON2 = Argon2Factory.create();

	private static Random random = new Random();
	
    public static String hashPassword(String password) {
        int iterations = random.nextInt(11, 23); //17;
        int memory =  random.nextInt(64, 128) * 1024; // 131072; // 128k
        int parallelism = 1;

        return ARGON2.hash(iterations, memory, parallelism, password.toCharArray());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return ARGON2.verify(hashedPassword, password.toCharArray());
    }

}
