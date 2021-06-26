package com.epam.esm.api.v1;

import com.epam.esm.domain.tag.Tag;
import com.epam.esm.domain.tag.validation.TagValidationErrors;
import com.epam.esm.domain.tag.validation.TagValidator;
import com.epam.esm.service.TagService;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Rest Api Controller for Tags.
 */
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;

    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    /**
     * Get list of all Tags.
     *
     * @return List of all Tags.
     */
    @GetMapping("/")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(this.tagService.getAll(), HttpStatus.OK);
    }

    /**
     * Get Tag by id.
     *
     * @param id Id of Tag
     * @return Found Tag by id.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(this.tagService.getById(id), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("Tag is not found (id = " + id + ")", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create new Tag.
     * Values should pass validation otherwise Bad Request will be returned.
     *
     * @param tag Tag values
     * @return Id of created Tag.
     */
    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        final BindingResult bindingResult = ValidatorUtil.validate(tag, this.tagValidator);

        if (bindingResult.hasErrors()) {
            TagValidationErrors result = new TagValidationErrors(HttpStatus.BAD_REQUEST, TagValidationErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.tagService.save(tag), HttpStatus.CREATED);
    }

    /**
     * Delete Tag by id.
     *
     * @param id Id of Tag
     * @return null
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        this.tagService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
