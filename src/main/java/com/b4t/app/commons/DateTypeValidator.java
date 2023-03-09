package com.b4t.app.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTypeValidator implements ConstraintValidator<DateTypeValidate, Long> {
    private static final Logger logger = LoggerFactory.getLogger(DateTypeValidator.class);
    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return isValidFormat(value.toString(), "yyyyMMdd");
    }

    public boolean isValidFormat(String dateString, String pattern) {
        boolean valid = true;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            formatter.parse(dateString);
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage(), e);
            valid = false;
        }
        return valid;
    }
}
