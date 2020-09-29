import {Inject, Injectable} from '@angular/core';
import {GenericService, BOOTSTRAP_SETTINGS, BootstrapSettings} from "@alien4cloud/wizard4cloud-commons";
import {HttpClient, HttpParams} from "@angular/common/http";
import {TranslateService} from "@ngx-translate/core";
import {Observable} from "rxjs";

@Injectable({
    providedIn: 'root'
})
export class SampleService extends GenericService {

    private readonly url: string;

    constructor(
        http: HttpClient,
        translate: TranslateService,
        @Inject(BOOTSTRAP_SETTINGS) protected bootstrapSettings: BootstrapSettings
    ) {
        super(http, translate, bootstrapSettings);
        this.url = this.baseUrl + "/sample/hello";
    }

    hello(name: string): Observable<String> {
        let params = new HttpParams().set("name", name);
        return this.handleResult<String>(this.http.get(this.url,{params: params}));
    }

}
