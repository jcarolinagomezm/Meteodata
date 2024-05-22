import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule, HTTP_INTERCEPTORS} from '@angular/common/http';

import {AppRoutingModule} from '@/app-routing.module';
import {AppComponent} from './app.component';
import {MainComponent} from '@modules/main/main.component';
import {LoginComponent} from '@modules/login/login.component';
import {HeaderComponent} from '@modules/main/header/header.component';
import {FooterComponent} from '@modules/main/footer/footer.component';
import {MenuSidebarComponent} from '@modules/main/menu-sidebar/menu-sidebar.component';
import {AlertComponent} from '@pages/alerts/alert.component';
import {ReactiveFormsModule} from '@angular/forms';
import {UserAdminComponent} from '@pages/user/userAdmin.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {DashboardComponent} from '@pages/dashboard/dashboard.component';
import {ToastrModule} from 'ngx-toastr';

import {CommonModule, registerLocaleData} from '@angular/common';
import localeEn from '@angular/common/locales/en';
import {UserComponent} from '@modules/main/header/user/user.component';
import {MenuItemComponent} from './components/menu-item/menu-item.component';
import {ControlSidebarComponent} from './modules/main/control-sidebar/control-sidebar.component';
import {StoreModule} from '@ngrx/store';
import {authReducer} from './store/auth/reducer';
import {uiReducer} from './store/ui/reducer';
import {ProfabricComponentsModule} from '@profabric/angular-components';
import {SidebarSearchComponent} from './components/sidebar-search/sidebar-search.component';
import { environment } from 'environments/environment';
import { Interceptor } from './interceptor/interceptor';
import { NgxPaginationModule } from 'ngx-pagination';
import { ModalComponent } from '@components/modal/modal.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { ModalUserComponent } from '@components/modalUser/modalUser.component';
import { WeatherComponent } from '@pages/weather/weather.component';
import { ReportesComponent } from '@pages/reportes/reportes.component';
import { CargarDatosComponent } from '@pages/cargarDatos/cargarDatos.component';
import {DatePipe} from '@angular/common';
import { DataReportComponent } from '@pages/data-report/data-report.component';
import { AlertsReportComponent } from '@pages/alerts-report/alerts-report.component';

registerLocaleData(localeEn, 'es-ES');

@NgModule({
    declarations: [
        AppComponent,
        MainComponent,
        LoginComponent,
        HeaderComponent,
        FooterComponent,
        MenuSidebarComponent,
        AlertComponent,
        UserAdminComponent,
        DashboardComponent,
        UserComponent,
        MenuItemComponent,
        ControlSidebarComponent,
        SidebarSearchComponent,
        ModalComponent,
        ModalUserComponent,
        WeatherComponent,
        ReportesComponent,
        DataReportComponent,
        AlertsReportComponent,
        CargarDatosComponent,
    ],
    imports: [
        ProfabricComponentsModule,
        CommonModule,
        BrowserModule,
        StoreModule.forRoot({auth: authReducer, ui: uiReducer}),
        HttpClientModule,
        AppRoutingModule,
        ReactiveFormsModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot({
            timeOut: 3000,
            positionClass: 'toast-top-right',
            preventDuplicates: true
        }),
        NgxPaginationModule,
    ],
    providers: [
        {
            provide: HTTP_INTERCEPTORS,
            useClass: Interceptor,
            multi: true,
        },
        provideAnimationsAsync('noop'),
        DatePipe
    ],
    bootstrap: [AppComponent]
})
export class AppModule {}
