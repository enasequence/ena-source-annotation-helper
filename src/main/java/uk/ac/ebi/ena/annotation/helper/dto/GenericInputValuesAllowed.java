/*
 * ******************************************************************************
 *  * Copyright 2021 EMBL-EBI, Hinxton outstation
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *****************************************************************************
 */

package uk.ac.ebi.ena.annotation.helper.dto;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.GenericInputInvalidMessage;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {GenericInputValuesAllowed.Validator.class})
public @interface GenericInputValuesAllowed {

    String message() default GenericInputInvalidMessage;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String propName();

    class Validator implements ConstraintValidator<GenericInputValuesAllowed, String> {
        private String propName;
        private String message;
        private char[] invalidChars = {'(', ')', ' ', '@', '{', '}', '[', ']'};

        @Override
        public void initialize(GenericInputValuesAllowed requiredIfChecked) {
            this.propName = requiredIfChecked.propName();
            this.message = requiredIfChecked.message();
        }

        public boolean isValid(String input, ConstraintValidatorContext context) {

            Boolean valid = true;
            String trimmedInput = input.trim();

            if (isEmpty(input)) {
                return valid;
            }

            for (char c :
                    invalidChars) {
                if (trimmedInput.indexOf(c) >= 0) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                        .addConstraintViolation();
            }
            return valid;
        }
    }
}