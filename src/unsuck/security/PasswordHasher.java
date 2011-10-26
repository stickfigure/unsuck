/*
 */

package unsuck.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

/**
 * Don't use this.  Use BCrypt instead.
 * 
 * Tool which securely generates and validates password hashes using an
 * algorithm you specify and a random per-password salt.
 * 
 * The produced hashes are a byte[] of which the first 8 bytes are random salt
 * and the rest are the hash of the UTF-8 bytes of the password string.
 * 
 * @author Jeff Schnitzer
 */
@Deprecated
public class PasswordHasher
{
	/** Number of bytes for the salt */
	private static final int SALT_LEN = 8;
	
	/** Generates random salts */
	private static final Random RANDOM = new Random();
	
	/** */
	private String hashAlgorithm;
	
	/** 
	 * Defaults algorithm to "SHA-256" - NEVER change this default or client code will break.
	 */
	public PasswordHasher() {
		this("SHA-256");
	}
	
	/** */
	public PasswordHasher(String hashAlgorithm) {
		this.hashAlgorithm = hashAlgorithm;
	}
	
	/** */
	public byte[] makeHash(String password)
	{
		byte[] salt = new byte[SALT_LEN];
		RANDOM.nextBytes(salt);
		
		return makeHash(salt, password);
	}
	
	/**
	 * Checks whether the password matches the specified hash.
	 * @param madeHash must be the hash returned from makeHash because it includes the salt
	 * @return true if password matches, false if not
	 */
	public boolean checkPassword(byte[] madeHash, String password) {
		byte[] salt = Arrays.copyOf(madeHash, SALT_LEN);
		byte[] complete = makeHash(salt, password);
		
		return Arrays.equals(madeHash, complete);
	}
	
	/**
	 * Make an output hash (including salt) with a specific salt
	 */
	private byte[] makeHash(byte[] salt, String password) {
		try
		{
			MessageDigest digest = MessageDigest.getInstance(this.hashAlgorithm);
			digest.update(salt);
			byte[] hash = digest.digest(password.getBytes("UTF-8"));
			
			byte[] complete = Arrays.copyOf(salt, salt.length + hash.length);
			System.arraycopy(hash, 0, complete, salt.length, hash.length);
			
			return complete;
		}
		catch (NoSuchAlgorithmException ex) { throw new RuntimeException(ex); }
		catch (UnsupportedEncodingException ex) { throw new RuntimeException(ex); }
	}
}