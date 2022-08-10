import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCheckboxModule } from '@angular/material/checkbox';
import {HttpClient, HttpClientModule} from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ConstructComponent } from './components/construct/construct.component';
import { ValidateComponent } from './components/validate/validate.component';
import {MatGridListModule} from "@angular/material/grid-list";
import { AppMenuComponent } from './components/app-menu/app-menu.component';
import {MatMenuModule} from "@angular/material/menu";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatTabsModule} from "@angular/material/tabs";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatAutocompleteModule} from "@angular/material/autocomplete";

@NgModule({
  declarations: [
    AppComponent,
    ConstructComponent,
    ValidateComponent,
    AppMenuComponent
  ],
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule,
        HttpClientModule,
        MatCheckboxModule,
        MatGridListModule,
        MatMenuModule,
        MatToolbarModule,
        MatIconModule,
        MatTabsModule,
        MatFormFieldModule,
        MatAutocompleteModule
    ],
  exports:[
    MatCheckboxModule
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
