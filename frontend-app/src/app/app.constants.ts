/**
 * Copyright (C) 2006-2021 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class AppConstants {

    // cofiguration constants
    public static CONSTRUCT_STORAGE_PREFIX = "construct@";
    public static VALIDATE_STORAGE_PREFIX = "validate@";
    public static NEW_LINE_SEPARATOR = "\r\n";

    // Error Messages
    public static NO_INSTITUTIONS_FOUND = "No Institutions found for the selected criteria";
    public static NO_COLLECTIONS_FOUND = "No Collections found for the selected institution";
    public static NO_MATCHES_FOUND = "No matches found for the selected criteria";
}