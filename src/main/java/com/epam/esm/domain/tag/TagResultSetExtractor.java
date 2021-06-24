package com.epam.esm.domain.tag;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagResultSetExtractor implements ResultSetExtractor<List<Tag>> {

    @Override
    public List<Tag> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<Integer, Tag> tags = new HashMap<>();

        while (resultSet.next()) {
            Tag tag;

            int id = resultSet.getInt("id");
            if (tags.containsKey(id)) {
                tag = tags.get(id);
            } else {
                tag = new Tag(resultSet.getInt("id"));
                tag.setName(resultSet.getString("name"));
                tags.put(tag.getId(), tag);
            }

            int certificateId = resultSet.getInt("gcId");
            if (certificateId != 0) {
                GiftCertificate giftCertificate = new GiftCertificate(certificateId);
                extractCertificate(giftCertificate, resultSet);
                tag.addCertificate(giftCertificate);
            }
        }
        return new ArrayList<>(tags.values());
    }

    private void extractCertificate(GiftCertificate giftCertificate, ResultSet resultSet) throws SQLException {
        giftCertificate.setName(resultSet.getString("gcName"));
        giftCertificate.setDescription(resultSet.getString("gcDescription"));
        giftCertificate.setPrice(resultSet.getBigDecimal("price"));
        giftCertificate.setDuration(resultSet.getInt("duration"));
        giftCertificate.setCreateDate(resultSet.getTimestamp("create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp("last_update_date").toLocalDateTime());
    }
}
