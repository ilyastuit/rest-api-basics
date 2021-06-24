package com.epam.esm.domain.giftcertificate;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GiftCertificateMapper implements RowMapper<GiftCertificate> {

    @Override
    public GiftCertificate mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate(resultSet.getInt("gc.id"));

        giftCertificate.setName(resultSet.getString("gc.name"));
        giftCertificate.setDescription(resultSet.getString("gc.description"));
        giftCertificate.setPrice(resultSet.getBigDecimal("gc.price"));
        giftCertificate.setDuration(resultSet.getInt("gc.duration"));

        giftCertificate.setCreateDate(resultSet.getTimestamp("gc.create_date").toLocalDateTime());
        giftCertificate.setLastUpdateDate(resultSet.getTimestamp("gc.last_update_date").toLocalDateTime());

        return giftCertificate;
    }
}
