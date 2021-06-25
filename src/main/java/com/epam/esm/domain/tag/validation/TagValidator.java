package com.epam.esm.domain.tag.validation;

import com.epam.esm.domain.tag.Tag;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Service
public class TagValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Tag.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Tag tag = (Tag) target;
        if (tag.getName() == null || tag.getName().isEmpty()) {
            errors.rejectValue("name", "name.required", "Name is required and should not be empty.");
        }
    }
}
