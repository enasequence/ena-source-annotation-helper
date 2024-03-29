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

import {Component, Injector, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ConstructValidateService} from 'src/app/services/construct-validate.service';
import {MatchData} from "../../models/MatchData";
import {ValidatestoreComponent} from "./validatestore/validatestore.component";
import {AppConstants} from "../../app.constants";
import {MatSelectChange} from "@angular/material/select";
import {QualifierTypeData, QualifierTypeDisplay} from "../../models/QualifierTypeData";
import {Institution} from "../../models/Institution";
import {HttpErrorResponse} from "@angular/common/http";
import {ErrorService} from "../../services/error.service";
import {LoggingService} from "../../services/logging.service";
import {NotificationService} from "../../services/notification.service";
import {Clipboard} from "@angular/cdk/clipboard";


@Component({
    selector: 'app-validate',
    templateUrl: './validate.component.html',
    styleUrls: ['./validate.component.scss']
})

export class ValidateComponent implements OnInit {

    readonly attributeTypeData = QualifierTypeData;
    readonly qualifierTypeDisplay = QualifierTypeDisplay;
    error$ = this.notificationService.errors$();

    attributeTypeVal: string = "";
    attributeVal: string = "";
    IsChecked: boolean;
    matchesResponse: MatchData[];
    localStorageObj: Map<string, Institution>;
    matchesResponseMap: Map<string, MatchData> = new Map<string, MatchData>();
    submitted: boolean;
    errorMessage: string = "";

    @ViewChild(ValidatestoreComponent, {static: false})
    showValidateStore: boolean = true;

    validateFormGroup = this._formBuilder.group({
        specimen_voucher: false,
        culture_collection: false,
        bio_material: false,
        attributeTypeCtrl: new FormControl(this.attributeTypeVal, [
            Validators.required
        ]),
        attributeCtrl: new FormControl(this.attributeVal, [
            Validators.required
        ])
    });

    get attributeTypeCtrl() {
        return this.validateFormGroup.get('attributeTypeCtrl')
    }

    get attributeCtrl() {
        return this.validateFormGroup.get('attributeCtrl')
    }

    constructor(private backendService: ConstructValidateService,
                private _formBuilder: FormBuilder,
                private injector: Injector,
                private readonly notificationService: NotificationService,
                private clipboard: Clipboard) {
        this.IsChecked = false;
        this.matchesResponse = new Array<MatchData>();
        this.localStorageObj = new Map<string, Institution>();
        //this.fetchFromLocalStorage();
        this.submitted = false;
    }

    ngOnInit(): void {

    }

    validateAttribute(): void {
        this.submitted = true;
        this.error$ = null as any;
        //if inputs are not valid, return
        if (!this.validateFormGroup.valid) {
            return;
        }
        var inputVal: string = this.validateFormGroup.get("attributeCtrl")?.value!;
        // call the validate request
        this.backendService.validateAttribute(inputVal, this.attributeTypeVal)
            .subscribe(resp => {
                    this.matchesResponse = resp.matches;
                    this.errorMessage = this.backendService.errorMessage;
                    this.matchesResponse.map(matchData => {
                        //alert(matchData.match);
                        this.matchesResponseMap.set(matchData.match, matchData);
                    })
                }, error => {
                    this.errorMessage = error;
                }
            )
    };

    storeResultInLocal(matchString: string): void {
        localStorage.setItem(AppConstants.VALIDATE_STORAGE_PREFIX + matchString, JSON.stringify(this.getAttributeInstitution(matchString)));
        this.fetchFromLocalStorage();
        this.refreshStoreComponent();
    }

    getAttributeInstitution(matchString: string) {
        // pull up the institution as it will be always single value only in this case
        return this.matchesResponseMap.get(matchString)?.institution;
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

    refreshStoreComponent() {
        this.showValidateStore = false;
        setTimeout(() => {
            this.showValidateStore = true
        }, 50);
    }

    attributeSelection(event: MatSelectChange) {
        // alert(event.value);
        this.attributeTypeVal = event.value;
    }

    getInstitutionMeta(matchString: string) {
        var inst = this.matchesResponseMap.get(matchString)?.institution;
        if (inst !== undefined) {
            return this.backendService.getInstitutionMeta(matchString, inst);
        } else {
            return "";
        }
    }

    getCollectionMeta(matchString: string) {
        var coll = this.matchesResponseMap.get(matchString)?.institution.collections;
        var inst = this.matchesResponseMap.get(matchString)?.institution;
        if (inst !== undefined && coll !== undefined) {
            return this.backendService.getCollectionMeta(matchString, inst);
        } else {
            return "";
        }
    }

    getAttributeMeta(matchString: string) {
        // pull up the first qualifier value as it will be always single value only in this case
        return this.qualifierTypeDisplay.get(
            this.matchesResponseMap.get(matchString)?.institution.qualifierType[0]
        );
    }

    copyToClipboard(matchString: string): void {
        this.clipboard.copy(matchString);
    }

    getAttributeDisplayText(attribStr: string) {
        var inst = this.matchesResponseMap.get(attribStr)?.institution;
        if (inst !== undefined) {
            return this.backendService.getAttributeDisplayText(attribStr, inst);
        } else {
            return attribStr;
        }
    }

    clearErrorMessages() {
        this.backendService.errorMessage = "";
        this.errorMessage = "";
    }

}
