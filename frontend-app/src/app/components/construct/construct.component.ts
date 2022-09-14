import {Component, OnInit, ViewChild} from '@angular/core';
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
import {QualifierTypeDisplay, QualifierTypeData} from '../../models/QualifierTypeData';


@Component({
    selector: 'app-construct',
    templateUrl: './construct.component.html',
    styleUrls: ['./construct.component.scss']
})

export class ConstructComponent implements OnInit {

    readonly attributeTypeData = QualifierTypeData;
    readonly qualifierTypeDisplay = QualifierTypeDisplay;

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

    @ViewChild(ConstructstoreComponent, {static: false})
    showConstructStore: boolean = true;

    institutionHelp: string = "Mandatory Field. Select the institution by keying-in the name";

    constructor(private institutionService: InstitutionService,
                private constructValidateService: ConstructValidateService,
                private sahCommonsService: SahCommonsService,
                private _formBuilder: FormBuilder) {
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
            debounceTime(300),
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


    }

    getFilteredCollections(): Observable<Collection[]> {
        return this.institutionService
            .findByAllCollByInstituteUniqueName(this.selectedInstitution.uniqueName, this.attributeVal);

    }

    // Construct
    constructAttribute(): void {
        this.submitted = true;
        // Get all Form Controls keys and loop them
        Object.keys(this.constructFormGroup.controls).forEach(key => {
            // Get errors of every form control
            console.log("====>" + this.constructFormGroup.get(key)?.errors);
        });

        var inputVal: string = this.constructFormGroup.get("specimenCtrl")?.value!;
        console.log(inputVal);

        if (this.selectedInstitution !== null) {
            // call the validate request
            this.constructValidateService
                .constructAttribute(this.selectedInstitution.uniqueName, this.selectedCollection, inputVal, this.attributeVal)
                .subscribe(resp => {
                    this.matchesResponse = resp.matches;
                    this.matchesResponse.map(matchData => {
                        //alert(matchData.match);
                        this.matchesResponseMap.set(matchData.match, matchData);
                    })
                })
        } else {
            //raise errors TODO
            alert('qwert');
        }
    }

    getInstitutionMeta(matchString: string) {
        return this.matchesResponseMap.get(matchString)?.institution.institutionName + " , " +
            this.matchesResponseMap.get(matchString)?.institution.address + " , " +
            this.matchesResponseMap.get(matchString)?.institution.country;
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
            console.log(key);
            if (key.startsWith(AppConstants.CONSTRUCT_STORAGE_PREFIX)) {
                var dd: Institution = JSON.parse(localStorage.getItem(key) as any);
                if (dd !== null) {
                    this.localStorageObj.set(key.split(AppConstants.CONSTRUCT_STORAGE_PREFIX)[1], dd);
                }
            }
        }
    }

    onSelected() {
        console.log(this.typedInstitution);
        // @ts-ignore
        this.selectedInstitution = this.institutionsMap.get(this.typedInstitution);
        this.typedInstitution = this.selectedInstitution.uniqueName + "-" + this.selectedInstitution.institutionName;
        this.filteredCollections = this.getFilteredCollections();
    }

    collectionChangeAction(selectedVal: string) {
        //alert(selectedVal);
        console.log(selectedVal);
        this.selectedCollection = selectedVal;
    }

    displayWith(value: any) {
        return value?.Title;
    }

    clearSelection() {
        this.selectedInstitution = null as any;
        this.filteredInstitutions = new Observable<Institution[]>;
    }

    clearInstitutionSelection() {
        this.selectedInstitution = null as any;
        //this.filteredInstitutions = new Observable<Institution[]>;
        this.typedInstitution = "";
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
        //this.clearInstitutionSelection();
    }

}
