package unsuck.gae;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.appengine.api.users.User;

/**
 * <p>Guice AOP interceptor which "fixes" the User in development when using Federated Login on Appengine.</p>
 * 
 * <p>If you're using Federated Login (ie OpenID), the field you care about in the User object is
 * "federatedIdentity".  Unfortunately when in dev mode, this field is always null.  This interceptor
 * fakes out the federatedIdentity field, making it the same as the userId.  Good enough.</p>
 * 
 * <p>Put this in your Guice module configure():</p>
<pre>
if (!Situation.isProduction())
	bindInterceptor(Matchers.subclassesOf(User.class), Matchers.any(), new LocalFederatedUserInterceptor());
</pre>
 * 
 * @author Jeff Schnitzer <jeff@infohazard.org>
 */
public class LocalFederatedUserInterceptor implements MethodInterceptor
{
	@Override
	public Object invoke(MethodInvocation inv) throws Throwable
	{
		if (inv.getMethod().getName().equals("getFederatedIdentity"))
		{
			User user = (User)inv.getThis();
			
			if (user.getFederatedIdentity() == null)
				return "fake:" + user.getUserId();
			else
				return user.getFederatedIdentity();
		}
		
		return inv.proceed();
	}
}