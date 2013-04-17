/*
 */

package unsuck.guava;

import unsuck.guice.Nullable;

import com.google.common.base.Function;


/**
 * Add the ability to reverse the function
 *
 * @author Jeff Schnitzer
 */
public interface ReversibleFunction<F, T> extends Function<F, T>
{
	/**
	 * Do the inverse of apply()
	 * @param to is the version after apply() has run
	 */
	@Nullable
	F unapply(@Nullable T to);
}