package uk.ac.ebi.ena.annotation.helper.dto;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SVErrorCode.QualifierTypeInvalidMessage;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {QualifierValuesAllowed.Validator.class})
public @interface QualifierValuesAllowed {

    String message() default QualifierTypeInvalidMessage;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propName();

    String[] values();

    class Validator implements ConstraintValidator<QualifierValuesAllowed, String[]> {
        private String propName;
        private String message;
        private List<String> allowable;

        @Override
        public void initialize(QualifierValuesAllowed requiredIfChecked) {
            this.propName = requiredIfChecked.propName();
            this.message = requiredIfChecked.message();
            this.allowable = Arrays.asList(requiredIfChecked.values());
        }

        public boolean isValid(String[] value, ConstraintValidatorContext context) {

            Boolean valid = true;

            if (isEmpty(value)) {
                return valid;
            }

            for (String val :
                    value) {
                if (!isEmpty(val) && !this.allowable.contains(val)) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message.concat(this.allowable.toString()))
                        .addConstraintViolation();
            }
            return valid;
        }
    }
}