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
import java.util.Arrays;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;
import static uk.ac.ebi.ena.annotation.helper.exception.SAHErrorCode.QualifierTypeInvalidMessage;

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