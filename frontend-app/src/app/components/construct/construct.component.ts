import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {MatchData} from "../../models/MatchData";
import {Clipboard} from '@angular/cdk/clipboard';
import {debounceTime, filter, switchMap} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {Institution} from "../../models/Institution";
import {InstitutionService} from "../../services/institution.service";
import {ConstructValidateService} from "../../services/construct-validate.service";
import {Observable, of} from "rxjs";
import {Collection} from "../../models/Collection";
import {SahCommonsService} from "../../services/sah-commons.service";


@Component({
    selector: 'app-construct',
    templateUrl: './construct.component.html',
    styleUrls: ['./construct.component.scss']
})

export class ConstructComponent implements OnInit {

    static STORAGE_PREFIX: string = "construct@";

    IsChecked: boolean;
    specimenVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Array<string>;
    typedInstitution: string = "";
    selectedInstitution: string = "";
    selectedCollection: string = "";
    searchInstitutionCtrl = new FormControl();
    filteredInstitutions: Observable<Institution[]> = null as any;
    filteredCollections: Observable<Collection[]> = null as any;
    isLoading = false;
    errorMsg!: string;
    minLengthTerm = 1;

    onSelected() {
        console.log(this.selectedInstitution);
        this.selectedInstitution = this.typedInstitution;
        this.filteredCollections = this.getfilteredCollections();
    }

    collectionChangeAction(selectedVal: string) {
        console.log(this.selectedCollection);
        this.selectedCollection = selectedVal;
    }

    displayWith(value: any) {
        return value?.Title;
    }

    clearSelection() {
        this.selectedInstitution = "";
        this.filteredInstitutions = new Observable<Institution[]>;
    }

    constructFormGroup = this._formBuilder.group({
        specimen_voucher: false,
        culture_collection: false,
        bio_material: false,
        specimen: new FormControl(this.specimenVal, [
            Validators.required,
            Validators.minLength(3)
        ])
    });

    constructor(private institutionService: InstitutionService,
                private constructValidateService: ConstructValidateService,
                private sahCommonsService: SahCommonsService,
                private _formBuilder: FormBuilder,
                private clipboard: Clipboard,
                private http: HttpClient) {
        this.IsChecked = false;
        this.matchesResponse = new Array();
        this.localStorageObj = new Array();
        this.fetchFromLocalStorage();
        this.filteredInstitutions = new Observable<Institution[]>;
    }

    ngOnInit() {
        this.filteredInstitutions = this.searchInstitutionCtrl.valueChanges.pipe(
            filter(data => data.trim().length > this.minLengthTerm && data.trim() !== this.selectedInstitution),
            // delay emits
            debounceTime(300),
            //     tap(() => {
            //         this.errorMsg = "";
            //         this.filteredInstitutions = new Observable<Institution[]>;
            //     }),
            // use switch map so as to cancel previous subscribed events, before creating new once
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

    getfilteredCollections(): Observable<Collection[]> {
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
        localStorage.setItem(ConstructComponent.STORAGE_PREFIX + matchString, matchString);
        this.fetchFromLocalStorage();
    }

    copyToClipboard(matchString: string): void {
        this.clipboard.copy(matchString);
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
            if (key.startsWith(ConstructComponent.STORAGE_PREFIX)) {
                var dd = localStorage.getItem(key);
                if (dd !== null) {
                    this.localStorageObj.push(dd);
                }
            }
        }
    }

    //clear result
    clearSavedResult(matchString: string): void {
        //clear all before building
        localStorage.removeItem(ConstructComponent.STORAGE_PREFIX + matchString);
        window.location.reload();
    }

    /**
     * clear all construct strings.
     */
    clearAllSavedResults(): void {
        var keysToRemove: string[] = [];
        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key !== null && key.startsWith(ConstructComponent.STORAGE_PREFIX)) {
                keysToRemove.push(key);
            }
        }
        while (keysToRemove.length > 0) {
            localStorage.removeItem(keysToRemove.pop()!);
        }

        //TODO - should not use this to reload the table
        window.location.reload();
    }

}
