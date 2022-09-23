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

import {Injectable} from '@angular/core';
import {Observable, of, Subject} from "rxjs";
import {Alert, AlertType} from "../models/Alert";

@Injectable({
    providedIn: 'root'
})
export class SahCommonsService {

    private subject = new Subject<Alert>();
    private keepAfterRouteChange = false;

    constructor() {
    }

    /**
     * Handle Http operation that failed.
     * Let the app continue.
     * @param operation - name of the operation that failed
     * @param result - optional value to return as the observable result
     */
    public handleError<T>(operation = 'operation', result?: T) {
        return (error: any): Observable<T> => {

            // TODO: send the error to remote logging infrastructure
            console.error(error); // log to console instead

            // TODO: better job of transforming error for user consumption
            this.log(`${operation}: ${error.error.message}`);

            // Let the app keep running by returning an empty result.
            return of(result as T);
        };
    }


    /** Log a message with the AlertService */
    private log(message: string) {
        this.error(message);
    }

    error(message: string, keepAfterRouteChange = false) {
        this.alert(AlertType.Error, message, keepAfterRouteChange);
    }

    alert(type: AlertType, message: string, keepAfterRouteChange = false) {
        this.keepAfterRouteChange = keepAfterRouteChange;
        this.clear();
        this.subject.next(<Alert>{type: type, message: message});
    }

    clear() {
        // clear alerts
        //todo clear subject
        //this.subject.next();
    }

}
