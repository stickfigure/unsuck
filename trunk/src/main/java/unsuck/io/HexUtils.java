package unsuck.io;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;


/**
 * WTF is up with the commons-codec API?  This is retarded.
 */
public class HexUtils
{
	/** Dump stream to file */
	public static byte[] decode(String hex)
	{
		try {
			return Hex.decodeHex(hex.toCharArray());
		} catch (DecoderException ex) {
			throw new RuntimeException(ex);
		}
	}
}