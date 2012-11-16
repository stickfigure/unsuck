/*
 */

package unsuck.test;

import java.util.Iterator;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import unsuck.collections.FailSafeLinkedCollection;

/**
 * @author Jeff Schnitzer
 */
public class FailSafeLinkedCollectionTests
{
	private static final String FOO = "foo";
	private static final String BAR = "bar";

	FailSafeLinkedCollection<String> list;

	/** */
	@BeforeMethod
	public void setUp() throws Exception {
		this.list = new FailSafeLinkedCollection<String>();
	}

	/** */
	@AfterMethod
	public void tearDown() throws Exception {
		this.list = null;
	}

	/** */
	@Test
	public void addWhileIterating() throws Exception {
		assert list.size() == 0;
		list.add(FOO);
		assert list.size() == 1;

		Iterator<String> it = list.iterator();
		assert it.hasNext();
		assert it.next().equals(FOO);
		assert !it.hasNext();

		list.add(BAR);
		assert list.size() == 2;
		assert it.hasNext();
		assert it.next().equals(BAR);
		assert !it.hasNext();
	}

	/** */
	@Test
	public void removeFirstWhileIterating() throws Exception {
		list.add(FOO);
		list.add(BAR);
		assert list.size() == 2;

		Iterator<String> it = list.iterator();
		it.next();

		Iterator<String> it2 = list.iterator();
		it2.next();

		Iterator<String> it3 = list.iterator();

		// Remove the first element
		it.remove();
		assert list.size() == 1;

		assert it2.hasNext();
		assert it2.next().equals(BAR);
		assert !it2.hasNext();

		assert it3.hasNext();
		assert it3.next().equals(BAR);
		assert !it3.hasNext();
	}

	/** */
	@Test
	public void removeLastWhileIterating() throws Exception {
		list.add(FOO);
		list.add(BAR);
		assert list.size() == 2;

		Iterator<String> it = list.iterator();
		it.next();
		it.next();

		Iterator<String> it2 = list.iterator();
		it2.next();

		Iterator<String> it3 = list.iterator();

		// Remove the second element
		it.remove();
		assert list.size() == 1;

		assert !it2.hasNext();

		assert it3.hasNext();
		assert it3.next().equals(FOO);
		assert !it3.hasNext();
	}
}