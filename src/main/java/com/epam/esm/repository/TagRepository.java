package com.epam.esm.repository;

import com.epam.esm.domain.tag.Tag;
import com.epam.esm.domain.tag.TagResultSetExtractor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TagRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
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

    public boolean isNameExist(String name) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t WHERE t.name = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), name).stream().findAny().orElse(null) != null;
    }

    public Tag getByName(String name) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t WHERE t.name = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), name).stream().findAny().orElse(null);
    }

    public boolean isTagAlreadyAssignedToGiftCertificate(int certificateId, int tagId) {
        final String SQL = "SELECT gct.tag_id as id FROM gifts.gift_certificate_tag gct WHERE gct.gift_certificate_id = ? AND gct.tag_id = ?";
        return this.jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Tag.class), certificateId, tagId).stream().findAny().orElse(null) != null;
    }

    public void assignTagToGiftCertificate(int certificateId, int tagId) {
        final String SQL = "INSERT INTO gifts.gift_certificate_tag (gift_certificate_id, tag_id) VALUES (?, ?)";
        jdbcTemplate.update(SQL, certificateId, tagId);
    }

    public List<Tag> getByGiftCertificateId(Integer certificateId) {
        final String SQL = "SELECT t.id, t.name FROM gifts.tag t LEFT JOIN gifts.gift_certificate_tag gct ON gct.tag_id = t.id LEFT JOIN gifts.gift_certificate gc ON gct.gift_certificate_id = gc.id WHERE gc.id = ?";
        return this.jdbcTemplate.query(SQL, new TagResultSetExtractor(), certificateId);
    }

    public void deleteById(int id) {
        String SQL = "DELETE FROM gifts.gift_certificate_tag WHERE tag_id = ?";
        this.jdbcTemplate.update(SQL, id);
        SQL = "DELETE FROM gifts.tag WHERE id = ?";
        this.jdbcTemplate.update(SQL, id);
    }
}
