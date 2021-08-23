import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentoEtiqueta } from '../documento-etiqueta.model';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';

@Component({
  templateUrl: './documento-etiqueta-delete-dialog.component.html',
})
export class DocumentoEtiquetaDeleteDialogComponent {
  documentoEtiqueta?: IDocumentoEtiqueta;

  constructor(protected documentoEtiquetaService: DocumentoEtiquetaService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.documentoEtiquetaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
