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

import {Collection} from "./Collection";

export class Institution {
    institutionCode: string;
    uniqueName: string;
    institutionName: string;
    country: string;
    address: string;
    collectionType: string;
    qualifierType: string[];
    homeUrl: string;
    collections: Collection[];


    constructor(institutionCode: string, uniqueName: string, institutionName: string,
                country: string, address: string, collectionType: string,
                qualifierType: string[], homeUrl: string, collections: Collection[]) {
        this.institutionCode = institutionCode;
        this.uniqueName = uniqueName;
        this.institutionName = institutionName;
        this.country = country;
        this.address = address;
        this.collectionType = collectionType;
        this.qualifierType = qualifierType;
        this.homeUrl = homeUrl;
        this.collections = collections;
    }
}
