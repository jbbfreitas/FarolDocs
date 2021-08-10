import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EtiquetaComponent } from './list/etiqueta.component';
import { EtiquetaDetailComponent } from './detail/etiqueta-detail.component';
import { EtiquetaUpdateComponent } from './update/etiqueta-update.component';
import { EtiquetaDeleteDialogComponent } from './delete/etiqueta-delete-dialog.component';
import { EtiquetaRoutingModule } from './route/etiqueta-routing.module';

@NgModule({
  imports: [SharedModule, EtiquetaRoutingModule],
  declarations: [EtiquetaComponent, EtiquetaDetailComponent, EtiquetaUpdateComponent, EtiquetaDeleteDialogComponent],
  entryComponents: [EtiquetaDeleteDialogComponent],
})
export class EtiquetaModule {}
