/*
 * $Id$
 */

package unsuck.security;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * Creates a digest of all material written to the stream.
 * 
 * @author Jeff Schnitzer
 */
public class DigestOutputStream extends OutputStream
{
	/** */
	MessageDigest md;
	
	/**
	 * Creates a digest stream for the given algorithm.
	 */
	public DigestOutputStream(String algorithm)
	{
		try
		{
			this.md = MessageDigest.getInstance(algorithm);
		}
		catch (NoSuchAlgorithmException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		this.md.update(b, off, len);
	}

	@Override
	public void write(int b) throws IOException
	{
		this.md.update((byte)b);
	}
	
	/** */
	public byte[] getDigest()
	{
		return this.md.digest();
	}
	
	/** */
	public String getDigestHex()
	{
		return Hex.encodeHexString(this.getDigest());
	}
}