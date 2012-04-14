package unsuck.json.ser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.appengine.api.blobstore.BlobKey;

/**
 * Configuring this serializer will make BlobKey objects render as their web-safe string.
 */
public class BlobKeySerializer extends JsonSerializer<BlobKey> {

	@Override
	public void serialize(BlobKey value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(value.getKeyString());
	}
}
