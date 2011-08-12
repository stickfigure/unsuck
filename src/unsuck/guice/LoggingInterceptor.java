package unsuck.guice;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>Guice AOP interceptor which looks for {@code @LogCall} annotation on methods to log method calls.</p>
 * 
 * <p>Put this in your Guice module configure():</p>
 * <p>{@code bindInterceptor(Matchers.any(), Matchers.annotatedWith(LogCall.class), new LoggingInterceptor());}
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class LoggingInterceptor implements MethodInterceptor
{
	/** Default fallback logger */
	private static Logger log = Logger.getLogger(LoggingInterceptor.class.getName());
	
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable
	{
//		Object obj = inv.getThis();
//		Logger logger;
//		
//		Class<?> clazz = obj.getClass();
//		try {
//			Field loggerField = clazz.getDeclaredField("log");
//			
//			if (Logger.class.isAssignableFrom(loggerField.getType()))
//				logger = (Logger)loggerField.get(obj);
//			else
//				logger = log;
//		} catch (NoSuchFieldException ex) {
//			logger = log;
//		}

		log.logp(
			Level.FINEST,
			inv.getClass().getName(),
			inv.getMethod().getName(),
			"invocation",
			inv.getArguments());
		
		return inv.proceed();
	}
}