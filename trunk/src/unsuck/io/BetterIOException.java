package unsuck.io;

import java.io.IOException;

/**
 * A wrapper for IOException that extends RuntimeException.  Use this whenver you want to convert
 * IOException into something that is unchecked but still identifiable as IOException.
 */
public class BetterIOException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	/** */
	public BetterIOException(IOException ex) {
		super(ex.getMessage(), ex);
	}
}