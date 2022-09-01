import { Injectable } from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AppConstants {

    // cofiguration constants
    public static CONSTRUCT_STORAGE_PREFIX = "construct@";
    public static VALIDATE_STORAGE_PREFIX = "validate@";

    // Error Messages
    public static NO_INSTITUTIONS_FOUND = "No Institutions found for the selected criteria";
    public static NO_COLLECTIONS_FOUND = "No Collections found for the selected institution";
    public static NO_MATCHES_FOUND = "No matches found for the selected criteria";
}