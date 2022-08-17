import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ValidateComponent} from './components/validate/validate.component';
import {ConstructComponent} from './components/construct/construct.component';

const routes: Routes = [
    {path: 'validate', component: ValidateComponent},
    {path: 'construct', component: ConstructComponent},
];

@NgModule({
    imports: [RouterModule.forRoot(
        routes, {onSameUrlNavigation: 'reload'}
    )],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
