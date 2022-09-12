import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ConstructValidateService} from 'src/app/services/construct-validate.service';
import {MatchData} from "../../models/MatchData";
import {ValidatestoreComponent} from "./validatestore/validatestore.component";
import {AppConstants} from "../../app.constants";
import {MatSelectChange} from "@angular/material/select";
import {AttributeTypeData} from "../../models/AttributeTypeData";

@Component({
    selector: 'app-validate',
    templateUrl: './validate.component.html',
    styleUrls: ['./validate.component.scss']
})

export class ValidateComponent implements OnInit {

    readonly attributeTypeData = AttributeTypeData;

    attributeVal: string = "";
    IsChecked: boolean;
    attribVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Map<string, MatchData>;

    @ViewChild(ValidatestoreComponent, {static: false})
    showValidateStore: boolean = true;

    validateFormGroup = this._formBuilder.group({
        specimen_voucher: false,
        culture_collection: false,
        bio_material: false,
        attribute: new FormControl(this.attribVal, [
            Validators.required,
            Validators.minLength(3)
        ])
    });

    constructor(private backendService: ConstructValidateService,
                private _formBuilder: FormBuilder) {
        this.IsChecked = false;
        this.matchesResponse = new Array();
        this.localStorageObj = new Map<string, MatchData>();
        this.fetchFromLocalStorage();
    }


    ngOnInit(): void {

    }

    validate(): void {
        var inputVal: string = this.validateFormGroup.get("attribute")?.value!;
        console.log(inputVal);
        // call the validate request
        this.backendService.validateAttribute(inputVal, this.attributeVal)
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
            })
    };

    storeResultInLocal(matchString: string): void {
        //TODO discuss if this is necessary
        localStorage.setItem(AppConstants.VALIDATE_STORAGE_PREFIX + matchString, matchString);
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
            if (key.startsWith(AppConstants.VALIDATE_STORAGE_PREFIX)) {
                var dd: MatchData = localStorage.getItem(key) as any;
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
        this.attributeVal = event.value;
    }


}
