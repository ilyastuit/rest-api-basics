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
        return certificateRepository.findOne(id).stream().findAny().orElseThrow(() -> new NotFoundException("GiftCertificate is not found (id = " + id + ")"));
    }

    public List<GiftCertificate> getAll() {
        return certificateRepository.findAll();
    }
}
