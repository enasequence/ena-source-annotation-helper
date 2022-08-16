import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {MetaResponse} from "../models/MetaResponse";
import {Institution} from "../models/Institution";
import {Collection} from "../models/Collection";


@Injectable({
    providedIn: 'root'
})
export class InstitutionService {

    constructor(private http: HttpClient) {
    }

    /**
     *
     * @param instVal
     * @param qualifierArray
     */
    findByInstitutionValue(instVal: string, qualifierArray: string[]): Observable<Institution[]> {
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'institution/' + instVal;
        if (qualifierArray.length > 0) {
            urlString = urlString + '&qualifier_type=' + qualifierArray;
        }
        //alert(urlString);
        return this.http.get<MetaResponse>(urlString, options)
            .pipe(
                map(response => {
                        console.log(response.institutions);
                        return response.institutions;
                    }
                ));
    }

    /**
     *
     * @param instVal
     * @param qualifierArray
     */
    findByAllCollByInstituteUniqueName(instVal: string, qualifierArray: string[]): Observable<Collection[]> {
        var filterdCollections: Collection[] = [] as any;
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'institution/' + instVal + '/collection';
        if (qualifierArray.length > 0) {
            urlString = urlString + '?qualifier_type=' + qualifierArray;
        }
        //alert(urlString);
        return this.http.get<MetaResponse>(urlString, options)
            .pipe(
                map(response => {
                        //console.log(response.institutions);
                        return response.institutions[0] ? response.institutions[0].collections : [];
                    }
                ));
    }
}
