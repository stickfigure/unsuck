package unsuck.json.ser;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Configuring this serializer will make LocalDateTime objects render as an ISO8601 datetime string.
 * The default one in jackson renders as an array of values, no good.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

	@Override
	public void serialize(LocalDateTime value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
		jgen.writeString(ISODateTimeFormat.dateHourMinuteSecond().print(value.toDate().getTime()));
	}
}
