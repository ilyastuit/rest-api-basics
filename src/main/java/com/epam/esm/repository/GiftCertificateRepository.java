package com.epam.esm.repository;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.service.giftcertificate.GiftCertificateResultSetExtractor;
import com.epam.esm.entity.Tag;
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
public class GiftCertificateRepository {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final TagRepository tagRepository;

    public GiftCertificateRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, TransactionTemplate transactionTemplate, TagRepository tagRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.tagRepository = tagRepository;
    }

    public int save(MapSqlParameterSource params) {
        final String SQL = "INSERT INTO gifts.gift_certificate (name, description, price, duration, create_date, last_update_date) VALUES (:name, :description, :price, :duration, :create_date, :last_update_date)";
        return updateQuery(params, SQL);
    }

    public int update(Integer id, MapSqlParameterSource params) {
        final String SQL = "UPDATE gifts.gift_certificate SET name = :name, description = :description, price = :price, duration = :duration, last_update_date = :last_update_date WHERE id = :id";
        params.addValue("id", id);
        return updateQuery(params, SQL);
    }

    public boolean isExistById(int id) {
        String SQL = "SELECT gc.id FROM gifts.gift_certificate gc WHERE gc.id = ?";
        return this.jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(GiftCertificate.class), id).stream().findAny().orElse(null) != null;
    }

    public List<GiftCertificate> findById(int id) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date from gifts.gift_certificate gc WHERE gc.id=?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository), id);
    }

    public List<GiftCertificate> findAll() {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date from gifts.gift_certificate gc";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository));
    }

    public List<GiftCertificate> findByIdWithTags(int id) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.id=?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), id);
    }

    public List<GiftCertificate> findAllWithTags() {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags());
    }

    public List<GiftCertificate> findAllWithTagsByTagName(String tagName) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc LEFT JOIN gifts.gift_certificate_tag gct ON gct.gift_certificate_id = gc.id LEFT JOIN gifts.tag t ON gct.tag_id = t.id WHERE t.name = ?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), tagName);
    }

    public List<GiftCertificate> findAllByNameOrDescription(String text) {
        String likeText = prepareText(text);
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText);
    }

    public List<GiftCertificate> findAllWithTagsByNameOrDescription(String text) {
        String likeText = this.prepareText(text);
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ?";
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText);
    }

    public void deleteById(int id) {
        String SQL = "DELETE FROM gifts.gift_certificate WHERE id=?";
        this.jdbcTemplate.update(SQL, id);
    }

    public List<GiftCertificate> findAllWithTagsOrderByDate(String date) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.create_date " + date;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags());
    }

    public List<GiftCertificate> findAllOrderByDate(String date) {
        final String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.create_date " + date;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository));
    }

    public List<GiftCertificate> findAllWithTagsOrderByName(String name) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags());
    }

    public List<GiftCertificate> findAllOrderByName(String name) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository));
    }

    public List<GiftCertificate> findAllWithTagsOrderByDateAndByName(String date, String name) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.create_date " + date +", gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags());
    }

    public List<GiftCertificate> findAllOrderByDateAndByName(String date, String name) {
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc ORDER BY gc.create_date " + date +", gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository));
    }

    public List<GiftCertificate> findAllByNameOrDescriptionOrderByDateAndName(String text, String date, String name) {
        String likeText = this.prepareText(text);
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ORDER BY gc.create_date "+ date +", gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText);
    }

    public List<GiftCertificate> findAllByNameOrDescriptionOrderByName(String text, String name) {
        String likeText = this.prepareText(text);
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ORDER BY gc.name " + name;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText);
    }

    public List<GiftCertificate> findAllByNameOrDescriptionOrderByDate(String text, String date) {
        String likeText = this.prepareText(text);
        String SQL = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration, gc.create_date, gc.last_update_date FROM gifts.gift_certificate gc WHERE gc.name LIKE ? OR gc.description LIKE ? ORDER BY gc.create_date " + date;
        return jdbcTemplate.query(SQL, new GiftCertificateResultSetExtractor(tagRepository).withTags(), likeText, likeText);
    }

    private int updateQuery(MapSqlParameterSource params, String SQL) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        transactionTemplate.execute(status -> {
            namedParameterJdbcTemplate.update(SQL, params, keyHolder, new String[] {"id"});
            return null;
        });
        int certificateId = keyHolder.getKey().intValue();
        updateTags(params, certificateId);

        return certificateId;
    }

    private void updateTags(MapSqlParameterSource params, int certificateId) {
        try {
            List<Tag> tags = (List<Tag>) params.getValue("tags");
            for (Tag tag: tags) {
                int tagId;
                if (!this.tagRepository.isNameExist(tag.getName())) {
                    tagId = this.tagRepository.save(tag);
                } else {
                    tagId = this.tagRepository.getByName(tag.getName()).getId();
                }
                if (!this.tagRepository.isTagAlreadyAssignedToGiftCertificate(certificateId, tagId)) {
                    this.tagRepository.assignTagToGiftCertificate(certificateId, tagId);
                }
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    private String prepareText(String text) {
        return "%" + text + "%";
    }
}
