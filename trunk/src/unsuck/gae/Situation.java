package unsuck.gae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.utils.SystemProperty;

/**
 * Provides some basic information about where this app is situated - ie
 * in development or production, and what URLs should be used to ping
 * services etc.
 *
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
//@Slf4j	// logger used in static initializer
public class Situation
{
	/** */
	private final static Logger log = LoggerFactory.getLogger(Situation.class);

	/** Let's kick this message off early */
	static {
		if (!isProduction()) {
			log.warn("************************ USING DEVOPMENT MODE ************************");
		}
	}

	/** App id, ie "voost0" - will be same even on dev instance */
	public final static String RAW_APPENGINE_ID = SystemProperty.applicationId.get() == null ? "testmode" : SystemProperty.applicationId.get();
	public final static String APPENGINE_ID = RAW_APPENGINE_ID.startsWith("s~") ? RAW_APPENGINE_ID.substring("s~".length()) : RAW_APPENGINE_ID;

	/** */
	public static boolean isProduction()
	{
		try
		{
			return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
		}
		catch (Exception ex)
		{
			// This will happen if we run outside the context of appengine
			return false;
		}
	}
}