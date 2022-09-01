import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {SAHResponse} from "../models/SAHResponse";
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {AppConstants} from "../app.constants";

@Injectable({
    providedIn: 'root'
})
export class ConstructValidateService {

    constructor(private http: HttpClient) {
    }

    /**
     *
     * @param attribVal
     * @param qualifierArray
     */
    validateAttribute(attribVal: string, qualifierArray: string[]): Observable<SAHResponse> {
        // alert(attribVal);
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'validate?value=' + attribVal;
        if (qualifierArray.length > 0) {
            urlString = urlString + '&qualifier_type=' + qualifierArray;
        }
        //alert(urlString);
        return this.http.get<SAHResponse>(urlString, options)
            .pipe(
                map(response => {
                        if (response.matches.length <= 0) {
                            console.log(AppConstants.NO_MATCHES_FOUND);
                            throw new Error(AppConstants.NO_MATCHES_FOUND);
                        }
                        console.log(response.matches);
                        return response;

                    }
                ));
    };

    /**
     *
     * @param instVal
     * @param collVal
     * @param attribVal
     * @param qualifierArray
     */
    constructAttribute(instVal: string, collVal: string, attribVal: string, qualifierArray: string[]): Observable<SAHResponse> {
        // alert(attribVal);
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + '/construct?institution=' + instVal + '&id=' + attribVal;

        if (collVal.trim() != '') {
            urlString = urlString + '&collection=' + collVal;
        }

        if (qualifierArray.length > 0) {
            urlString = urlString + '&qualifier_type=' + qualifierArray;
        }

        // alert(urlString);
        return this.http.get<SAHResponse>(urlString, options)
            .pipe(
                map(response => {
                        if (response.matches.length <= 0) {
                            console.log(AppConstants.NO_MATCHES_FOUND);
                            throw new Error(AppConstants.NO_MATCHES_FOUND);
                        }
                        console.log(response.matches);
                        return response;

                    }
                ));
    };

}
