package com.epam.esm.api.v1;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.validation.GiftCertificateApiErrors;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
     *  Get list of all gift certificates (optional: with Tags).
     *  Sort by date, name or with both of them.
     *
     * @param tags Include tags (true|false)
     * @param date Sort by date (asc|desc)
     * @param name Sort by name (asc|desc)
     * @return List<GiftCertificate>
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
     * @return GiftCertificate
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id, @RequestParam("tags") Optional<String> tags) {
        boolean withTags = ValidatorUtil.isValidBoolean(tags);

        try {
            return new ResponseEntity<>(this.giftCertificateService.getOne(id, withTags), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("GiftCertificate with id = " + id + " is not found.", HttpStatus.NOT_FOUND);
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
     * @return List<GiftCertificate>
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
        return new ResponseEntity<>("Please provide the search parameter <q> or <tag>", HttpStatus.BAD_REQUEST);
    }

    /**
     * Create new GiftCertificate.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param giftCertificate Request body representation of GiftCertificate
     * @return int
     */
    @PostMapping(value = "/create")
    public ResponseEntity<?> create(@RequestBody GiftCertificate giftCertificate) {

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateApiErrors result = new GiftCertificateApiErrors(HttpStatus.BAD_REQUEST, GiftCertificateApiErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.giftCertificateService.save(giftCertificate), HttpStatus.CREATED);
    }

    /**
     * Update GiftCertificate by id.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param id Id of GiftCertificate to update.
     * @param giftCertificate GiftCertificate values.
     * @return GiftCertificate
     */
    @PatchMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody GiftCertificate giftCertificate) {

        if (!this.giftCertificateService.isExistById(id)) {
            return new ResponseEntity<>("GiftCertificate with id = " + id + " is not found.", HttpStatus.NOT_FOUND);
        }

        final BindingResult bindingResult = ValidatorUtil.validate(giftCertificate, this.giftCertificateValidator);

        if (bindingResult.hasErrors()) {
            GiftCertificateApiErrors result = new GiftCertificateApiErrors(HttpStatus.BAD_REQUEST, GiftCertificateApiErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        GiftCertificate result = null;
        try {
            result = this.giftCertificateService.getOne(this.giftCertificateService.update(id, giftCertificate), true);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("Error in the process of updating.", HttpStatus.INTERNAL_SERVER_ERROR);
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
