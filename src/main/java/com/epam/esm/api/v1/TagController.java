package com.epam.esm.api.v1;

import com.epam.esm.service.TagService;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        this.tagService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
