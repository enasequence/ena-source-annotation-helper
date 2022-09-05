import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
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


@Component({
    selector: 'app-construct',
    templateUrl: './construct.component.html',
    styleUrls: ['./construct.component.scss']
})

export class ConstructComponent implements OnInit {

    specimenVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Array<string>;
    typedInstitution: string = "";
    selectedInstitution: string = "";
    selectedCollection: string = "";
    searchInstitutionCtrl = new FormControl();
    filteredInstitutions: Observable<Institution[]> = null as any;
    filteredCollections: Observable<Collection[]> = null as any;
    minLengthTerm = 0;

    @ViewChild(ConstructstoreComponent, {static: false})
    showConstructStore: boolean = true;

    institutionHelp: string = "Mandatory Field. Select the institution by keying-in the name";

    constructor(private institutionService: InstitutionService,
                private constructValidateService: ConstructValidateService,
                private sahCommonsService: SahCommonsService,
                private _formBuilder: FormBuilder) {
        this.matchesResponse = new Array();
        this.localStorageObj = new Array();
        this.filteredInstitutions = new Observable<Institution[]>;

    }

    constructFormGroup = this._formBuilder.group({
        specimen_voucher: false,
        culture_collection: false,
        bio_material: false,
        specimen: new FormControl(this.specimenVal, [
            Validators.required,
            Validators.minLength(3)
        ]),
        inputCollection: new FormControl(this.selectedCollection)
    });

    ngOnInit() {
        this.filteredInstitutions = this.searchInstitutionCtrl.valueChanges.pipe(
            filter(data => data.trim().length > this.minLengthTerm && data.trim() !== this.selectedInstitution),
            // delay emits
            debounceTime(300),
            switchMap(value => {
                if (value !== '') {
                    return this.institutionService
                        .findByInstitutionValue(value, this.buildAttributeTypeArray());
                } else {
                    // if no value is present, return null
                    return of(null as any);
                }
            })
        );
    }

    getFilteredCollections(): Observable<Collection[]> {
        return this.institutionService
            .findByAllCollByInstituteUniqueName(this.selectedInstitution, this.buildAttributeTypeArray());

    }

    construct(): void {
        var inputVal: string = this.constructFormGroup.get("specimen")?.value!;
        console.log(inputVal);
        // call the validate request
        this.constructValidateService
            .constructAttribute(this.selectedInstitution, this.selectedCollection, inputVal, this.buildAttributeTypeArray())
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
            })
    };

    buildAttributeTypeArray(): Array<string> {
        var qualifierArray = new Array<string>();
        // prepare the request
        if (this.constructFormGroup.get("specimen_voucher")?.value == true) {
            qualifierArray.push("specimen_voucher");
        }
        if (this.constructFormGroup.get("culture_collection")?.value == true) {
            qualifierArray.push("culture_collection");
        }
        if (this.constructFormGroup.get("bio_material")?.value == true) {
            qualifierArray.push("bio_material");
        }
        return qualifierArray;
    }


    storeResultInLocal(matchString: string): void {
        localStorage.setItem(AppConstants.CONSTRUCT_STORAGE_PREFIX + matchString, matchString);
        this.fetchFromLocalStorage();
        this.refreshStoreComponent();
    }

    fetchFromLocalStorage(): void {
        //clear all before building
        while (this.localStorageObj.length) {
            this.localStorageObj.pop();
        }

        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key == null) {
                break;
            }
            console.log(key);
            if (key.startsWith(AppConstants.CONSTRUCT_STORAGE_PREFIX)) {
                var dd = localStorage.getItem(key);
                if (dd !== null) {
                    this.localStorageObj.push(dd);
                }
            }
        }
    }

    onSelected() {
        console.log(this.selectedInstitution);
        this.selectedInstitution = this.typedInstitution;
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
        this.selectedInstitution = "";
        this.filteredInstitutions = new Observable<Institution[]>;
    }

    refreshStoreComponent() {
        this.showConstructStore = false;
        setTimeout(() => {
            this.showConstructStore = true
        }, 50);
    }

}
