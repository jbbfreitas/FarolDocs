import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrgaoEmissor } from '../orgao-emissor.model';
import { OrgaoEmissorService } from '../service/orgao-emissor.service';

@Component({
  templateUrl: './orgao-emissor-delete-dialog.component.html',
})
export class OrgaoEmissorDeleteDialogComponent {
  orgaoEmissor?: IOrgaoEmissor;

  constructor(protected orgaoEmissorService: OrgaoEmissorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgaoEmissorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
