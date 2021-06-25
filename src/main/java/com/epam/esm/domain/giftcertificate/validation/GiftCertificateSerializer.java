package com.epam.esm.domain.giftcertificate.validation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class GiftCertificateSerializer extends StdSerializer<GiftCertificateApiErrors> {

    public GiftCertificateSerializer() {
        this(null);
    }

    public GiftCertificateSerializer(Class<GiftCertificateApiErrors> t) {
        super(t);
    }

    @Override
    public void serialize(GiftCertificateApiErrors apiErrors, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("status", apiErrors.getStatus().value());
        jgen.writeStringField("errorMessage", apiErrors.getMessage());

        jgen.writeFieldName("errors");
        jgen.writeStartArray();
        jgen.writeStartObject();

        Map<String, String> data = apiErrors.getErrors();

        for (Map.Entry<String, String> error: data.entrySet()) {
            jgen.writeStringField(error.getKey(), error.getValue());
        }

        jgen.writeEndObject();
        jgen.writeEndArray();

        jgen.writeEndObject();
    }
}
