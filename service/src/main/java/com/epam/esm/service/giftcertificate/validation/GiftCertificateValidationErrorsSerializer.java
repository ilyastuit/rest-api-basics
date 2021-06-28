package com.epam.esm.service.giftcertificate.validation;

import com.epam.esm.service.ValidatorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GiftCertificateValidationErrorsSerializer extends StdSerializer<GiftCertificateValidationErrors> {

    public GiftCertificateValidationErrorsSerializer() {
        this(null);
    }

    public GiftCertificateValidationErrorsSerializer(Class<GiftCertificateValidationErrors> t) {
        super(t);
    }

    @Override
    public void serialize(GiftCertificateValidationErrors apiErrors, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ValidatorUtil.serializeValidationError(apiErrors, jgen);
    }
}
