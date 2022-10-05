/**
 * Copyright (C) 2006-2021 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Injectable, Injector} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {MetaResponse} from "../models/MetaResponse";
import {Institution} from "../models/Institution";
import {Collection} from "../models/Collection";
import {AppConstants} from "../app.constants";
import {ErrorService} from "./error.service";
import {LoggingService} from "./logging.service";


@Injectable({
    providedIn: 'root'
})
export class InstitutionService {

    errorMessage: string = "";
    errorMessageInst: string = "";
    errorMessageColl: string = "";

    constructor(private http: HttpClient,
                private injector: Injector) {
    }

    /**
     *
     * @param instVal
     * @param qualifierArray
     */
    findByInstitutionValue(instVal: string, qualifier: string): Observable<Institution[]> {
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'institution/' + instVal;
        if (qualifier !== "") {
            urlString = urlString + '?qualifier_type=' + qualifier;
        }
        // alert(urlString);
        return this.http.get<MetaResponse>(urlString, options)
            .pipe(
                map(response => {
                        if (response.institutions.length <= 0) {
                            this.errorMessageInst = AppConstants.NO_INSTITUTIONS_FOUND;
                            this.handleError(new Error(AppConstants.NO_INSTITUTIONS_FOUND));
                            //alert(this.errorMessage);
                        } else {
                            this.errorMessageInst = "";
                            this.errorMessage = "";
                        }
                        return response.institutions;
                    }
                ));
    }

    /**
     *
     * @param instVal
     * @param qualifierArray
     */
    findByAllCollByInstituteUniqueName(instVal: string, qualifier: string): Observable<Collection[]> {
        var filterdCollections: Collection[] = [] as any;
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'institution/' + instVal + '/collection';
        if (qualifier !== "") {
            urlString = urlString + '?qualifier_type=' + qualifier;
        }
        // alert(urlString);
        return this.http.get<MetaResponse>(urlString, options)
            .pipe(
                map(response => {
                        if (response.institutions[0].collections.length <= 0) {
                            this.errorMessageColl = AppConstants.NO_COLLECTIONS_FOUND;
                            this.handleError(new Error(AppConstants.NO_COLLECTIONS_FOUND));
                        } else {
                            this.errorMessageColl = "";
                            this.errorMessage = "";
                        }
                        return response.institutions[0] ? response.institutions[0].collections : [];
                    }
                ));
    }

    handleError(error: Error | HttpErrorResponse) {
        const errorService = this.injector.get(ErrorService);
        const logger = this.injector.get(LoggingService);
        let message;
        if (error instanceof HttpErrorResponse) {
            // Server error
            this.errorMessage = errorService.getServerErrorMessage(error);
        } else {
            // Client Error
            this.errorMessage = errorService.getClientErrorMessage(error);
        }
        // log errors
        // logger.logError(this.errorMessage, "");
    }
}
