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

import {Component, Injector} from '@angular/core';
import {MenuListItem} from "./models/MenuListItem";
import {Router} from "@angular/router";
import {environment} from "@env";
import {HttpClient} from "@angular/common/http";
import {SAHResponse} from "./models/SAHResponse";
import {map} from "rxjs/operators";
import {AppConstants} from "./app.constants";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    title = 'ena-sah-frontend';
    public selectedTab = 0;
    sahAPIURL = environment.sahAPIURL;
    ncbiURL = environment.ncbiURL;
    insdcFTURL = environment.insdcFTURL;
    contactSupportURL = environment.contactSupportURL;
    externalHtml = "";

    public tab = Object.seal({
        CONSTRUCT: 0,
        VALIDATE: 1
    });

    tabIndex = 0;

    featureEnabled = true;

    public changeTab() {
        //alert(this.selectedTab);
    }

    constructor(private http: HttpClient,
                private router: Router) {
    }

    ngOnInit(): void {
        this.http.get(environment.ebiFooterHTML, {responseType: 'text'})
            .subscribe(data => this.externalHtml=data);
    }
}
