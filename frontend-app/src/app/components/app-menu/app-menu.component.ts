import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MenuListItem} from "../../models/MenuListItem";

@Component({
    selector: 'app-menu',
    templateUrl: './app-menu.component.html',
    styleUrls: ['./app-menu.component.scss']
})
export class AppMenuComponent implements OnInit {
    menu: any;
    menuListItems: MenuListItem[];

    constructor(private router: Router) {

        this.menuListItems = [
            {
                menuLinkText: 'Construct',
                menuIcon: 'construct',
                isDisabled: false,
                routerLink: "/construct"
            },
            {
                menuLinkText: 'Validate',
                menuIcon: 'validate',
                isDisabled: false,
                routerLink: "/validate"
            }
        ];
    }

    ngOnInit(): void {
    }

    clickMenuItem(menuItem: MenuListItem) {
        this.router.navigate([menuItem.routerLink]);
    }

}
