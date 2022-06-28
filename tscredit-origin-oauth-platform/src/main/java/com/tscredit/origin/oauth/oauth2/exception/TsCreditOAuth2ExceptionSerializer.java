package com.tscredit.origin.oauth.oauth2.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;


public class TsCreditOAuth2ExceptionSerializer extends StdSerializer<TsCreditOAuth2Exception> {

    protected TsCreditOAuth2ExceptionSerializer() {
        super(TsCreditOAuth2Exception.class);
    }

    @Override
    public void serialize(TsCreditOAuth2Exception e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField("code", e.getHttpErrorCode());
        jsonGenerator.writeStringField("msg", e.getOAuth2ErrorCode());
        jsonGenerator.writeEndObject();
    }
}
