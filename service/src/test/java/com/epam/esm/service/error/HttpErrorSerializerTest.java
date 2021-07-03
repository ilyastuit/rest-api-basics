package com.epam.esm.service.error;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpErrorSerializerTest {

    private static Writer jsonWriter = new StringWriter();
    private static JsonGenerator jsonGenerator;
    private static SerializerProvider serializerProvider;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
        serializerProvider = new ObjectMapper().getSerializerProvider();
    }

    @BeforeEach
    public void init() throws IOException {
        jsonGenerator.close();
        jsonWriter.close();
        jsonWriter = new StringWriter();
        jsonGenerator = new JsonFactory().createGenerator(jsonWriter);
    }

    @Test
    public void successNotFoundGiftCertificate() throws IOException {
        HttpErrorImpl httpError = new HttpErrorImpl("message", HttpStatus.NOT_FOUND, ErrorCode.GIFT_CERTIFICATE);
        new HttpErrorSerializer().serialize(httpError, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertEquals(jsonWriter.toString(), "{\"errorMessage\":\"message\",\"errorCode\":40401}");
    }

    @Test
    public void successNotFoundTag() throws IOException {
        HttpErrorImpl httpError = new HttpErrorImpl("message", HttpStatus.NOT_FOUND, ErrorCode.TAG);
        new HttpErrorSerializer().serialize(httpError, jsonGenerator, serializerProvider);
        jsonGenerator.flush();
        assertEquals(jsonWriter.toString(), "{\"errorMessage\":\"message\",\"errorCode\":40402}");
    }

}
