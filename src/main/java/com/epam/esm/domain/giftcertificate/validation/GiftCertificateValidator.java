package com.epam.esm.domain.giftcertificate.validation;

import com.epam.esm.domain.giftcertificate.GiftCertificate;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@Service
public class GiftCertificateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return GiftCertificate.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty", "Name should not be empty.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "name.description", "Description should not be empty.");

        GiftCertificate giftCertificate = (GiftCertificate) target;

        if (giftCertificate.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
            errors.rejectValue("price", "price.negativevalue", "Price should be positive.");
        }
        if (giftCertificate.getDuration() <= 0) {
            errors.rejectValue("duration", "duration.lessthanzero", "Duration should be greater than zero.");
        }
    }
}
