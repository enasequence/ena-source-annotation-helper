import {Injectable, Injector} from '@angular/core';
import {environment} from '@env';
import {HttpClient, HttpErrorResponse, HttpHeaders} from '@angular/common/http';
import {SAHResponse} from "../models/SAHResponse";
import {Observable} from "rxjs";
import {map} from 'rxjs/operators';
import {AppConstants} from "../app.constants";
import {Institution} from "../models/Institution";
import {ErrorService} from "./error.service";
import {LoggingService} from "./logging.service";

@Injectable({
    providedIn: 'root'
})
export class ConstructValidateService {

    errorMessage: string = "";

    constructor(private http: HttpClient,
                private injector: Injector) {
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
        // alert(urlString);
        return this.http.get<SAHResponse>(urlString, options)
            .pipe(
                map(response => {
                    if (response.matches.length <= 0) {
                        console.log(AppConstants.NO_MATCHES_FOUND);
                        this.handleError(new Error(AppConstants.NO_MATCHES_FOUND));
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
                            this.handleError(new Error(AppConstants.NO_MATCHES_FOUND));
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

    getInstitutionMeta(matchString: string, institution: Institution) {
        let institutionPlaceHolder = "Institution: ";
        let institutionMeta = [
            institution.institutionName.trim(),
            institution.address.trim(),
            institution.country.trim()
        ].join(",");
        return institutionPlaceHolder + institutionMeta;
    }

    getCollectionMeta(matchString: string, institution: Institution) {
        let collectionPlaceHolder = "Collection: ";
        let collectionMeta = [
            institution.collections[0]?.collectionName,
            institution.collections[0]?.collectionType
        ].join(",");
        return collectionPlaceHolder + collectionMeta;
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
        // Always log errors
        logger.logError(this.errorMessage, "");
        console.error(error);
    }

}
