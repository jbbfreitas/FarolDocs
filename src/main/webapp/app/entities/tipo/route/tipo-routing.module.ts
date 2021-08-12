import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoComponent } from '../list/tipo.component';
import { TipoDetailComponent } from '../detail/tipo-detail.component';
import { TipoUpdateComponent } from '../update/tipo-update.component';
import { TipoRoutingResolveService } from './tipo-routing-resolve.service';

const tipoRoute: Routes = [
  {
    path: '',
    component: TipoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoDetailComponent,
    resolve: {
      tipo: TipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoUpdateComponent,
    resolve: {
      tipo: TipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoUpdateComponent,
    resolve: {
      tipo: TipoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoRoute)],
  exports: [RouterModule],
})
export class TipoRoutingModule {}
