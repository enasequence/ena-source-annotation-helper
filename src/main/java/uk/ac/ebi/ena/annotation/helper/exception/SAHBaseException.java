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

package uk.ac.ebi.ena.annotation.helper.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SAHBaseException extends RuntimeException {

    private HttpStatus httpStatus;
    private int errorCode;
    private int refId;

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode, Throwable exp) {
        super(message, exp);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public SAHBaseException(String message, HttpStatus httpStatus, int errorCode, int refId, Throwable exp) {
        super(message, exp);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.refId = refId;
    }

}
