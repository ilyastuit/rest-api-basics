package com.epam.esm.repository.tag;

import com.epam.esm.entity.tag.Tag;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
