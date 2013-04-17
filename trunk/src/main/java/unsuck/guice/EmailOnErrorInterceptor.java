package unsuck.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interceptor that sends the admins a nastygram if an exception is thrown.  Be careful about putting
 * this in places that could generate a lot of email.
 */
abstract public class EmailOnErrorInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable {
		try {
			return inv.proceed();
		} catch (Throwable th) {
			notify(th);
			throw th;
		}
	}

	/** */
	abstract protected void notify(Throwable th);
}