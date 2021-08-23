import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DocumentoUserComponent } from './list/documento-user.component';
import { DocumentoUserDetailComponent } from './detail/documento-user-detail.component';
import { DocumentoUserUpdateComponent } from './update/documento-user-update.component';
import { DocumentoUserDeleteDialogComponent } from './delete/documento-user-delete-dialog.component';
import { DocumentoUserRoutingModule } from './route/documento-user-routing.module';

@NgModule({
  imports: [SharedModule, DocumentoUserRoutingModule],
  declarations: [DocumentoUserComponent, DocumentoUserDetailComponent, DocumentoUserUpdateComponent, DocumentoUserDeleteDialogComponent],
  entryComponents: [DocumentoUserDeleteDialogComponent],
})
export class DocumentoUserModule {}
