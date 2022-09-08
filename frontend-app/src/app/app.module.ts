import {ErrorHandler, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {ConstructComponent} from './components/construct/construct.component';
import {ValidateComponent} from './components/validate/validate.component';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatMenuModule} from "@angular/material/menu";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatTabsModule} from "@angular/material/tabs";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatAutocompleteModule} from "@angular/material/autocomplete";
import {MatButtonModule} from '@angular/material/button';
import {MatButtonToggleModule} from '@angular/material/button-toggle';
import {MatCardModule} from '@angular/material/card';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {MatTooltipModule} from "@angular/material/tooltip";
import {MatDialogModule} from "@angular/material/dialog";
import {MatSnackBarModule} from '@angular/material/snack-bar';
import {ConstructstoreComponent} from './components/construct/constructstore/constructstore.component';
import {ValidatestoreComponent} from './components/validate/validatestore/validatestore.component';
import {GlobalErrorHandler} from "./global-error-handler";
import {ServerErrorInterceptor} from "./server-error.interceptor";
import {TooltipDirective} from "./tooltip.directive";

@NgModule({
    declarations: [
        AppComponent,
        ConstructComponent,
        ValidateComponent,
        ConstructstoreComponent,
        ValidatestoreComponent,
        TooltipDirective
    ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpClientModule,
        MatCheckboxModule,
        MatDialogModule,
        MatGridListModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatTabsModule,
        MatFormFieldModule,
        MatInputModule,
        MatAutocompleteModule,
        MatSelectModule,
        MatButtonModule,
        MatButtonToggleModule,
        MatCardModule,
        MatTooltipModule,
        MatSnackBarModule
    ],
    exports: [
        MatCheckboxModule
    ],
    providers: [
        {provide: ErrorHandler, useClass: GlobalErrorHandler},
        {provide: HTTP_INTERCEPTORS, useClass: ServerErrorInterceptor, multi: true}
    ],
    bootstrap: [AppComponent]
})
export class AppModule {
}