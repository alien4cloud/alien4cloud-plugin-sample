import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {HttpClient} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {FlexLayoutModule} from '@angular/flex-layout';
import {TranslateModule, TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ReactiveFormsModule} from '@angular/forms';
import {OverlayContainer} from '@angular/cdk/overlay';
import {AppRoutingModule, routingComponents} from './app-routing.module';
import {NgxWebstorageModule} from 'ngx-webstorage';
import {APP_BASE_HREF, HashLocationStrategy, LocationStrategy} from "@angular/common";
import {CookieService} from "ngx-cookie-service";
import {
  W4cMaterialModule,
  AuthInterceptor,
  W4cCommonsModule,
  AddonRedirectionProviderService, REDIRECTION_PROVIDER
} from "@alien4cloud/wizard4cloud-commons";

/**
 * Here we use the prefixed `merged-{lang}` i18n files that are the result of merging `addon-{lang}.json` and `{lang}.json` files.
 */
export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/merged-', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    routingComponents
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    // import HttpClientModule after BrowserModule.
    HttpClientModule,
    FlexLayoutModule,
    W4cMaterialModule,
    BrowserAnimationsModule,
    //  NoopAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    W4cCommonsModule,
    NgxWebstorageModule.forRoot({prefix: 'w4c', separator: '-'}),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    })
  ],
  exports: [
    W4cMaterialModule,
    W4cCommonsModule
  ],
  providers: [
    OverlayContainer,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: APP_BASE_HREF,
      useValue: window['base-href']
    },
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    },
    CookieService,
    {
      provide: REDIRECTION_PROVIDER,
      useClass: AddonRedirectionProviderService
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(overlayContainer: OverlayContainer) {
  }
}
