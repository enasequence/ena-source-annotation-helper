import {Component, EventEmitter, Injectable, OnInit, Output} from '@angular/core';
import {Clipboard} from '@angular/cdk/clipboard';
import {ConstructComponent} from "../construct.component";
import {AppConstants} from "../../../app.constants";

@Injectable({
    providedIn: 'root'
})

@Component({
    selector: 'app-constructstore',
    templateUrl: './constructstore.component.html',
    styleUrls: ['./constructstore.component.scss']
})
export class ConstructstoreComponent implements OnInit {

    localStorageObj: Array<string>;

    @Output("refreshStoreComponent") refreshStoreComponent: EventEmitter<any> = new EventEmitter();

    constructor(private clipboard: Clipboard) {
        this.localStorageObj = new Array();
        this.fetchFromLocalStorage();
    }

    ngOnInit(): void {
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
            if (key.startsWith(AppConstants.CONSTRUCT_STORAGE_PREFIX)) {
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
        localStorage.removeItem(AppConstants.CONSTRUCT_STORAGE_PREFIX + matchString);
        // refresh the component
        this.refreshStoreComponent.emit();
    }

    /**
     * clear all construct strings.
     */
    clearAllSavedResults(): void {
        var keysToRemove: string[] = [];
        for (var i = 0; i < localStorage.length; i++) {
            var key = localStorage.key(i);
            if (key !== null && key.startsWith(AppConstants.CONSTRUCT_STORAGE_PREFIX)) {
                keysToRemove.push(key);
            }
        }
        while (keysToRemove.length > 0) {
            localStorage.removeItem(keysToRemove.pop()!);
        }
        // refresh the component
        this.refreshStoreComponent.emit();
    }

}