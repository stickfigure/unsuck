/*
 * $Id$
 */

package unsuck.security;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import unsuck.web.AbstractFilter;


/**
 * <p>A filter which requires very simple basic auth.  Just derive a subclass
 * that implements the getRealm() and authenticate() methods.</p>  
 * 
 * <p>Does not interact with the J2EE security constraints
 * in any way, so it can be used to enable basic auth in Google App Engine.</p>
 * 
 * <p>Requires commons-codec.</p>
 * 
 * @author Jeff Schnitzer
 */
abstract public class BasicAuthFilter extends AbstractFilter
{
	/** */
	private final static Logger log = LoggerFactory.getLogger(BasicAuthFilter.class);
	
	/** */
	String authenticateHeader;
	
	/** @return the ream which is advertised to clients */
	abstract public String getRealm();
	
	/** @return true if the username/pw combo are valid */
	abstract public boolean authenticate(String username, String password);
	
	/** Useful tool:  Add http basic auth credentials to a connection */
	public static void addBasicAuth(URLConnection conn, String username, String password)
	{
		try
		{
			StringBuilder buf = new StringBuilder(username).append(':').append(password);
			
			// There is no standard for charset, might as well use utf-8
			byte[] bytes = buf.toString().getBytes("utf-8");

			// Watch out, Base64.encodeBase64String puts a fatal CRLF at the end
			String header = "Basic " + new String(Base64.encodeBase64(bytes), "utf-8");	// really ascii
			conn.setRequestProperty("Authorization", header);
			
			if (log.isDebugEnabled())
				log.debug("Authorization header is: " + header);
		}
		catch (UnsupportedEncodingException ex) { throw new RuntimeException(ex); }
	}

	/** */
	@Override
	public void init(FilterConfig cfg) throws ServletException
	{
		super.init(cfg);
		
		this.authenticateHeader = "Basic realm=\"" + this.getRealm() + "\"";
	}

	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		try
		{
			String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Basic "))
			{
				String base64AuthInfo = authorization.substring("Basic ".length());
				// There is no charset standard for basic auth, utf-8 is as good as any
				String authInfo = new String(Base64.decodeBase64(base64AuthInfo.getBytes("utf-8")), "utf-8");
				String[] authParts = authInfo.split(":");
				
				if (this.authenticate(authParts[0], authParts[1]))
				{
					chain.doFilter(request, response);
					return;
				}
				else
				{
					if (log.isWarnEnabled())
						log.warn("Failed auth attempt for: " + authParts[0]);
				}
			}
			else
			{
				if (log.isWarnEnabled())
					log.warn("Bad authorization header: " + authorization);
			}
		}
		catch (Exception ex)
		{
			if (log.isWarnEnabled())
				log.warn("Error trying to parse authorization header", ex);
		}

		// return auth required
		response.addHeader("WWW-Authenticate", this.authenticateHeader);
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}