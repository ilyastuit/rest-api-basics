package com.epam.esm.repository;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.tag.TagResultSetExtractor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Repository
public class TagRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public TagRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate, TransactionTemplate transactionTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    public List<Tag> findOne(int id) {
        return jdbcTemplate.query("SELECT t.id, t.name FROM gifts.tag t WHERE t.id=?", new TagResultSetExtractor(), id);
    }

    public List<Tag> findAll() {
        return jdbcTemplate.query("SELECT t.id, t.name FROM gifts.tag t", new TagResultSetExtractor());
    }

    public int save(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String SQL = "INSERT INTO gifts.tag (name) VALUES (:name)";
        this.namedParameterJdbcTemplate.update(SQL, new MapSqlParameterSource().addValue("name",tag.getName()), keyHolder, new String[] {"id"});
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : 0;
    }

    public List<Tag> findByName(String name) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t WHERE t.name = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), name);
    }

    public List<Tag> findAssignedCertificateToTag(int certificateId, int tagId) {
        final String SQL = "SELECT gct.tag_id as id FROM gifts.gift_certificate_tag gct WHERE gct.gift_certificate_id = ? AND gct.tag_id = ?";
        return this.jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Tag.class), certificateId, tagId);
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        final String SQL = "INSERT INTO gifts.gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
        transactionTemplate.execute(status -> {
            jdbcTemplate.update(SQL, certificateId, tagId);
            return null;
        });
    }

    public List<Tag> findByGiftCertificateId(Integer certificateId) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON gct.tag_id = t.id LEFT JOIN gifts.gift_certificate gc ON gct.gift_certificate_id = gc.id WHERE gc.id = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), certificateId);
    }

    public void deleteById(int id) {
        final String firstSql = "DELETE FROM gifts.gift_certificate_tag WHERE tag_id = ?";
        final String secondSql = "DELETE FROM gifts.tag WHERE id = ?";
        transactionTemplate.execute(status -> {
            this.jdbcTemplate.update(firstSql, id);
            this.jdbcTemplate.update(secondSql, id);
            return null;
        });
    }
}
