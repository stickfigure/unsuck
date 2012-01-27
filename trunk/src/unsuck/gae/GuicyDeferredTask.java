package unsuck.gae;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.inject.Injector;

/**
 * A deferred task that performs Guice injection on itself before executing.
 * Note that this class needs guice configuration for requestStaticInjection(GuicyDeferredTask.class);
 * All injected fields should be transient!  Otherwise they will get serialized and that's probably bad.
 */
abstract public class GuicyDeferredTask implements DeferredTask
{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(GuicyDeferredTask.class);

	private static @Inject Injector injector;
	
	/** Perform guice injection and then continue */
	@Override
	public final void run()
	{
		try {
			log.debug("Executing task " + this);
	    	injector.injectMembers(this);
	    	this.run2();
		} catch (RuntimeException ex) {
			log.error("Error executing task " + this, ex);
			throw ex;
		}
	}
	
	/** Implement this instead of run(), executed after guice injection */
	abstract public void run2();
}
