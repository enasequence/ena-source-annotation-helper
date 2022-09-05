import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {MetaResponse} from "../models/MetaResponse";
import {Institution} from "../models/Institution";
import {Collection} from "../models/Collection";
import {AppConstants} from "../app.constants";


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
                            console.log(AppConstants.NO_INSTITUTIONS_FOUND);
                            throw new Error(AppConstants.NO_INSTITUTIONS_FOUND);
                        }
                        // console.log(response.institutions);
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
                            console.log(AppConstants.NO_COLLECTIONS_FOUND);
                            throw new Error(AppConstants.NO_COLLECTIONS_FOUND);
                        }
                        return response.institutions[0] ? response.institutions[0].collections : [];
                    }
                ));
    }
}
