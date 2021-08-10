import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OrgaoEmissorComponent } from './list/orgao-emissor.component';
import { OrgaoEmissorDetailComponent } from './detail/orgao-emissor-detail.component';
import { OrgaoEmissorUpdateComponent } from './update/orgao-emissor-update.component';
import { OrgaoEmissorDeleteDialogComponent } from './delete/orgao-emissor-delete-dialog.component';
import { OrgaoEmissorRoutingModule } from './route/orgao-emissor-routing.module';

@NgModule({
  imports: [SharedModule, OrgaoEmissorRoutingModule],
  declarations: [OrgaoEmissorComponent, OrgaoEmissorDetailComponent, OrgaoEmissorUpdateComponent, OrgaoEmissorDeleteDialogComponent],
  entryComponents: [OrgaoEmissorDeleteDialogComponent],
})
export class OrgaoEmissorModule {}
