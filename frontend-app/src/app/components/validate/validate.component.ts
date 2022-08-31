import {Component, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ConstructValidateService} from 'src/app/services/construct-validate.service';
import {MatchData} from "../../models/MatchData";
import {Router} from "@angular/router";
import {ValidatestoreComponent} from "./validatestore/validatestore.component";

@Component({
    selector: 'app-validate',
    templateUrl: './validate.component.html',
    styleUrls: ['./validate.component.scss']
})

export class ValidateComponent implements OnInit {

    static STORAGE_PREFIX: string = "validate@";

    IsChecked: boolean;
    attribVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Array<string>;

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
        this.localStorageObj = new Array();
        this.fetchFromLocalStorage();
    }


    ngOnInit(): void {

    }

    validate(): void {
        var inputVal: string = this.validateFormGroup.get("attribute")?.value!;
        console.log(inputVal);
        // call the validate request
        this.backendService.validateAttribute(inputVal, this.buildAttributeTypeArray())
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
            })
    };

    buildAttributeTypeArray(): Array<string> {
        var qualifierArray = new Array<string>();
        // prepare the request
        if (this.validateFormGroup.get("specimen_voucher")?.value == true) {
            qualifierArray.push("specimen_voucher");
        }
        if (this.validateFormGroup.get("culture_collection")?.value == true) {
            qualifierArray.push("culture_collection");
        }
        if (this.validateFormGroup.get("bio_material")?.value == true) {
            qualifierArray.push("bio_material");
        }
        return qualifierArray;
    }

    storeResultInLocal(matchString: string): void {
        //TODO discuss if this is necessary
        localStorage.setItem(ValidateComponent.STORAGE_PREFIX + matchString, matchString);
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
            if (key.startsWith(ValidateComponent.STORAGE_PREFIX)) {
                var dd = localStorage.getItem(key);
                if (dd !== null) {
                    this.localStorageObj.push(dd);
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


}
