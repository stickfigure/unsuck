/*
 */

package unsuck.test;

import java.util.Iterator;
import java.util.Random;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import unsuck.collections.FailSafeStack;

/**
 * @author Jeff Schnitzer
 */
public class FailSafeStackTests
{
	private static final String FOO = "foo";
	private static final String BAR = "bar";

	FailSafeStack<String> stack;

	/** */
	@BeforeMethod
	public void setUp() throws Exception {
		this.stack = new FailSafeStack<String>();
	}

	/** */
	@AfterMethod
	public void tearDown() throws Exception {
		this.stack = null;
	}

	/** */
	@Test
	public void addWhileIterating() throws Exception {
		assert stack.size() == 0;
		stack.add(FOO);
		assert stack.size() == 1;

		Iterator<String> it = stack.iterator();
		assert it.hasNext();
		assert it.next().equals(FOO);
		assert !it.hasNext();

		Iterator<String> it2 = stack.iterator();
		assert it2.hasNext();
		
		stack.add(BAR);
		assert stack.size() == 2;
		assert !it.hasNext();
		
		assert it2.next().equals(BAR);
		assert it2.next().equals(FOO);
	}

	/** */
	@Test
	public void removeFirstWhileIterating() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		it.next();

		Iterator<String> it2 = stack.iterator();
		it2.next();

		Iterator<String> it3 = stack.iterator();

		// Remove the first element
		it.remove();
		assert stack.size() == 1;

		assert it2.hasNext();
		assert it2.next().equals(FOO);
		assert !it2.hasNext();

		assert it3.hasNext();
		assert it3.next().equals(FOO);
		assert !it3.hasNext();
	}

	/** */
	@Test
	public void simpleRemove() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		it.next();
		it.next();

		// Remove the second element, FOO
		it.remove();
		assert stack.size() == 1;
	}

	/** */
	@Test
	public void simpleIteration() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		assert it.next().equals(BAR);
		assert it.next().equals(FOO);
	}

	/** */
	@Test
	public void removeLastWhileIterating() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		it.next();
		it.next();

		Iterator<String> it2 = stack.iterator();
		it2.next();

		Iterator<String> it3 = stack.iterator();

		// Remove the second element
		it.remove();
		assert stack.size() == 1;

		assert !it2.hasNext();

		assert it3.hasNext();
		assert it3.next().equals(BAR);
		assert !it3.hasNext();
	}

	/** */
	@Test
	public void removeThenAdd() throws Exception {
		stack.add(FOO);
		assert stack.size() == 1;

		Iterator<String> it = stack.iterator();
		it.next();
		it.remove();
		assert stack.size() == 0;

		stack.add(BAR);
		assert stack.size() == 1;

		Iterator<String> it2 = stack.iterator();
		assert it2.next().equals(BAR);
		assert !it2.hasNext();
	}

	/** */
	@Test
	public void removeBoth() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		Iterator<String> it2 = stack.iterator();
		
		it.next();
		it2.next();
		it2.next();
		
		it.remove();
		it2.remove();
		
		assert stack.size() == 0;
		assert !it.hasNext();
		assert !it2.hasNext();
	}

	/** */
	@Test
	public void removeBothOtherOrder() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		Iterator<String> it2 = stack.iterator();
		
		it.next();
		it2.next();
		it2.next();

		// Invert order
		it2.remove();
		it.remove();
		
		assert stack.size() == 0;
		assert !it.hasNext();
		assert !it2.hasNext();
	}

	/** */
	@Test
	public void removeBothVariation() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		it.next();
		
		Iterator<String> it2 = stack.iterator();
		it2.next();
		it2.next();
		it2.remove();

		it.remove();
		
		assert stack.size() == 0;
		assert !it.hasNext();
		assert !it2.hasNext();
	}

	/** */
	@Test
	public void removeBothVariationOtherOrder() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		Iterator<String> it = stack.iterator();
		it.next();
		it.next();
		
		Iterator<String> it2 = stack.iterator();
		it2.next();
		it2.remove();

		it.remove();
		
		assert stack.size() == 0;
		assert !it.hasNext();
		assert !it2.hasNext();
	}

	/** */
	@Test
	public void shuffle() throws Exception {
		stack.add(FOO);
		stack.add(BAR);
		assert stack.size() == 2;

		stack.shuffle(new Random());
		assert stack.size() == 2;
	}
}