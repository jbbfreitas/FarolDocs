import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocumentoComponent } from '../list/documento.component';
import { DocumentoDetailComponent } from '../detail/documento-detail.component';
import { DocumentoUpdateComponent } from '../update/documento-update.component';
import { DocumentoRoutingResolveService } from './documento-routing-resolve.service';
import { DocumentoEtiquetaComponent } from 'app/entities/documento-etiqueta/list/documento-etiqueta.component';
import { EtiquetaComponent } from 'app/entities/etiqueta/list/etiqueta.component';
import { EtiquetaUpdateComponent } from 'app/entities/etiqueta/update/etiqueta-update.component';
import { EtiquetaRoutingResolveService } from 'app/entities/etiqueta/route/etiqueta-routing-resolve.service';

const documentoRoute: Routes = [
  {
    path: '',
    component: DocumentoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DocumentoDetailComponent,
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DocumentoUpdateComponent,
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DocumentoUpdateComponent,
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  /* Tirar
  {
    path: ':id/etiqueta',
    component: DocumentoEtiquetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  */
  {
    path: ':id/etiquetas',
    component: EtiquetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },

];

@NgModule({
  imports: [RouterModule.forChild(documentoRoute)],
  exports: [RouterModule],
})
export class DocumentoRoutingModule {}
