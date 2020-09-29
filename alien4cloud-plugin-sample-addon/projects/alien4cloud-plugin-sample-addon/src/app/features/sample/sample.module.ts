import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {W4cMaterialModule, W4cCommonsModule} from "@alien4cloud/wizard4cloud-commons";
import {HelloComponent} from "@app/features/sample/hello/hello.component";
import {SampleRoutingModule} from "@app/features/sample/sample-routing.module";

@NgModule({
  declarations: [
    HelloComponent,
  ],
  imports: [
    CommonModule,
    SampleRoutingModule,
    W4cCommonsModule,
    W4cMaterialModule
  ]
})
export class SampleModule { }
