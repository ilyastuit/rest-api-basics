package com.epam.esm.service;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import com.epam.esm.service.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCertificateService {
    private final GiftCertificateRepository certificateRepository;

    public GiftCertificateService(GiftCertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public GiftCertificate getOne(int id) throws NotFoundException {
        return getFromList(certificateRepository.findById(id), id);
    }

    public GiftCertificate getOneWithTags(int id) throws NotFoundException {
        return getFromList(certificateRepository.findByIdWithTags(id), id);
    }

    public List<GiftCertificate> getAll() {
        return certificateRepository.findAll();
    }

    public List<GiftCertificate> getAllWithTags() {
        return certificateRepository.findAllWithTags();
    }

    public List<GiftCertificate> getAllByTagName(String tagName) {
        return certificateRepository.findAllByTagName(tagName);
    }

    public List<GiftCertificate> getAllByNameOrDescription(String text) {
        return certificateRepository.findAllByNameOrDescription(text);
    }

    public List<GiftCertificate> getAllByNameOrDescriptionWithTags(String text) {
        return certificateRepository.findAllByNameOrDescriptionWithTags(text);
    }

    private GiftCertificate getFromList(List<GiftCertificate> certificateList, int id) throws NotFoundException {
        return certificateList.stream().findAny().orElseThrow(() -> new NotFoundException("GiftCertificate is not found (id = " + id + ")"));
    }
}
