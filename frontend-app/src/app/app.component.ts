import {Component} from '@angular/core';
import {MenuListItem} from "./models/MenuListItem";
import {Router} from "@angular/router";
import {environment} from "@env";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {
    title = 'ena-sah-frontend';
    public selectedTab = 0;
    sahAPIURL = environment.sahAPIURL;

    public tab = Object.seal({
        CONSTRUCT: 0,
        VALIDATE: 1
    });

    tabIndex = 0;

    featureEnabled = true;

    public changeTab() {
        //alert(this.selectedTab);
    }

    constructor(private router: Router) {

    }

    ngOnInit(): void {

    }
}
