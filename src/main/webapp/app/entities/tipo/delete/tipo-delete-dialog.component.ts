import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipo } from '../tipo.model';
import { TipoService } from '../service/tipo.service';

@Component({
  templateUrl: './tipo-delete-dialog.component.html',
})
export class TipoDeleteDialogComponent {
  tipo?: ITipo;

  constructor(protected tipoService: TipoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
