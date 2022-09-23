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

import {Component, EventEmitter, Injectable, OnInit, Output} from '@angular/core';
import {Clipboard} from "@angular/cdk/clipboard";
import {AppConstants} from "../../../app.constants";
import {MatchData} from "../../../models/MatchData";
import {Institution} from "../../../models/Institution";
import {QualifierTypeDisplay} from "../../../models/QualifierTypeData";
import {ConstructValidateService} from "../../../services/construct-validate.service";

@Injectable({
    providedIn: 'root'
})

@Component({
    selector: 'app-validatestore',
    templateUrl: './validatestore.component.html',
    styleUrls: ['./validatestore.component.scss']
})
export class ValidatestoreComponent implements OnInit {

    localStorageObj: Map<string, Institution>;
    readonly qualifierTypeDisplay = QualifierTypeDisplay;

    @Output("refreshStoreComponent") refreshStoreComponent: EventEmitter<any> = new EventEmitter(true);

    constructor(private constructValidateService: ConstructValidateService,
                private clipboard: Clipboard) {
        this.localStorageObj = new Map<string, Institution>();
        this.fetchFromLocalStorage();
    }

    ngOnInit(): void {
    }


    copyToClipboard(matchString: string): void {
        this.clipboard.copy(matchString);
    }

    copyAllToClipboard(): void {
        var savedAttributes = new Array<string>();
        Array.from(this.localStorageObj.keys()).forEach(key => savedAttributes.push(key));
        this.clipboard.copy(savedAttributes.join(AppConstants.NEW_LINE_SEPARATOR));
    }

    fetchFromLocalStorage(): void {
        //clear all before building
        this.localStorageObj.clear();
        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key == null) {
                break;
            }
            if (key.startsWith(AppConstants.VALIDATE_STORAGE_PREFIX)) {
                var dd: Institution = JSON.parse(localStorage.getItem(key) as any);
                if (dd !== null) {
                    this.localStorageObj.set(key.split(AppConstants.VALIDATE_STORAGE_PREFIX)[1], dd);
                }
            }
        }
    }

    //clear result
    clearSavedResult(matchString: string): void {
        //clear all before building
        localStorage.removeItem(AppConstants.VALIDATE_STORAGE_PREFIX + matchString);
        // refresh the component
        this.refreshStoreComponent.emit();
    }

    /**
     * clear all validate strings.
     */
    clearAllSavedResults(): void {
        Object.keys(localStorage).forEach(function(key){
            if (key !== null && key.startsWith(AppConstants.VALIDATE_STORAGE_PREFIX)) {
                localStorage.removeItem(key);
            }
        });

        // refresh the component
        this.refreshStoreComponent.emit();
    }

    getQualifierMeta(qualifierType: string) {
        return this.qualifierTypeDisplay.get(qualifierType);
    }

    getAttributeDisplayText(attribStr: string) {
        var inst = this.localStorageObj.get(attribStr);
        if(inst !== undefined) {
            return this.constructValidateService.getAttributeDisplayText(attribStr, inst);
        } else {
            return attribStr;
        }
    }

    getInstitutionMeta(matchString: string) {
        var inst = this.localStorageObj.get(matchString);
        if (inst !== undefined) {
            return this.constructValidateService.getInstitutionMeta(matchString, inst);
        } else {
            return "";
        }
    }

    getCollectionMeta(matchString: string) {
        var coll = this.localStorageObj.get(matchString)?.collections;
        var inst = this.localStorageObj.get(matchString);
        if (inst !== undefined && coll !== undefined) {
            return this.constructValidateService.getCollectionMeta(matchString, inst);
        } else {
            return "";
        }
    }
}
