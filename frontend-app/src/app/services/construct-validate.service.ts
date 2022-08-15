import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders, HttpParams, HttpResponse} from '@angular/common/http';
import {SAHResponse} from "../models/SAHResponse";
import {Observable} from "rxjs";
import {map, retry, catchError, shareReplay, finalize, take} from 'rxjs/operators';

@Injectable({
    providedIn: 'root'
})
export class ConstructValidateService {

    constructor(private http: HttpClient) {
    }

    validateAttribute(attribVal: string, qualifierArray: string[]): Observable<SAHResponse> {
        // alert(attribVal);
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'validate?value=' + attribVal;
        if (qualifierArray.length > 0) {
            urlString = urlString + '&qualifier_type=' + qualifierArray;
        }
        // alert(urlString);

        // return this.http.get<SAHResponse>(urlString, options);

        return this.http.get<SAHResponse>(urlString, options)
            .pipe(
                map(response => {
                        console.log(response.matches);
                        return response;

                    }
                ));
    };

}
