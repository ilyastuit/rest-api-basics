package com.epam.esm.service;

import com.epam.esm.domain.ApiErrors;
import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.validation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ValidatorUtil {

    public final static String ASC = "ASC";
    public final static String DESC = "DESC";

    public static boolean isValidSort(Optional<String> value) {
        return value.isPresent() && (ValidatorUtil.ASC.equalsIgnoreCase(value.get()) || ValidatorUtil.DESC.equalsIgnoreCase(value.get()));
    }

    public static boolean isValidBoolean(Optional<String> value) {
        return value.isPresent() && Boolean.parseBoolean(value.get());
    }

    public static BindingResult validate(Object target, Validator validator) {
        final DataBinder dataBinder = new DataBinder(target);
        dataBinder.addValidators(validator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for (ObjectError error : bindingResult.getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        return errors;
    }

    public static void apiSerialize(ApiErrors apiErrors, JsonGenerator jgen) throws IOException {
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
