import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'sample', pathMatch: 'full' },
  // Lazy load LoginModule is necessary to avoid init exception.
  {
    path: 'login',
    loadChildren: () => import('./login-wrapper.module').then(mod => mod.LoginWrapperModule)
  },
  {
    path: 'sample',
    loadChildren: () => import('./features/sample/sample.module').then(mod => mod.SampleModule)
  }
];
@NgModule({
  imports: [
    RouterModule.forRoot(routes/*, { enableTracing: true } */)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
export const routingComponents = [ ];
