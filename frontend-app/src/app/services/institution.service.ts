import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {MetaResponse} from "../models/MetaResponse";
import {Institution} from "../models/Institution";


@Injectable({
    providedIn: 'root'
})
export class InstitutionService {

    constructor(private http: HttpClient) {
    }

    findByInstitutionValue(instVal: string, qualifierArray: string[]): Observable<Institution[]> {
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'institution/=' + instVal;
        if (qualifierArray.length > 0) {
            urlString = urlString + '&qualifier_type=' + qualifierArray;
        }
        alert(urlString);

        // return this.http.get<SAHResponse>(urlString, options);
        return this.http.get<MetaResponse>(urlString, options)
            .pipe(
                map(response => {
                        console.log(response.institutions);
                        return response.institutions;
                    }
                ));
    };
}
