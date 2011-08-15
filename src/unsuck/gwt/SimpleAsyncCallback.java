package unsuck.gwt;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

/**
 * This handler takes care of errors by simply raising a message box,
 * leaving you to implement onSuccess().  Override failed() if you want
 * more sophisticated error handling.
 */
public class SimpleAsyncCallback<T> implements AsyncCallback<T>
{
	/** */
	@Override
	final public void onSuccess(T result)
	{
		this.success(result);
		this.done();
	}
	
	/** */
	@Override
	final public void onFailure(Throwable caught)
	{
		if (caught instanceof IncompatibleRemoteServiceException)
		{
			Window.alert("The network protocol has changed. Please reload the page to get a new version.");
		}
		else
		{
			GWT.log("Caught exception", caught);
			this.failed(caught);
		}
		
		this.done();
	}
	
	/** Called when we have succeeded */
	protected void success(T result) {}
	
	/** Called when a failure occurs */
	protected void failed(Throwable caught)
	{
		// Don't use more sophisticated dialogs, interferes with code splitting
		String msg = caught.getLocalizedMessage();
		Window.alert("Error: " + msg);
	}
	
	/** Called after success or error */
	protected void done() {}
}
