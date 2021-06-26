package com.epam.esm.api.v1;

import com.epam.esm.domain.tag.Tag;
import com.epam.esm.domain.tag.validation.TagApiErrors;
import com.epam.esm.domain.tag.validation.TagValidator;
import com.epam.esm.service.TagService;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;
    private final TagValidator tagValidator;

    public TagController(TagService tagService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.tagValidator = tagValidator;
    }

    @GetMapping("/")
    public ResponseEntity<?> all() {
        return new ResponseEntity<>(this.tagService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable("id") int id) {
        try {
            return new ResponseEntity<>(this.tagService.getById(id), HttpStatus.OK);
        } catch (NotFoundException exception) {
            return new ResponseEntity<>("Tag is not found (id = " + id + ")", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> create(@RequestBody Tag tag) {
        final BindingResult bindingResult = ValidatorUtil.validate(tag, this.tagValidator);

        if (bindingResult.hasErrors()) {
            TagApiErrors result = new TagApiErrors(HttpStatus.BAD_REQUEST, TagApiErrors.DEFAULT_ERROR_MESSAGE, ValidatorUtil.getErrors(bindingResult));
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(this.tagService.save(tag), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        this.tagService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
