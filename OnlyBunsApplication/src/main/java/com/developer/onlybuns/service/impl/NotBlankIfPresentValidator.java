package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.service.NotBlankIfPresent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankIfPresentValidator implements ConstraintValidator<NotBlankIfPresent, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Ako nije poslato, validacija ga ignoriše
        }
        return !value.trim().isEmpty(); // Ako je poslato, ne sme biti prazno
    }
}

