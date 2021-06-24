package com.epam.esm.repository;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.GiftCertificateResultSetExtractor;
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
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE gc.id=?", new GiftCertificateResultSetExtractor(), id);
    }

    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id", new GiftCertificateResultSetExtractor());
    }
}
