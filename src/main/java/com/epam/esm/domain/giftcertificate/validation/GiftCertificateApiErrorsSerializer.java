package com.epam.esm.domain.giftcertificate.validation;

import com.epam.esm.service.ValidatorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GiftCertificateApiErrorsSerializer extends StdSerializer<GiftCertificateApiErrors> {

    public GiftCertificateApiErrorsSerializer() {
        this(null);
    }

    public GiftCertificateApiErrorsSerializer(Class<GiftCertificateApiErrors> t) {
        super(t);
    }

    @Override
    public void serialize(GiftCertificateApiErrors apiErrors, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ValidatorUtil.apiSerialize(apiErrors, jgen);
    }
}
