import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoNorma } from '../tipo-norma.model';
import { TipoNormaService } from '../service/tipo-norma.service';

@Component({
  templateUrl: './tipo-norma-delete-dialog.component.html',
})
export class TipoNormaDeleteDialogComponent {
  tipoNorma?: ITipoNorma;

  constructor(protected tipoNormaService: TipoNormaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoNormaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
