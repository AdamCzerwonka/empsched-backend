package com.example.empsched.employee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PictureValidator.class)
public @interface ValidPicture {
    String message() default "Invalid picture format or size. Picture cannot be empty and must be one of the following types: JPEG, PNG, BMP, WEBP, GIF, SVG.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
