package com.epam.esm.service.giftcertificate;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    private boolean withTags = false;
    private final TagRepository tagRepository;

    public GiftCertificateResultSetExtractor(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        List<GiftCertificate> giftList = new ArrayList<>();
        List<Tag> tagList = new ArrayList<>();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            GiftCertificate giftCertificate = new GiftCertificate(id);
            extractRow(giftCertificate, resultSet);

            giftList.add(giftCertificate);
        }

        if (this.withTags) {
            for (GiftCertificate giftCertificate: giftList) {
                giftCertificate.setTags(this.tagRepository.getByGiftCertificateId(giftCertificate.getId()));
            }
        }

        return giftList;
    }

    public GiftCertificateResultSetExtractor withTags() {
        this.withTags = true;
        return this;
    }

    private void extractRow(GiftCertificate giftCertificate, ResultSet resultSet) throws SQLException {
        giftCertificate.setName(resultSet.getString("name"));
        giftCertificate.setDescription(resultSet.getString("description"));
        giftCertificate.setPrice(resultSet.getBigDecimal("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime());
    }
}
