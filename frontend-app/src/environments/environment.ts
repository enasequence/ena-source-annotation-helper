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

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
    name: 'dev',
    production: false,
    sahAPIURL: 'https://www.ebi.ac.uk/ena/sah/api/',
    contactSupportURL: "https://www.ebi.ac.uk/ena/browser/support",
    ncbiURL: 'https://www.ncbi.nlm.nih.gov/biocollections',
    insdcFTURL: 'https://www.insdc.org/submitting-standards/feature-table/',
    ebiFooterHTML: "https://www.embl.org/api/v1/pattern.html?filter-content-type=article&filter-id=106902&pattern=node-body&source=contenthub"
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
