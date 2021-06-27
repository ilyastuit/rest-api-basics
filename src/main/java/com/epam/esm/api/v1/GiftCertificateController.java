package com.epam.esm.api.v1;

import com.epam.esm.service.error.ErrorCode;
import com.epam.esm.service.error.HttpError;
import com.epam.esm.service.error.HttpErrorImpl;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.giftcertificate.validation.GiftCertificateValidationErrors;
import com.epam.esm.service.giftcertificate.GiftCertificateService;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Rest Api Controller for GiftCertificate.
 */
@RestController
@RequestMapping("/api/v1/gift-certificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;
    private final Validator giftCertificateValidator;

    public GiftCertificateController(GiftCertificateService giftCertificateService, Validator giftCertificateValidator) {
        this.giftCertificateService = giftCertificateService;
        this.giftCertificateValidator = giftCertificateValidator;
    }

    /**
     *  Get list of all GiftCertificates (optional: with Tags).
     *  Sort by date, name or with both of them.
     *
     * @param tags Include tags (true|false)
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List of all GiftCertificates.
     */
    @GetMapping(value = "/")
    public ResponseEntity<?> all(@RequestParam("tags") Optional<String> tags,
                                 @RequestParam("date") Optional<String> date,
                                 @RequestParam("name") Optional<String> name) {

        return new ResponseEntity<>(this.giftCertificateService.getAll(tags, date, name), HttpStatus.OK);
    }

    /**
     * Get Gift Certificate by id (optional: with tags).
     *
     * @param id id of the GiftCertificate
     * @param tags Include Tags (true|false)
     * @return One GiftCertificate or Not Found if not.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id, @RequestParam("tags") Optional<String> tags) {
        boolean withTags = ValidatorUtil.isValidBoolean(tags);

        try {
            return new ResponseEntity<>(this.giftCertificateService.getOne(id, withTags), HttpStatus.OK);
        } catch (NotFoundException exception) {
            HttpError httpError = new HttpErrorImpl(HttpStatus.NOT_FOUND, exception.getMessage(), ErrorCode.GiftCertificate);
            return new ResponseEntity<>(httpError, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Search GiftCertificates matching by name or description.
     * Search GiftCertificates matching by Tag name.
     * Sort by date, name or with both of them.
     *
     * @param q Search parameter
     * @param tag Tag name
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List of found GiftCertificates.
     */
    @GetMapping(value = "/search")
    public ResponseEntity<?> search(@RequestParam("q") Optional<String> q,
                                    @RequestParam("tag") Optional<String> tag,
                                    @RequestParam("date") Optional<String> date,
                                    @RequestParam("name") Optional<String> name) {
        if (q.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByNameOrDescription(q.get(), date, name), HttpStatus.OK);
        } else if(tag.isPresent()) {
            return new ResponseEntity<>(this.giftCertificateService.getAllByTagName(tag.get()), HttpStatus.OK);
        }
        HttpError httpError = new HttpErrorImpl(HttpStatus.BAD_REQUEST, "Please provide the search parameter <q> or <tag>", ErrorCode.GiftCertificate);
        return new ResponseEntity<>(httpError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Create new GiftCertificate.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param giftCertificate Request body representation of GiftCertificate
     * @return Id of created GiftCertificate.
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(HttpStatus.BAD_REQUEST, GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        try {
            return new ResponseEntity<>(this.giftCertificateService.save(giftCertificate), HttpStatus.CREATED);
        } catch (NotFoundException exception) {
            HttpError httpError = new HttpErrorImpl(HttpStatus.NOT_FOUND, exception.getMessage(), ErrorCode.GiftCertificate);
            return new ResponseEntity<>(httpError, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update GiftCertificate by id.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param id Id of GiftCertificate to update.
     * @param giftCertificate GiftCertificate values.
     * @return Id of updated GiftCertificate.
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiftCertificate giftCertificate) {

        if (!this.giftCertificateService.isExistById(id)) {
            HttpError httpError = new HttpErrorImpl(HttpStatus.NOT_FOUND, "GiftCertificate with id = " + id + " is not found.", ErrorCode.GiftCertificate);
            return new ResponseEntity<>(httpError, HttpStatus.NOT_FOUND);
        }

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateValidationErrors result = new GiftCertificateValidationErrors(HttpStatus.BAD_REQUEST, GiftCertificateValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        GiftCertificate result = null;
        try {
            result = this.giftCertificateService.getOne(this.giftCertificateService.update(id, giftCertificate), true);
        } catch (NotFoundException exception) {
            HttpError httpError = new HttpErrorImpl(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), ErrorCode.GiftCertificate);
            return new ResponseEntity<>(httpError, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Delete GiftCertificate by id.
     *
     * @param id Id of GiftCertificate to delete
     * @return null
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        this.giftCertificateService.delete(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
