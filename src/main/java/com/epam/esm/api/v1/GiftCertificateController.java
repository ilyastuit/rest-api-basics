package com.epam.esm.api.v1;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.validation.GiftCertificateApiErrors;
import com.epam.esm.service.GiftCertificateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Validator giftCertificateValidator;

    @Autowired
    public GiftCertificateController(GiftCertificateService giftCertificateService, Validator giftCertificateValidator) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> all(@RequestParam("withTags") Optional<String> withTags){
        if (withTags.isPresent() && Boolean.parseBoolean(withTags.get())) {
            return new ResponseEntity<>(giftCertificateService.getAllWithTags(), HttpStatus.OK);
        }
        return new ResponseEntity<>(giftCertificateService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {

        final DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(this.giftCertificateValidator);
        dataBinder.validate();

        if (dataBinder.getBindingResult().hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            for(FieldError fieldError : dataBinder.getBindingResult().getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            for (ObjectError error : dataBinder.getBindingResult().getGlobalErrors()) {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }

            GiftCertificateApiErrors result = new GiftCertificateApiErrors(HttpStatus.BAD_REQUEST, GiftCertificateApiErrors.DEFAULT_ERROR_MESSAGE, errors);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(giftCertificate, HttpStatus.OK);
    }

}
