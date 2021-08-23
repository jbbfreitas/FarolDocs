import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentoEtiquetaComponent } from './list/documento-etiqueta.component';
import { DocumentoEtiquetaDetailComponent } from './detail/documento-etiqueta-detail.component';
import { DocumentoEtiquetaUpdateComponent } from './update/documento-etiqueta-update.component';
import { DocumentoEtiquetaDeleteDialogComponent } from './delete/documento-etiqueta-delete-dialog.component';
import { DocumentoEtiquetaRoutingModule } from './route/documento-etiqueta-routing.module';

@NgModule({
  imports: [SharedModule, DocumentoEtiquetaRoutingModule],
  declarations: [
    DocumentoEtiquetaComponent,
    DocumentoEtiquetaDetailComponent,
    DocumentoEtiquetaUpdateComponent,
    DocumentoEtiquetaDeleteDialogComponent,
  ],
  entryComponents: [DocumentoEtiquetaDeleteDialogComponent],
})
export class DocumentoEtiquetaModule {}
