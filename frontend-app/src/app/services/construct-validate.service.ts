import {Injectable} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {SAHResponse} from "../models/SAHResponse";
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {AppConstants} from "../app.constants";
import {Institution} from "../models/Institution";

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
    validateAttribute(attribVal: string, qualifier: string): Observable<SAHResponse> {
        // alert(attribVal);
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + 'validate?value=' + attribVal;
        if (qualifier !== "") {
            urlString = urlString + '&qualifier_type=' + qualifier;
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

                })
            );
    };

    /**
     *
     * @param instVal
     * @param collVal
     * @param attribVal
     * @param qualifierArray
     */
    constructAttribute(instVal: string, collVal: string, attribVal: string, qualifier: string): Observable<SAHResponse> {
        // alert(attribVal);
        const headers = new HttpHeaders({'Accept': 'application/json'});
        const options = {headers: headers};
        var urlString = environment.sahAPIURL + '/construct?institution=' + instVal + '&id=' + attribVal;

        if (collVal.trim() != '') {
            urlString = urlString + '&collection=' + collVal;
        }

        if (qualifier !== "") {
            urlString = urlString + '&qualifier_type=' + qualifier;
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
    }

    getAttributeDisplayText(attribStr: string, inst: Institution) {

        var attribArrWithLinks = "";
        var splittedAttribArr = attribStr.split(":", 3);

        //process institution
        if (inst?.homeUrl.trim() !== "") {
            console.log("institution url exists for " + inst?.uniqueName + " : " + inst?.homeUrl.trim());
            attribArrWithLinks = "<a target='_blank' href='" + inst?.homeUrl.trim() + "' class='white-color'>" +
                splittedAttribArr[0]
                    .replace(/</g, "&lt;")
                    .replace(/>/g, "&gt;")
                + "</a>";
        } else {
            attribArrWithLinks = splittedAttribArr[0]
                .replace(/</g, "&lt;")
                .replace(/>/g, "&gt;");
        }

        //process collection if available and simply add identifier/code
        if (splittedAttribArr.length === 3) {
            console.log(3);
            if (inst?.collections[0].collectionUrl.trim() !== "") {
                console.log("collection url exists for " + inst?.collections[0].collectionCode + " : " + inst?.collections[0].collectionUrl.trim());
                attribArrWithLinks = attribArrWithLinks +
                    ":<a target='_blank' href='" + inst?.collections[0].collectionUrl.trim() + "' class='white-color'>" + splittedAttribArr[1] + "</a>" +
                    ":" + splittedAttribArr[2];
            } else {
                attribArrWithLinks = attribArrWithLinks + ":" + splittedAttribArr[1] +
                    ":" + splittedAttribArr[2];
            }
        } else {
            attribArrWithLinks = attribArrWithLinks + ":" + splittedAttribArr[1];
        }

        return attribArrWithLinks;
    }

}
