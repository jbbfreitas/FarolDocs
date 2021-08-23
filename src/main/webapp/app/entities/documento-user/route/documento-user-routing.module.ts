import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentoUserComponent } from '../list/documento-user.component';
import { DocumentoUserDetailComponent } from '../detail/documento-user-detail.component';
import { DocumentoUserUpdateComponent } from '../update/documento-user-update.component';
import { DocumentoUserRoutingResolveService } from './documento-user-routing-resolve.service';

const documentoUserRoute: Routes = [
  {
    path: '',
    component: DocumentoUserComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentoUserDetailComponent,
    resolve: {
      documentoUser: DocumentoUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentoUserUpdateComponent,
    resolve: {
      documentoUser: DocumentoUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentoUserUpdateComponent,
    resolve: {
      documentoUser: DocumentoUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentoUserRoute)],
  exports: [RouterModule],
})
export class DocumentoUserRoutingModule {}
