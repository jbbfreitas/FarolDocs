import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TipoComponent } from './list/tipo.component';
import { TipoDetailComponent } from './detail/tipo-detail.component';
import { TipoUpdateComponent } from './update/tipo-update.component';
import { TipoDeleteDialogComponent } from './delete/tipo-delete-dialog.component';
import { TipoRoutingModule } from './route/tipo-routing.module';

@NgModule({
  imports: [SharedModule, TipoRoutingModule],
  declarations: [TipoComponent, TipoDetailComponent, TipoUpdateComponent, TipoDeleteDialogComponent],
  entryComponents: [TipoDeleteDialogComponent],
})
export class TipoModule {}
