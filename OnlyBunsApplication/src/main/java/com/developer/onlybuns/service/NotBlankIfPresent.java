package com.developer.onlybuns.service;
import com.developer.onlybuns.service.impl.NotBlankIfPresentValidator;
import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
public @interface NotBlankIfPresent {
    String message() default "Polje ne može biti prazno ako se menja.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
