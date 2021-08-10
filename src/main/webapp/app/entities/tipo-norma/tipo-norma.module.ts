import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoNormaComponent } from './list/tipo-norma.component';
import { TipoNormaDetailComponent } from './detail/tipo-norma-detail.component';
import { TipoNormaUpdateComponent } from './update/tipo-norma-update.component';
import { TipoNormaDeleteDialogComponent } from './delete/tipo-norma-delete-dialog.component';
import { TipoNormaRoutingModule } from './route/tipo-norma-routing.module';

@NgModule({
  imports: [SharedModule, TipoNormaRoutingModule],
  declarations: [TipoNormaComponent, TipoNormaDetailComponent, TipoNormaUpdateComponent, TipoNormaDeleteDialogComponent],
  entryComponents: [TipoNormaDeleteDialogComponent],
})
export class TipoNormaModule {}
