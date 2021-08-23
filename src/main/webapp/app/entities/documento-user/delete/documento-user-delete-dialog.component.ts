import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentoUser } from '../documento-user.model';
import { DocumentoUserService } from '../service/documento-user.service';

@Component({
  templateUrl: './documento-user-delete-dialog.component.html',
})
export class DocumentoUserDeleteDialogComponent {
  documentoUser?: IDocumentoUser;

  constructor(protected documentoUserService: DocumentoUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentoUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
