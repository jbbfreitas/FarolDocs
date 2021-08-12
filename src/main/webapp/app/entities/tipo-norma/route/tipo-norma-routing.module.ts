import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoNormaComponent } from '../list/tipo-norma.component';
import { TipoNormaDetailComponent } from '../detail/tipo-norma-detail.component';
import { TipoNormaUpdateComponent } from '../update/tipo-norma-update.component';
import { TipoNormaRoutingResolveService } from './tipo-norma-routing-resolve.service';

const tipoNormaRoute: Routes = [
  {
    path: '',
    component: TipoNormaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoNormaDetailComponent,
    resolve: {
      tipoNorma: TipoNormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoNormaUpdateComponent,
    resolve: {
      tipoNorma: TipoNormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoNormaUpdateComponent,
    resolve: {
      tipoNorma: TipoNormaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoNormaRoute)],
  exports: [RouterModule],
})
export class TipoNormaRoutingModule {}
