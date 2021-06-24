package com.epam.esm.service;

import com.epam.esm.domain.tag.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.exceptions.NotFoundException;

import java.util.List;

public class TagService {

    private final TagRepository repository;

    public TagService(TagRepository repository) {
        this.repository = repository;
    }

    public Tag getById(int id) throws NotFoundException {
        return repository.findOne(id).stream().findAny().orElseThrow(() -> new NotFoundException("Tag is not found (id = "+ id +")"));
    }

    public List<Tag> getAll() {
        return repository.findAll();
    }
}
