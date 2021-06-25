package com.epam.esm.domain.giftcertificate.validation;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import com.epam.esm.domain.tag.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class GiftCertificateValidator implements Validator {

    @Qualifier("tagValidator")
    private final Validator tagValidator;

    public GiftCertificateValidator(Validator tagValidator) {
        this.tagValidator = tagValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificate.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name is required and should not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "description.empty", "Description is required and should not be empty.");

        GiftCertificate giftCertificate = (GiftCertificate) target;

        if (giftCertificate.getPrice() == null || giftCertificate.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
            errors.rejectValue("price", "price.required", "Price is required and should be positive.");
        }
        if (giftCertificate.getDuration() == null || giftCertificate.getDuration() <= 0) {
            errors.rejectValue("duration", "duration.required", "Duration is required and should be greater than zero.");
        }

        if (!giftCertificate.getTags().isEmpty()) {
            int i = 0;
            for(Tag tag : giftCertificate.getTags()) {
                try {
                    errors.pushNestedPath("tags["+ i++ +"]");
                    ValidationUtils.invokeValidator(this.tagValidator, tag, errors);
                } finally {
                    errors.popNestedPath();
                }
            }
        }
    }
}
