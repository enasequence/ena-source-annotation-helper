import {Component, EventEmitter, Injectable, OnInit, Output} from '@angular/core';
import {Clipboard} from "@angular/cdk/clipboard";
import {AppConstants} from "../../../app.constants";
import {MatchData} from "../../../models/MatchData";

@Injectable({
    providedIn: 'root'
})

@Component({
    selector: 'app-validatestore',
    templateUrl: './validatestore.component.html',
    styleUrls: ['./validatestore.component.scss']
})
export class ValidatestoreComponent implements OnInit {

    localStorageObj: Map<string, MatchData>;

    @Output("refreshStoreComponent") refreshStoreComponent: EventEmitter<any> = new EventEmitter(true);

    constructor(private clipboard: Clipboard) {
        this.localStorageObj = new Map<string, MatchData>();
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
            console.log(key);
            if (key.startsWith(AppConstants.VALIDATE_STORAGE_PREFIX)) {
                var dd: MatchData = localStorage.getItem(key) as any;
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
            console.log(localStorage.getItem(key));
            if (key !== null && key.startsWith(AppConstants.VALIDATE_STORAGE_PREFIX)) {
                localStorage.removeItem(key);
            }
        });
        
        // refresh the component
        this.refreshStoreComponent.emit();
    }
}
