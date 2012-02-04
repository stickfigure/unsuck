/*
 */

package unsuck.gae;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

/**
 * TODO:  finish this someday
 *  
 * @author Jeff Schnitzer
 */
public class BlobCache
{
	/** */
	//private static final Logger log = LoggerFactory.getLogger(BlobCache.class);
	
	/** You implement this interface to fetch the raw data on a cache miss */
	public static interface Fetcher {
		InputStream fetch() throws IOException;
	}
	
	/** Separator to use in cache keys between normal key and data index */
	private static final String SEPARATOR = "~~~";
	
	/** Lets us know how many items to expect */
	Cache<String, Integer> metaCache;
	
	/** The actual data items */
	Cache<String, byte[]> dataCache;
	
	/** If non-null, expire all entries after this number of seconds */
	Integer expireSeconds;

	/** Create cache interface with default namespace */
	public BlobCache(String namespace)
	{
		this.metaCache = new Cache<String, Integer>(namespace);
		this.dataCache = new Cache<String, byte[]>(namespace);
	}
	
	/** Create cache with explicit namespace and an expiration period */
	public BlobCache(String namespace, int expireSeconds)
	{
		this(namespace);
		this.expireSeconds = expireSeconds;
	}
	
	/**
	 * Get the cached value, possibly fetching it from the fetcher.
	 */
	public InputStream get(String key, Fetcher fetcher) {
		// The master value is the # of parts
		Integer count = this.metaCache.get(key);
		if (count == null)
			return fetchAndCache(key, fetcher);
		else {
			List<String> dataKeys = new ArrayList<String>(count);
			for (int i=0; i<count; i++)
				dataKeys.add(makeDataKey(key, i));
			
			Map<String, byte[]> data = dataCache.getAll(dataKeys);
			if (data.size() != count)
				return fetchAndCache(key, fetcher);
			else
				return assemble(key, count, data);
		}
	}
	
	/** Make a key for a particular data index */
	private String makeDataKey(String key, int index) {
		return key + SEPARATOR + index;
	}
	
	/**
	 * 
	 */
	private InputStream fetchAndCache(String key, Fetcher fetcher) {
		return null;	
	}
	
	/**
	 * 
	 */
	private InputStream assemble(String key, int count, Map<String, byte[]> data) {
		assert count == data.size();
		
		List<InputSupplier<ByteArrayInputStream>> streams = Lists.newArrayList();
		for (int i=0; i<count; i++)
			streams.add(ByteStreams.newInputStreamSupplier(data.get(makeDataKey(key, i))));

		return null;
	}
}