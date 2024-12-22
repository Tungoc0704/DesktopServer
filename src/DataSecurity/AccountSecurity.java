package DataSecurity;

import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.mindrot.jbcrypt.BCrypt;

public class AccountSecurity {

	// Mã hóa password:
	public String hashPassword(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
		// -> Method hashpw do BCrypt cung cấp

	}

	// Ma hoa email:
	private static final String ALGORITHM = "AES";

	public static SecretKey generateKey() throws Exception {
		KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
		keyGen.init(256); // Độ dài khóa AES
		return keyGen.generateKey();
	}

	public static String encryptEmail(String email, SecretKey sec) throws Exception {
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, sec);
		byte[] encrypted = cipher.doFinal(email.getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
	}
}
