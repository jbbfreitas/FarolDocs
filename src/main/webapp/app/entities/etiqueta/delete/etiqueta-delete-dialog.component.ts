import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEtiqueta } from '../etiqueta.model';
import { EtiquetaService } from '../service/etiqueta.service';

@Component({
  templateUrl: './etiqueta-delete-dialog.component.html',
})
export class EtiquetaDeleteDialogComponent {
  etiqueta?: IEtiqueta;

  constructor(protected etiquetaService: EtiquetaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.etiquetaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
