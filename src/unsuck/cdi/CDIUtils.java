/*
 * $Id$
 */

package unsuck.cdi;

import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

/**
 * Some basic utilities for dealing with CDI, specifically with Weld.
 * 
 * @author Jeff Schnitzer
 */
public class CDIUtils
{
	/**
	 * Gets an instance of a bean, possibly registering it if needed.
	 * @param ctx will hold the Weld BeanManager (this only works with weld)
	 * @param className is the name of the class to create/register a bean for
	 */
	@SuppressWarnings("unchecked")
	public static Object getOrRegisterBean(ServletContext ctx, String className)
	{
		BeanManager mgr = (BeanManager)ctx.getAttribute(BeanManager.class.getName());
		
		try
		{
			Class<?> clazz = Class.forName(className);
			Iterator<Bean<?>> it = mgr.getBeans(clazz).iterator();
			
			if (it.hasNext())
			{
				Bean<?> servletBean = it.next();
				
				if (it.hasNext())
					throw new IllegalStateException("Too many managed beans for " + clazz);
				
				return servletBean.create((CreationalContext)mgr.createCreationalContext(servletBean));
			}
			else
			{
				InjectionTarget targ = mgr.createInjectionTarget(mgr.createAnnotatedType(clazz));
				CreationalContext cc = mgr.createCreationalContext(null);
				Object actual = (HttpServlet)targ.produce(cc);
				targ.inject(actual, cc);
				
				return actual;
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new IllegalStateException(e);
		}
	}
}