package com.epam.esm.repository.exceptions;

public class TagNameAlreadyExistException extends Exception{

    public TagNameAlreadyExistException(String name) {
        super("Tag name <"+ name +"> is already exist.");
    }
}
