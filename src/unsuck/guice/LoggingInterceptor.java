package unsuck.guice;


import java.lang.reflect.Field;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static Logger log = LoggerFactory.getLogger(LoggingInterceptor.class.getName());
	
	/* (non-Javadoc)
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable
	{
		Object obj = inv.getThis();
		Logger logger;
		
		// The actual class will be the guice enhanced one, so get super
		Class<?> clazz = obj.getClass().getSuperclass();
		try {
			Field loggerField = clazz.getDeclaredField("log");
			loggerField.setAccessible(true);
			
			if (Logger.class.isAssignableFrom(loggerField.getType()))
				logger = (Logger)loggerField.get(obj);
			else
				logger = log;
		} catch (NoSuchFieldException ex) {
			logger = log;
		}

		StringBuilder msg = new StringBuilder();
		
		msg.append(clazz.getSimpleName());
		msg.append('.');
		msg.append(inv.getMethod().getName());
		msg.append('(');
		
		for (int i=0; i<inv.getArguments().length; i++)
		{
			Object arg = inv.getArguments()[i];
			if (arg instanceof String)
				msg.append('"').append(arg).append('"');
			else
				msg.append(arg);
			
			if (i != inv.getArguments().length-1)
				msg.append(", ");
		}

		msg.append(')');
				
		logger.debug(msg.toString());
		
		return inv.proceed();
	}
}