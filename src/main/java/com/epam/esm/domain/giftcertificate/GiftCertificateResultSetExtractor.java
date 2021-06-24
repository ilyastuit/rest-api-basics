package com.epam.esm.domain.giftcertificate;

import com.epam.esm.domain.tag.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class GiftCertificateResultSetExtractor implements ResultSetExtractor<List<GiftCertificate>> {

    @Override
    public List<GiftCertificate> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, GiftCertificate> giftCertificates = new HashMap<>();

        while (resultSet.next()) {
            GiftCertificate giftCertificate;
            int id = resultSet.getInt("id");

            if (giftCertificates.containsKey(id)) {
                giftCertificate = giftCertificates.get(id);
            } else {
                giftCertificate = new GiftCertificate();
                extractRow(giftCertificate, resultSet);
                giftCertificates.put(id, giftCertificate);
            }

            int tagId = resultSet.getInt("tagId");

            if (tagId != 0) {
                Tag tag = new Tag(tagId);
                tag.setName(resultSet.getString("tagName"));
                giftCertificate.addTag(tag);
            }
        }

        return new ArrayList<>(giftCertificates.values());
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
