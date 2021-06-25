package com.epam.esm.repository;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.giftcertificate.GiftCertificateResultSetExtractor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GiftCertificateRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<GiftCertificate> findById(int id) {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date from gifts.gift_certificate gc WHERE gc.id=?", new GiftCertificateResultSetExtractor(false), id);
    }

    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date from gifts.gift_certificate gc", new GiftCertificateResultSetExtractor(false));
    }

    public List<GiftCertificate> findByIdWithTags(int id) {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE gc.id=?", new GiftCertificateResultSetExtractor(true), id);
    }

    public List<GiftCertificate> findAllWithTags() {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id", new GiftCertificateResultSetExtractor(true));
    }

    public List<GiftCertificate> findAllByTagName(String tagName) {
        return jdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE t.name = ?", new GiftCertificateResultSetExtractor(true), tagName);
    }

    public List<GiftCertificate> findAllByNameOrDescription(String text) {
        Map<String, String> params = new HashMap<>();
        params.put("text", "%" + text + "%");
        return namedParameterJdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE :text OR gc.description LIKE :text", params, new GiftCertificateResultSetExtractor(false));
    }

    public List<GiftCertificate> findAllByNameOrDescriptionWithTags(String text) {
        Map<String, String> params = new HashMap<>();
        params.put("text", "%" + text + "%");
        return namedParameterJdbcTemplate.query("SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date, t.id as tagId, t.name as tagName FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE gc.name LIKE :text OR gc.description LIKE :text", params, new GiftCertificateResultSetExtractor(true));
    }
}
