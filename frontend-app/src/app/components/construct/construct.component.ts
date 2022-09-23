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
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {MatchData} from "../../models/MatchData";
import {debounceTime, filter, switchMap} from "rxjs/operators";
import {Institution} from "../../models/Institution";
import {InstitutionService} from "../../services/institution.service";
import {ConstructValidateService} from "../../services/construct-validate.service";
import {Observable, of} from "rxjs";
import {Collection} from "../../models/Collection";
import {SahCommonsService} from "../../services/sah-commons.service";
import {ConstructstoreComponent} from "./constructstore/constructstore.component";
import {AppConstants} from "../../app.constants";
import {MatSelectChange} from "@angular/material/select";
import {QualifierTypeData, QualifierTypeDisplay} from '../../models/QualifierTypeData';
import {Clipboard} from "@angular/cdk/clipboard";
import {NotificationService} from "../../services/notification.service";


@Component({
    selector: 'app-construct',
    templateUrl: './construct.component.html',
    styleUrls: ['./construct.component.scss']
})

export class ConstructComponent implements OnInit {

    readonly attributeTypeData = QualifierTypeData;
    readonly qualifierTypeDisplay = QualifierTypeDisplay;
    error$ = this.notificationService.errors$();

    attributeVal: string = "";
    specimenVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Map<string, Institution>;
    typedInstitution: string = "";
    selectedInstitution: Institution = null as any;
    selectedCollection: string = "";
    searchInstitutionCtrl = new FormControl();
    institutionsMap: Map<string, Institution> = new Map<string, Institution>();
    matchesResponseMap: Map<string, MatchData> = new Map<string, MatchData>();
    filteredInstitutions: Observable<Institution[]> = null as any;
    filteredCollections: Observable<Collection[]> = null as any;
    minLengthTerm = 0;
    submitted: boolean;
    errorMessage: string = "";

    @ViewChild(ConstructstoreComponent, {static: false})
    showConstructStore: boolean = true;

    institutionHelp: string = "Mandatory Field. Select the institution by keying-in the name";

    constructor(private institutionService: InstitutionService,
                private constructValidateService: ConstructValidateService,
                private sahCommonsService: SahCommonsService,
                private injector: Injector,
                private readonly notificationService: NotificationService,
                private _formBuilder: FormBuilder,
                private clipboard: Clipboard) {
        this.matchesResponse = new Array<MatchData>();
        this.localStorageObj = new Map<string, Institution>();
        this.filteredInstitutions = new Observable<Institution[]>;
        this.submitted = false;

    }

    constructFormGroup = new FormGroup({
        attributeCtrl: new FormControl(this.attributeVal, [
            Validators.required
        ]),
        specimenCtrl: new FormControl(this.specimenVal, [
            Validators.required,
            Validators.minLength(1)
        ]),
        inputCollectionCtrl: new FormControl(this.selectedCollection)
    });

    get attributeCtrl() {
        return this.constructFormGroup.get('attributeCtrl')
    }

    get specimenCtrl() {
        return this.constructFormGroup.get('specimenCtrl')
    }

    ngOnInit() {
        this.filteredInstitutions = this.searchInstitutionCtrl.valueChanges.pipe(
            filter(data => data.trim().length > this.minLengthTerm && data.trim() !== this.typedInstitution),
            // delay emits
            debounceTime(500),
            switchMap(value => {
                if (value !== '') {
                    return this.institutionService
                        .findByInstitutionValue(value, this.attributeVal);
                } else {
                    // if no value is present, return null
                    return of(null as any);
                }
            })
        )
        this.filteredInstitutions.subscribe(institutions => {
            institutions.map(instObj => {
                this.institutionsMap.set(instObj.uniqueName, instObj);
            })
        });
        this.errorMessage = this.institutionService.errorMessage;

    }

    getFilteredCollections(): Observable<Collection[]> {
        return this.institutionService
            .findByAllCollByInstituteUniqueName(this.selectedInstitution.uniqueName, this.attributeVal);

    }

    // Construct
    constructAttribute(): void {

        // on submit, reset the constructed attributes and the error messages
        this.submitted = true;
        this.clearMatchResponses();
        this.clearErrorMessages();

        //if inputs are not valid, return
        if (!this.constructFormGroup.valid) {
            return;
        }

        var inputVal: string = this.constructFormGroup.get("specimenCtrl")?.value!;
        // alert(inputVal);

        if (this.selectedInstitution == null || this.selectedInstitution.uniqueName == null) {
            return;
        }
        // call the construct request
        this.constructValidateService
            .constructAttribute(this.selectedInstitution.uniqueName, this.selectedCollection, inputVal, this.attributeVal)
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
                this.matchesResponse.map(matchData => {
                    //alert(matchData.match);
                    this.matchesResponseMap.set(matchData.match, matchData);
                })
            })

    }

    getInstitutionMeta(matchString: string) {
        var inst = this.matchesResponseMap.get(matchString)?.institution;
        if (inst !== undefined) {
            return this.constructValidateService.getInstitutionMeta(matchString, inst);
        } else {
            return "";
        }
    }

    getCollectionMeta(matchString: string) {
        var coll = this.matchesResponseMap.get(matchString)?.institution.collections;
        var inst = this.matchesResponseMap.get(matchString)?.institution;
        if (inst !== undefined && coll !== undefined) {
            return this.constructValidateService.getCollectionMeta(matchString, inst);
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

    getAttributeInstitution(matchString: string) {
        // pull up the institution as it will be always single value only in this case
        return this.matchesResponseMap.get(matchString)?.institution;
    }

    copyToClipboard(matchString: string): void {
        this.clipboard.copy(matchString);
    }

    storeResultInLocal(matchString: string): void {
        localStorage.setItem(AppConstants.CONSTRUCT_STORAGE_PREFIX + matchString, JSON.stringify(this.getAttributeInstitution(matchString)));
        this.fetchFromLocalStorage();
        this.refreshStoreComponent();
    }

    fetchFromLocalStorage(): void {
        //clear all before building
        this.localStorageObj.clear();

        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key == null) {
                break;
            }
            if (key.startsWith(AppConstants.CONSTRUCT_STORAGE_PREFIX)) {
                var dd: Institution = JSON.parse(localStorage.getItem(key) as any);
                if (dd !== null) {
                    this.localStorageObj.set(key.split(AppConstants.CONSTRUCT_STORAGE_PREFIX)[1], dd);
                }
            }
        }
    }

    onSelected() {
        this.selectedInstitution = this.institutionsMap.get(this.typedInstitution) == null ? null as any : this.institutionsMap.get(this.typedInstitution);
        if (this.selectedInstitution != null) {
            this.typedInstitution = this.selectedInstitution.uniqueName + "-" + this.selectedInstitution.institutionName;
            this.selectedCollection = "";
            this.filteredCollections = this.getFilteredCollections();
        }
    }

    collectionChangeAction(selectedVal: string) {
        // alert(selectedVal);
        this.selectedCollection = selectedVal;
    }

    displayWith(value: any) {
        return value?.Title;
    }


    refreshStoreComponent() {
        this.showConstructStore = false;
        setTimeout(() => {
            this.showConstructStore = true
        }, 50);
    }

    attributeSelection(event: MatSelectChange) {
        // alert(event.value);
        this.attributeVal = event.value;
        this.clearInstitutionSelection();
    }

    getAttributeDisplayText(attribStr: string) {
        var inst = this.matchesResponseMap.get(attribStr)?.institution;
        if (inst !== undefined) {
            return this.constructValidateService.getAttributeDisplayText(attribStr, inst);
        } else {
            return attribStr;
        }
    }

    clearInstitutionSelection() {
        this.selectedInstitution = null as any;
        //this.filteredInstitutions = new Observable<Institution[]>;
        this.typedInstitution = "";
        this.filteredInstitutions.forEach(val => val.pop());
        this.clearCollectionSelection();
        this.clearErrorMessages();
    }

    clearCollectionSelection() {
        this.selectedCollection = "";
        //this.filteredCollections.forEach(val => val.pop());
        this.clearSpecimenValSelection();
    }

    clearSpecimenValSelection() {
        this.specimenVal = "";
    }

    clearMatchResponses() {
        this.matchesResponseMap.clear();
        this.matchesResponse = new Array<MatchData>();
    }

    clearErrorMessages() {
        this.institutionService.errorMessage = "";
        this.constructValidateService.errorMessage = "";
        this.errorMessage = "";
    }

}
