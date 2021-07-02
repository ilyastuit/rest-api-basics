package com.epam.esm.service.exceptions;

import java.sql.SQLDataException;

public class DataBaseException extends SQLDataException {
    public DataBaseException(String s) {
        super(s);
    }
}
