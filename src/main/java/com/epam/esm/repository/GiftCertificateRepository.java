package com.epam.esm.repository;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.GiftCertificateMapper;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;

    public GiftCertificateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<GiftCertificate> findOne(int id) {
        return jdbcTemplate.query("SELECT * FROM gifts.gift_certificate WHERE id=?", new GiftCertificateMapper(), id);
    }

    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query("SELECT * FROM gifts.gift_certificate", new GiftCertificateMapper());
    }
}
