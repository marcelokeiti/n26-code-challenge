package com.n26.infra;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ZonedDateTimeDeserializer extends StdDeserializer<ZonedDateTime> {

    private static final long serialVersionUID = 6593673265418485951L;

    public ZonedDateTimeDeserializer() {
	this(null);
    }

    public ZonedDateTimeDeserializer(Class<?> clazz) {
	super(clazz);
    }

    @Override
    public ZonedDateTime deserialize(JsonParser parser, DeserializationContext ctx)
	    throws IOException, JsonProcessingException {
	long timestamp = parser.getLongValue();

	return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC);
    }

}
