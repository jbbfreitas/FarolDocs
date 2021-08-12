import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgaoEmissorComponent } from '../list/orgao-emissor.component';
import { OrgaoEmissorDetailComponent } from '../detail/orgao-emissor-detail.component';
import { OrgaoEmissorUpdateComponent } from '../update/orgao-emissor-update.component';
import { OrgaoEmissorRoutingResolveService } from './orgao-emissor-routing-resolve.service';

const orgaoEmissorRoute: Routes = [
  {
    path: '',
    component: OrgaoEmissorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgaoEmissorDetailComponent,
    resolve: {
      orgaoEmissor: OrgaoEmissorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgaoEmissorUpdateComponent,
    resolve: {
      orgaoEmissor: OrgaoEmissorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgaoEmissorUpdateComponent,
    resolve: {
      orgaoEmissor: OrgaoEmissorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orgaoEmissorRoute)],
  exports: [RouterModule],
})
export class OrgaoEmissorRoutingModule {}
