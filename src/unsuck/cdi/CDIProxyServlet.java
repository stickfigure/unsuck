package unsuck.cdi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

/**
 * <p>Servlet which proxies to another servlet which was loaded using CDI and thus receives
 * proper dependency injection. You can use this on containers (like GAE) which do not support
 * CDI for Servlets.</p>
 * 
 * <p>Use an init-param which defines the actual class of the servlet to load.</p>
 * 
 * @author Jeff Schnitzer
 */
public class CDIProxyServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	/** */
	public static final String PROXY_FOR_INIT_PARAM_NAME = "realServletClass";
	
	/** The actual, CDI-managed servlet we wrap */
	HttpServlet actual;

	/** */
	@Override
	public void init() throws ServletException
	{
		String className = this.getServletConfig().getInitParameter(PROXY_FOR_INIT_PARAM_NAME);
		
		this.actual = (HttpServlet)CDIUtils.getOrRegisterBean(this.getServletContext(), className);
	}

	/** */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
	{
		this.actual.service(req, res);
	}
}
