package unsuck.gae;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.inject.Inject;

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

	private static @Inject Injector injector;
	
	/** Inject ourselves when the object is reconstituted from serialization */
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		
		injector.injectMembers(this);
	}
	
//	/** Perform guice injection and then continue */
//	@Override
//	public final void run()
//	{
//    	injector.injectMembers(this);
//    	this.run2();
//	}
//	
//	/** Implement this instead of run(), executed after guice injection */
//	abstract public void run2();
}
