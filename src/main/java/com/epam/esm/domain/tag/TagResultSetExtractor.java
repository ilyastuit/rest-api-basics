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
        List<Tag> tags = new ArrayList<>();

        while (resultSet.next()) {
            Tag tag;
            tag = new Tag(resultSet.getInt("id"));
            tag.setName(resultSet.getString("name"));
            tags.add(tag);
        }
        return tags;
    }
}
