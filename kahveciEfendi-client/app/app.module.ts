import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';
import { LoginComponent } from './components/login.component';
import { MenuComponent } from './components/menu.component';
import { FooterComponent } from './components/footer.component';

import { ShopComponent } from './subsites/shop.component';
import { ManagerComponent } from './subsites/manager.component';
import { ReportComponent } from './subsites/report.component';

import { ToolbarModule } from 'primeng/primeng';
import { InputTextModule } from 'primeng/primeng';
import { ButtonModule } from 'primeng/primeng';
import { DialogModule } from 'primeng/primeng';
import { PanelModule } from 'primeng/primeng';
import { DropdownModule } from 'primeng/primeng';
import { DataListModule } from 'primeng/primeng';
import { CheckboxModule } from 'primeng/primeng';
import { ChartModule } from 'primeng/primeng';

import { Ng2Webstorage } from 'ng2-webstorage';
import { RouterModule, Routes } from '@angular/router';

import { UserService } from './services/userservice';
import { DrinkManagerService } from './services/drinkmanagerservice';
import { ReportService } from './services/reportservice';

const appRoutes: Routes = [
  { path: 'shop', component: ShopComponent },
  { path: 'manager', component: ManagerComponent },
  { path: 'report', component: ReportComponent },
  {
    path: '',
    redirectTo: '/shop',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: '/shop',
    pathMatch: 'full'
  }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    MenuComponent,
    FooterComponent,
    ShopComponent,
    ManagerComponent,
    ReportComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,

    ToolbarModule,
    InputTextModule,
    ButtonModule,
    DialogModule,
    PanelModule,
    DropdownModule,
    DataListModule,
    CheckboxModule,
    ChartModule,

    Ng2Webstorage,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [UserService, DrinkManagerService, ReportService],
  bootstrap: [AppComponent]
})
export class AppModule { }
