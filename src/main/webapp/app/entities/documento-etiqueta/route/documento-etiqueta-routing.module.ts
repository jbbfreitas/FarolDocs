import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentoEtiquetaComponent } from '../list/documento-etiqueta.component';
import { DocumentoEtiquetaDetailComponent } from '../detail/documento-etiqueta-detail.component';
import { DocumentoEtiquetaUpdateComponent } from '../update/documento-etiqueta-update.component';
import { DocumentoEtiquetaRoutingResolveService } from './documento-etiqueta-routing-resolve.service';

const documentoEtiquetaRoute: Routes = [
  {
    path: '',
    component: DocumentoEtiquetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentoEtiquetaDetailComponent,
    resolve: {
      documentoEtiqueta: DocumentoEtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentoEtiquetaUpdateComponent,
    resolve: {
      documentoEtiqueta: DocumentoEtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentoEtiquetaUpdateComponent,
    resolve: {
      documentoEtiqueta: DocumentoEtiquetaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(documentoEtiquetaRoute)],
  exports: [RouterModule],
})
export class DocumentoEtiquetaRoutingModule {}
