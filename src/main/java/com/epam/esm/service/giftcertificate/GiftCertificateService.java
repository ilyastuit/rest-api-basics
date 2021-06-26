package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.ValidatorUtil;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;

    public GiftCertificateService(GiftCertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public int save(GiftCertificate giftCertificate) {
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", giftCertificate.getName());
        params.addValue("description", giftCertificate.getDescription());
        params.addValue("price", giftCertificate.getPrice());
        params.addValue("duration", giftCertificate.getDuration());
        params.addValue("create_date", Timestamp.valueOf(giftCertificate.getCreateDate()));
        params.addValue("last_update_date", Timestamp.valueOf(giftCertificate.getLastUpdateDate()));

        if (giftCertificate.getTags() != null && !giftCertificate.getTags().isEmpty()) {
            params.addValue("tags", giftCertificate.getTags());
        }

        return this.certificateRepository.save(params);
    }

    public int update(int id, GiftCertificate giftCertificate) {
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

        return this.certificateRepository.update(id, params);
    }

    public boolean isExistById(int id) {
        return this.certificateRepository.isExistById(id);
    }

    public GiftCertificate getOne(int id, boolean withTags) throws NotFoundException {
        if (withTags) {
            return getFromList(this.certificateRepository.findByIdWithTags(id), id);
        }
        return getFromList(this.certificateRepository.findById(id), id);
    }

    public GiftCertificate getOneWithTags(int id) throws NotFoundException {
        return getFromList(this.certificateRepository.findByIdWithTags(id), id);
    }

    public List<GiftCertificate> getAll(Optional<String> tags, Optional<String> date, Optional<String> name) {
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
            return this.certificateRepository.findAll();
        }
    }


    public List<GiftCertificate> getAllSortByDate(boolean withTags, String date) {
        if (withTags) {
            return this.certificateRepository.findAllWithTagsOrderByDate(date);
        }
        return this.certificateRepository.findAllOrderByDate(date);
    }


    public List<GiftCertificate> getAllSortByName(boolean withTags, String name) {
        if (withTags) {
            return this.certificateRepository.findAllWithTagsOrderByName(name);
        }
        return this.certificateRepository.findAllOrderByName(name);
    }

    public List<GiftCertificate> getAllSortByDateAndByName(boolean withTags, String date, String name) {
        if (withTags) {
            return this.certificateRepository.findAllWithTagsOrderByDateAndByName(date, name);
        }
        return this.certificateRepository.findAllOrderByDateAndByName(date, name);
    }

    public List<GiftCertificate> getAllWithTags() {
        return this.certificateRepository.findAllWithTags();
    }

    public List<GiftCertificate> getAllByTagName(String tagName) {
        return this.certificateRepository.findAllWithTagsByTagName(tagName);
    }

    public List<GiftCertificate> getAllByNameOrDescription(String text, Optional<String> date, Optional<String> name) {
        boolean sortByDate = ValidatorUtil.isValidSort(date);
        boolean sortByName = ValidatorUtil.isValidSort(name);

        if (sortByDate && !sortByName) {
            return this.getAllByNameOrDescriptionSortByDate(text, date.get());
        } else if (!sortByDate && sortByName) {
            return this.getAllByNameOrDescriptionSortByName(text, name.get());
        } else if (sortByDate && sortByName) {
            return this.getAllByNameOrDescriptionSortByDateAndByName(text, date.get(), name.get());
        } else {
            return certificateRepository.findAllByNameOrDescription(text);
        }
    }

    private List<GiftCertificate> getAllByNameOrDescriptionSortByDateAndByName(String text, String date, String name) {
        return this.certificateRepository.findAllByNameOrDescriptionOrderByDateAndName(text, date, name);
    }

    private List<GiftCertificate> getAllByNameOrDescriptionSortByName(String text, String name) {
        return this.certificateRepository.findAllByNameOrDescriptionOrderByName(text, name);
    }

    private List<GiftCertificate> getAllByNameOrDescriptionSortByDate(String text, String date) {
        return this.certificateRepository.findAllByNameOrDescriptionOrderByDate(text, date);
    }

    public List<GiftCertificate> getAllByNameOrDescriptionWithTags(String text) {
        return this.certificateRepository.findAllWithTagsByNameOrDescription(text);
    }

    private GiftCertificate getFromList(List<GiftCertificate> certificateList, int id) throws NotFoundException {
        return certificateList.stream().findAny().orElseThrow(() -> new NotFoundException("GiftCertificate is not found (id = " + id + ")"));
    }

    public void delete(int id) {
        this.certificateRepository.deleteById(id);
    }
}
