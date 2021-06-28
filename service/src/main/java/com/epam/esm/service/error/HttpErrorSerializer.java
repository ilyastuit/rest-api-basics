package com.epam.esm.service.error;

import com.epam.esm.service.ValidatorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class HttpErrorSerializer extends StdSerializer<HttpErrorImpl> {

    public HttpErrorSerializer() {
        this(null);
    }

    public HttpErrorSerializer(Class<HttpErrorImpl> t) {
        super(t);
    }

    @Override
    public void serialize(HttpErrorImpl httpError, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ValidatorUtil.serializeHttpError(httpError, jgen);
    }
}
