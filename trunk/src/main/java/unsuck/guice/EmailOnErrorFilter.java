package unsuck.guice;

import java.io.IOException;

import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import unsuck.web.AbstractFilter;

/**
 * Like the EmailOnErrorInterceptor, this filter emails us a nastygram any time we hit an exception.
 */
@Singleton
abstract public class EmailOnErrorFilter extends AbstractFilter {

	/** */
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(request, response);
		} catch (IOException ex) {
			notify(ex);
			throw ex;
		} catch (ServletException ex) {
			notify(ex);
			throw ex;
		} catch (RuntimeException ex) {
			notify(ex);
			throw ex;
		} catch (Error ex) {
			notify(ex);
			throw ex;
		}
	}

	/** */
	abstract protected void notify(Throwable th);
}