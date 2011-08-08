package unsuck.gae;

import javax.inject.Inject;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.inject.Injector;

/**
 * A deferred task that performs Guice injection on itself before executing.
 * Note that this class needs guice configuration for requestStaticInjection(GuicyDeferredTask.class);
 */
abstract public class GuicyDeferredTask implements DeferredTask
{
	private static final long serialVersionUID = 1L;

	private static @Inject Injector injector;
	
	/** Perform guice injection and then continue */
	@Override
	public final void run()
	{
    	injector.injectMembers(this);
    	this.run2();
	}
	
	/** Implement this instead of run(), executed after guice injection */
	abstract public void run2();
}
