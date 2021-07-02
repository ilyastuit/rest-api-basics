package com.epam.esm.service.tag;

import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository repository;
    private final TagDTOMapper dtoMapper;

    public TagService(TagRepository repository, TagDTOMapper dtoMapper) {
        this.repository = repository;
        this.dtoMapper = dtoMapper;
    }

    public TagDTO getById(int id) throws NotFoundException {
        Tag tag = getFromList(this.repository.findById(id));

        if (tag == null) {
            throw new NotFoundException("Tag is not found (id = " + id + ")");
        }
        return dtoMapper.tagToDTO(tag);
    }

    public List<TagDTO> getAll() {
        return dtoMapper.map(this.repository.findAll());
    }

    public void deleteById(int id) {
        this.repository.deleteById(id);
    }

    public int save(TagDTO tagDTO) throws TagNameAlreadyExistException {
        int id = 0;
        try {
            id = this.repository.save(dtoMapper.dtoToTag(tagDTO));
        } catch (DuplicateKeyException exception) {
            throw new TagNameAlreadyExistException(exception.getMessage());
        }

        return id;
    }

    public List<TagDTO> getByGiftCertificateId(Integer certificateId) {
        return dtoMapper.map(this.repository.findByGiftCertificateId(certificateId));
    }

    public boolean isExistByName(String name) {
        return getFromList(this.repository.findByName(name)) != null;
    }

    public TagDTO getByName(String name) throws NotFoundException {
        Tag tag = getFromList(this.repository.findByName(name));

        if (tag == null) {
            throw new NotFoundException("Tag is not found (name = " + name + ")");
        }

        return dtoMapper.tagToDTO(tag);
    }

    public boolean isTagAlreadyAssignedToGiftCertificate(int certificateId, int tagId) {
        return getFromList(this.repository.findAssignedTagToCertificate(certificateId, tagId)) != null;
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        this.repository.assignTagToGiftCertificate(certificateId, tagId);
    }

    private Tag getFromList(List<Tag> tags) {
        return tags.stream().findAny().orElse(null);
    }
}
