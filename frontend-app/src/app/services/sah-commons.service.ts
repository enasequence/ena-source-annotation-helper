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
        console.log('message:' + message);
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
