package com.epam.esm.service.tag;

import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exceptions.TagNameAlreadyExistException;
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
        return this.repository.findOne(id).stream().findAny().orElseThrow(() -> new NotFoundException("Tag is not found (id = "+ id +")"));
    }

    public List<Tag> getAll() {
        return this.repository.findAll();
    }

    public void deleteById(int id) {
        this.repository.deleteById(id);
    }

    public int save(Tag tag) throws TagNameAlreadyExistException {
        return this.repository.save(tag);
    }

    public List<Tag> getByGiftCertificateId(Integer certificateId) {
        return this.repository.findByGiftCertificateId(certificateId);
    }

    public boolean isExistByName(String name) {
        return checkIsListEmpty(this.repository.findByName(name));
    }

    public Tag getByName(String name) throws NotFoundException {
        return this.repository.findByName(name).stream().findAny().orElseThrow(() -> new NotFoundException("Tag is not found (name = "+ name + ")"));
    }

    public boolean isTagAlreadyAssignedToGiftCertificate(int certificateId, int tagId) {
        return checkIsListEmpty(this.repository.findAssignedTagToCertificate(certificateId, tagId));
    }

    private boolean checkIsListEmpty(List<Tag> list) {
        return list.stream().findAny().orElse(null) != null;
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        this.repository.assignTagToGiftCertificate(certificateId, tagId);
    }
}
