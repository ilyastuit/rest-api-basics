package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.giftcertificate.GiftCertificate;
import com.epam.esm.entity.giftcertificate.GiftCertificateDTO;
import com.epam.esm.entity.tag.Tag;
import com.epam.esm.entity.tag.TagDTO;
import com.epam.esm.service.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.service.exceptions.TagNameAlreadyExistException;
import com.epam.esm.repository.giftcertificate.GiftCertificateRepository;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.TagNotFoundException;
import com.epam.esm.service.tag.TagDTOMapper;
import com.epam.esm.service.tag.TagService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService {

    private final GiftCertificateRepository certificateRepository;
    private final TagService tagService;
    private final GiftCertificateDTOMapper dtoMapper;
    private final TagDTOMapper tagDTOMapper;

    public GiftCertificateService(GiftCertificateRepository certificateRepository, TagService tagService, GiftCertificateDTOMapper dtoMapper, TagDTOMapper tagDTOMapper) {
        this.certificateRepository = certificateRepository;
        this.tagService = tagService;
        this.dtoMapper = dtoMapper;
        this.tagDTOMapper = tagDTOMapper;
    }

    public int save(GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        giftCertificate.setCreateDate(LocalDateTime.now());

        MapSqlParameterSource params = prepareParams(giftCertificate);
        params.addValue("create_date", Timestamp.valueOf(giftCertificate.getCreateDate()));

        int id = this.certificateRepository.save(params);
        this.updateTags(id, giftCertificate.getTags());
        return id;
    }

    public int update(int id, GiftCertificateDTO giftCertificateDTO) throws TagNameAlreadyExistException {
        GiftCertificate giftCertificate = dtoMapper.giftCertificateDTOToGiftCertificate(giftCertificateDTO);

        this.updateTags(id, giftCertificate.getTags());
        return this.certificateRepository.update(id, prepareParams(giftCertificate));
    }

    public boolean isExistById(int id) {
        return getFromList(this.certificateRepository.findById(id)) != null;
    }

    public GiftCertificateDTO getOne(int id, boolean withTags) throws GiftCertificateNotFoundException {
        GiftCertificate giftCertificate = null;
        if (withTags) {
            giftCertificate = getFromList(this.certificateRepository.findByIdWithTags(id));
        } else {
            giftCertificate = getFromList(this.certificateRepository.findById(id));
        }

        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(id);
        }

        return dtoMapper.giftCertificateToGiftCertificateDTO(giftCertificate);
    }

    public GiftCertificateDTO getOneWithTags(int id) throws GiftCertificateNotFoundException {
        GiftCertificate giftCertificate = getFromList(this.certificateRepository.findByIdWithTags(id));

        if (giftCertificate == null) {
            throw new GiftCertificateNotFoundException(id);
        }

        return dtoMapper.giftCertificateToGiftCertificateDTO(giftCertificate);
    }

    public List<GiftCertificateDTO> getAll(Optional<String> tags, Optional<String> date, Optional<String> name) {
        boolean withTags = ValidatorUtil.isValidBoolean(tags);
        boolean sortByDate = ValidatorUtil.isValidSort(date);
        boolean sortByName = ValidatorUtil.isValidSort(name);

        if (sortByDate && !sortByName) {
            return this.getAllSortByDate(withTags, date.get());
        } else if (!sortByDate && sortByName) {
            return this.getAllSortByName(withTags, name.get());
        } else if (sortByDate && sortByName) {
            return this.getAllSortByDateAndByName(withTags, date.get(), name.get());
        } else {
            if (withTags) {
                return this.getAllWithTags();
            }
            return this.getAll();
        }
    }

    public List<GiftCertificateDTO> getAll() {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAll());
    }

    public List<GiftCertificateDTO> getAllSortByDate(boolean withTags, String date) {
        List<GiftCertificate> list = null;
        if (withTags) {
            list = this.certificateRepository.findAllWithTagsOrderByDate(date);
        } else {
            list = this.certificateRepository.findAllOrderByDate(date);
        }

        return dtoMapper.giftCertificateListToGiftCertificateDTOList(list);
    }


    public List<GiftCertificateDTO> getAllSortByName(boolean withTags, String name) {
        List<GiftCertificate> list = null;
        if (withTags) {
            list = this.certificateRepository.findAllWithTagsOrderByName(name);
        } else {
            list = this.certificateRepository.findAllOrderByName(name);
        }

        return dtoMapper.giftCertificateListToGiftCertificateDTOList(list);
    }

    public List<GiftCertificateDTO> getAllSortByDateAndByName(boolean withTags, String date, String name) {
        List<GiftCertificate> list = null;
        if (withTags) {
            list = this.certificateRepository.findAllWithTagsOrderByDateAndByName(date, name);
        } else {
            list = this.certificateRepository.findAllOrderByDateAndByName(date, name);
        }

        return dtoMapper.giftCertificateListToGiftCertificateDTOList(list);
    }

    public List<GiftCertificateDTO> getAllWithTags() {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllWithTags());
    }

    public List<GiftCertificateDTO> getAllByTagName(String tagName) {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllWithTagsByTagName(tagName));
    }

    public List<GiftCertificateDTO> getAllByNameOrDescription(String text, Optional<String> date, Optional<String> name) {
        boolean sortByDate = ValidatorUtil.isValidSort(date);
        boolean sortByName = ValidatorUtil.isValidSort(name);

        if (sortByDate && !sortByName) {
            return this.getAllByNameOrDescriptionSortByDate(text, date.get());
        } else if (!sortByDate && sortByName) {
            return this.getAllByNameOrDescriptionSortByName(text, name.get());
        } else if (sortByDate && sortByName) {
            return this.getAllByNameOrDescriptionSortByDateAndByName(text, date.get(), name.get());
        } else {
            return dtoMapper.giftCertificateListToGiftCertificateDTOList(certificateRepository.findAllByNameOrDescription(text));
        }
    }

    private List<GiftCertificateDTO> getAllByNameOrDescriptionSortByDateAndByName(String text, String date, String name) {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllByNameOrDescriptionOrderByDateAndName(text, date, name));
    }

    private List<GiftCertificateDTO> getAllByNameOrDescriptionSortByName(String text, String name) {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllByNameOrDescriptionOrderByName(text, name));
    }

    private List<GiftCertificateDTO> getAllByNameOrDescriptionSortByDate(String text, String date) {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllByNameOrDescriptionOrderByDate(text, date));
    }

    public List<GiftCertificateDTO> getAllByNameOrDescriptionWithTags(String text) {
        return dtoMapper.giftCertificateListToGiftCertificateDTOList(this.certificateRepository.findAllWithTagsByNameOrDescription(text));
    }

    public void delete(int id) {
        this.certificateRepository.deleteById(id);
    }

    private void updateTags(int certificateId, List<Tag> tags) throws TagNameAlreadyExistException {
        List<TagDTO> tagDTOList = tagDTOMapper.map(tags);
        try {
            for (TagDTO tagDTO: tagDTOList) {
                int tagId;
                if (!this.tagService.isExistByName(tagDTO.getName())) {
                    tagId = this.tagService.save(tagDTO);
                } else {
                    tagId = this.tagService.getByName(tagDTO.getName()).getId();
                }
                if (!this.tagService.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
                    this.tagService.assignTagToGiftCertificate(certificateId, tagId);
                }
            }
        } catch (TagNotFoundException ignored) {
        }
    }

    public void assignTagToCertificate(int certificateId, int tagId) {
        if (!this.tagService.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
            this.tagService.assignTagToGiftCertificate(certificateId, tagId);
        }
    }

    private GiftCertificate getFromList(List<GiftCertificate> certificateList) {
        return certificateList.stream().findAny().orElse(null);
    }

    private MapSqlParameterSource prepareParams(GiftCertificate giftCertificate) {
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", giftCertificate.getName());
        params.addValue("description", giftCertificate.getDescription());
        params.addValue("price", giftCertificate.getPrice());
        params.addValue("duration", giftCertificate.getDuration());
        params.addValue("last_update_date", Timestamp.valueOf(giftCertificate.getLastUpdateDate()));

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            params.addValue("tags", giftCertificate.getTags());
        }

        return params;
    }
}
