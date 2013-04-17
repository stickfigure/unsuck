package unsuck.gwt;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.RpcTokenException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Injector;

/**
 * This thing is a little crazy.  It overrides (copy-paste, unfortunately) some core behavior
 * of the RemoteServiceServlet to use Guice to instantiate the delegate.
 * 
 * Thus, when a request comes in for Login.login(), the Guice injector is interrogated
 * for an instance of Login upon which the method is called.
 */
@Singleton
public class GuiceyRemoteServiceServlet extends RemoteServiceServlet
{
	private static final long serialVersionUID = 1L;
	
	/** */
	@SuppressWarnings("unused")
	private final static Logger log = LoggerFactory.getLogger(GuiceyRemoteServiceServlet.class);
	
	/** */
	Injector injector;
	
	/** */
	@Inject
	public GuiceyRemoteServiceServlet(Injector injector)
	{
		this.injector = injector;
	}

	/**
	 * This method is very nearly cut-and-pasted from RemoteServiceServlet. It
	 * makes two changes: 
	 * #1) RPC.decodeRequest is passed null for the expected class, eliminating the type checking
	 * #2) The delegate is instantiated via guice
	 */
	@Override
	public String processCall(String payload) throws SerializationException
	{
		// First, check for possible XSRF situation
		checkPermutationStrongName();

		try
		{
			// change #1, pass in null
			RPCRequest rpcRequest = RPC.decodeRequest(payload, null, this);
			onAfterRequestDeserialized(rpcRequest);
			// change #2, use injector.getInstance()
			return RPC.invokeAndEncodeResponse(injector.getInstance(rpcRequest.getMethod().getDeclaringClass()),
					rpcRequest.getMethod(),
					rpcRequest.getParameters(), rpcRequest.getSerializationPolicy(), rpcRequest.getFlags());
		}
		catch (IncompatibleRemoteServiceException ex)
		{
			log("An IncompatibleRemoteServiceException was thrown while processing this call.", ex);
			return RPC.encodeResponseForFailure(null, ex);
		}
		catch (RpcTokenException tokenException)
		{
			log("An RpcTokenException was thrown while processing this call.", tokenException);
			return RPC.encodeResponseForFailure(null, tokenException);
		}
	}
}
