package com.epam.esm.service;

import com.epam.esm.domain.tag.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public void deleteById(int id) {
        this.repository.deleteById(id);
    }

    public int save(Tag tag) {
        return this.repository.save(tag);
    }
}
