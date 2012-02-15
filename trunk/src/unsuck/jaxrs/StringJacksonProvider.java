package unsuck.jaxrs;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * This fixes what I interpret as broken behavior in Resteasy. For some stupid
 * reason, Resteasy refuses to JSON encode Strings returned from methods that
 * @Produces(MediaType.APPLICATION_JSON).
 * 
 * Register this provider and you can return String properly.
 */
@Provider
@Produces({MediaType.APPLICATION_JSON, MediaTypeUTF8.APPLICATION_JSON})
public class StringJacksonProvider implements MessageBodyWriter<String>
{
	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	@Override
	public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType)
	{
		return MediaType.APPLICATION_JSON_TYPE.equals(mediaType) && aClass == String.class;
	}

	/**
	 * Method that JAX-RS container calls to try to figure out serialized length
	 * of given value. Since computation of this length is about as expensive as
	 * serialization itself, implementation will return -1 to denote
	 * "not known", so that container will determine length from actual
	 * serialized output (if needed).
	 */
	@Override
	public long getSize(String value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType)
	{
		/*
		 * In general figuring output size requires actual writing; usually not
		 * worth it to write everything twice.
		 */
		return -1;
	}

	@Override
	public void writeTo(String value, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
	{
		try
		{
			MAPPER.writeValue(entityStream, value);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
