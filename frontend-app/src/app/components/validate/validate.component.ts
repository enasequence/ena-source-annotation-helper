import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {ConstructValidateService} from 'src/app/services/construct-validate.service';
import {MatchData} from "../../models/MatchData";
import {Clipboard} from '@angular/cdk/clipboard';

@Component({
    selector: 'app-validate',
    templateUrl: './validate.component.html',
    styleUrls: ['./validate.component.scss']
})

export class ValidateComponent implements OnInit {
    IsChecked: boolean;
    attribVal: string = "";
    matchesResponse: MatchData[];
    localStorageObj: Array<string>;

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
                private _formBuilder: FormBuilder,
                private clipboard: Clipboard) {
        this.IsChecked = false;
        this.matchesResponse = new Array();
        this.localStorageObj = new Array();
        this.fetchFromLocalStorage();
    }


    ngOnInit(): void {

    }

    validate(): void {
        var inputVal: string = this.validateFormGroup.get("attribute")?.value!;
        var qualifierArray = new Array<string>();
        console.log(inputVal);
        //alert(inputVal);

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
        // call the validate request
        this.backendService.validateAttribute(inputVal, qualifierArray)
            .subscribe(resp => {
                this.matchesResponse = resp.matches;
            })

    };

    storeResultInLocal(matchString: string): void {
        //alert(matchString);
        //this.localStorageObj.push(matchString);
        //TODO discuss if this is necessary
        localStorage.setItem(matchString, matchString);
        this.fetchFromLocalStorage();
    }

    copyToClipboard(matchString: string): void {
        //alert(matchString);
        this.clipboard.copy(matchString);
    }

    //TODO need to see when to use this??
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
            var dd = localStorage.getItem(key);
            if (dd !== null) {
                this.localStorageObj.push(dd);
            }
        }
    }

    //clear result
    clearSavedResult(matchString: string): void {
        //clear all before building
        localStorage.removeItem(matchString);
        window.location.reload();
    }

    //clear all saved results
    clearAllSavedResults(): void {
        //clear all before building
        localStorage.clear();
        window.location.reload();
    }

}
