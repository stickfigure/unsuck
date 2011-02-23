/*
 * $Id: BeanMixin.java 1075 2009-05-07 06:41:19Z lhoriman $
 * $URL: https://subetha.googlecode.com/svn/branches/resin/rtest/src/org/subethamail/rtest/util/BeanMixin.java $
 */

package unsuck.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import unsuck.collections.FastLooseMap;

/**
 * Make sure the FastLooseMap behaves resonably.
 * @author Jeff Schnitzer
 */
public class FastLooseMapTests
{
	/** */
	@SuppressWarnings("unused")
	private static Logger log = LoggerFactory.getLogger(FastLooseMapTests.class);

	FastLooseMap<Integer, String> map;
	
	/** */
	@BeforeMethod
	public void setUp() throws Exception
	{
		this.map = new FastLooseMap<Integer, String>();
	}
	
	/** */
	@AfterMethod
	public void tearDown() throws Exception
	{
		this.map = null;
	}

	/** */
	@Test
	public void testAdd() throws Exception
	{
		map.put(1, "one");
		map.put(2, "two");
		
		assert map.size() == 2;
		assert map.get(1).equals("one");
		assert map.get(2).equals("two");
	}

	/** */
	@Test
	public void testRemove() throws Exception
	{
		map.put(1, "one");
		map.put(2, "two");
		
		map.remove(1);
		map.remove(2);
		
		assert map.size() == 0;
	}
}