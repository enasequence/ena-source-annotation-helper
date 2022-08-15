import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {MatchData} from "../../models/MatchData";
import {Clipboard} from '@angular/cdk/clipboard';
import {debounceTime, distinctUntilChanged, filter, finalize, switchMap, tap, catchError, map} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";
import {Institution} from "../../models/Institution";
import {InstitutionService} from "../../services/institution.service";
import {ConstructValidateService} from "../../services/construct-validate.service";
import {Observable, of, startWith} from "rxjs";
import {Collection} from "../../models/Collection";
import {SahCommonsService} from "../../services/sah-commons.service";


@Component({
    selector: 'app-construct',
    templateUrl: './construct.component.html',
    styleUrls: ['./construct.component.scss']
})

export class ConstructComponent implements OnInit {
    IsChecked: boolean;
    specimenVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Array<string>;
    selectedInstitution: any = "";
    selectedCollection: any = "";

    //
    searchInstitutionCtrl = new FormControl();
    //searchCollectionCtrl = new FormControl();
    filteredInstitutions: Observable<Institution[]> = null as any;
    //filteredCollections: Collection[];
    isLoading = false;
    errorMsg!: string;
    minLengthTerm = 1;


    onSelected() {
        console.log(this.selectedInstitution);
        this.selectedInstitution = this.selectedInstitution;
    }

    displayWith(value: any) {
        return value?.Title;
    }

    clearSelection() {
        this.selectedInstitution =
            new Institution("", "", "", "",
                "", "", [""], "",
                new Array(new Collection("", "", "", [""], "")));
        this.filteredInstitutions = new Observable<Institution[]>;
    }

    //

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
        //this.fetchFromLocalStorage();
        //this.filteredInstitutions = new Observable<Institution[]>;
        //this.filteredCollections = new Array<Collection>();
        // this.selectedInstitution =
        //     new Institution("", "", "", "",
        //         "", "", [""], "",
        //         new Array(new Collection("", "", "", [""], "")));
    }

    lookup(value: string): Observable<Institution[]> {
        return this.institutionService.findByInstitutionValue(value.toLowerCase(), [""]);
    }

    ngOnInit() {
        this.filteredInstitutions = this.searchInstitutionCtrl.valueChanges.pipe(
            filter(data => data.trim().length > this.minLengthTerm),
            // delay emits
            debounceTime(300),
            //     tap(() => {
            //         this.errorMsg = "";
            //         this.filteredInstitutions = new Observable<Institution[]>;
            //     }),
            // use switch map so as to cancel previous subscribed events, before creating new once
            switchMap(value => {
                if (value !== '') {
                    // lookup from github
                    return this.institutionService
                        .findByInstitutionValue(value, ["specimen_voucher"]);
                } else {
                    // if no value is present, return null
                    return of(null as any);
                }
            })
        );
    }


    // ngOnInit(): void {
    //     //this.filteredInstitutions = new Array<Institution>();
    //     this.searchInstitutionCtrl.valueChanges.pipe(
    //         startWith(''),
    //         // delay emits
    //         debounceTime(300),
    //         // use switch map so as to cancel previous subscribed events, before creating new once
    //         switchMap(value => {
    //             if (value !== '') {
    //                 // lookup from github
    //                 return this.institutionService
    //                     .findByInstitutionValue(value, ["specimen_voucher"]);
    //             } else {
    //                 // if no value is present, return null
    //                 return of(null as any);
    //             }
    //         })
    //     );

    // valueChanges.pipe(
    //     filter(data => data.trim().length > this.minLengthTerm),
    //     distinctUntilChanged(),
    //     debounceTime(500),
    //     tap(() => {
    //         this.errorMsg = "";
    //         this.filteredInstitutions = new Observable<Institution[]>;
    //     }),
    //     switchMap((id: string) => {
    //         return this.institutionService
    //             .findByInstitutionValue(id, ["specimen_voucher"]);
    //     })
    // )
    //     .subscribe(data => {
    //     this.filteredInstitutions = data;
    // });

    // }

    construct(): void {
        var inputVal: string = this.constructFormGroup.get("specimen")?.value!;
        var qualifierArray = new Array<string>();
        console.log(inputVal);
        //alert(inputVal);

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
        // call the validate request
        this.constructValidateService.validateAttribute(inputVal, qualifierArray)
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
            })

    };

    //
    // storeResultInLocal(matchString: string): void {
    //     //alert(matchString);
    //     //this.localStorageObj.push(matchString);
    //     //TODO discuss if this is necessary
    //     localStorage.setItem(matchString, matchString);
    //     this.fetchFromLocalStorage();
    // }
    //
    // copyToClipboard(matchString: string): void {
    //     //alert(matchString);
    //     this.clipboard.copy(matchString);
    // }
    //
    // //TODO need to see when to use this??
    // fetchFromLocalStorage(): void {
    //     //clear all before building
    //     while (this.localStorageObj.length) {
    //         this.localStorageObj.pop();
    //     }
    //
    //     for (var i = 0; i < localStorage.length; i++) {
    //         var key = localStorage.key(i);
    //         if (key == null) {
    //             break;
    //         }
    //         console.log(key);
    //         var dd = localStorage.getItem(key);
    //         if (dd !== null) {
    //             this.localStorageObj.push(dd);
    //         }
    //     }
    // }
    //
    // //clear result
    // clearSavedResult(matchString: string): void {
    //     //clear all before building
    //     localStorage.removeItem(matchString);
    //     window.location.reload();
    // }
    //
    // //clear all saved results
    // clearAllSavedResults(): void {
    //     //clear all before building
    //     localStorage.clear();
    //     window.location.reload();
    // }

}
