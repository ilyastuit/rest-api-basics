package com.epam.esm.api.v1;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.validation.GiftCertificateApiErrors;
import com.epam.esm.service.CheckerUtil;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Validator giftCertificateValidator;

    public GiftCertificateController(GiftCertificateService giftCertificateService, Validator giftCertificateValidator) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> all(@RequestParam("tags") Optional<String> tags,
                                 @RequestParam("date") Optional<String> date,
                                 @RequestParam("name") Optional<String> name) {

        return new ResponseEntity<>(this.giftCertificateService.getAll(tags, date, name), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> one(@PathVariable("id") int id, @RequestParam("tags") Optional<String> tags) {
        boolean withTags = CheckerUtil.isValidBoolean(tags);

        try {
            return new ResponseEntity<>(this.giftCertificateService.getOne(id, withTags), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("GiftCertificate with id = " + id + " is not found.", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> search(@RequestParam("q") Optional<String> q,
                                    @RequestParam("tag") Optional<String> tag,
                                    @RequestParam("date") Optional<String> date,
                                    @RequestParam("name") Optional<String> name) {
        if (q.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByNameOrDescription(q.get(), date, name), HttpStatus.OK);
        } else if(tag.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByTagName(tag.get()), HttpStatus.OK);
        }
        return new ResponseEntity<>("Please provide the search parameter <q> or <tag>", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {

        final BindingResult bindingResult = validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            return returnApiErrors(bindingResult);
        }

        return new ResponseEntity<>(this.giftCertificateService.save(giftCertificate), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiftCertificate giftCertificate) {

        if (!this.giftCertificateService.isExistById(id)) {
            return new ResponseEntity<>("GiftCertificate with id = " + id + " is not found.", HttpStatus.NOT_FOUND);
        }

        final BindingResult bindingResult = validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            return returnApiErrors(bindingResult);
        }

        GiftCertificate result = null;
        try {
            result = this.giftCertificateService.getOne(this.giftCertificateService.update(id, giftCertificate), true);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("Error in the process of updating.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        this.giftCertificateService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> returnApiErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        for (ObjectError error : bindingResult.getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }

        GiftCertificateApiErrors result = new GiftCertificateApiErrors(HttpStatus.BAD_REQUEST, GiftCertificateApiErrors.DEFAULT_ERROR_MESSAGE, errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    private BindingResult validate(GiftCertificate giftCertificate, Validator validator) {
        final DataBinder dataBinder = new DataBinder(giftCertificate);
        dataBinder.addValidators(this.giftCertificateValidator);
        dataBinder.validate();
        return dataBinder.getBindingResult();
    }
}
