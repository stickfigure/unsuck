package unsuck.cdi;

import java.io.IOException;
import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
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
	@SuppressWarnings("unchecked")
	public void init() throws ServletException
	{
		BeanManager mgr = (BeanManager)this.getServletContext().getAttribute(BeanManager.class.getName());
		
		String className = this.getServletConfig().getInitParameter(PROXY_FOR_INIT_PARAM_NAME);
		try
		{
			Class<?> clazz = Class.forName(className);
			Iterator<Bean<?>> it = mgr.getBeans(clazz).iterator();
			
			if (it.hasNext())
			{
				Bean<?> servletBean = it.next();
				
				if (it.hasNext())
					throw new ServletException("Too many managed beans for " + clazz);
				
				this.actual = (HttpServlet)servletBean.create((CreationalContext)mgr.createCreationalContext(servletBean));
			}
			else
			{
				InjectionTarget targ = mgr.createInjectionTarget(mgr.createAnnotatedType(clazz));
				CreationalContext cc = mgr.createCreationalContext(null);
				this.actual = (HttpServlet)targ.produce(cc);
				targ.inject(this.actual, cc);
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new ServletException(e);
		}
	}

	/** */
	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException
	{
		this.actual.service(req, res);
	}
}
