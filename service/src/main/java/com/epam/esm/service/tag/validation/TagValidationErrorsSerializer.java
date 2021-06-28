package com.epam.esm.service.tag.validation;

import com.epam.esm.service.ValidatorUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TagValidationErrorsSerializer extends StdSerializer<TagValidationErrors> {

    public TagValidationErrorsSerializer() {
        this(null);
    }

    public TagValidationErrorsSerializer(Class<TagValidationErrors> t) {
        super(t);
    }

    @Override
    public void serialize(TagValidationErrors apiErrors, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        ValidatorUtil.serializeValidationError(apiErrors, jgen);
    }
}
