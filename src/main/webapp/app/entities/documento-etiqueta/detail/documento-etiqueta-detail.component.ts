import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentoEtiqueta } from '../documento-etiqueta.model';

@Component({
  selector: 'jhi-documento-etiqueta-detail',
  templateUrl: './documento-etiqueta-detail.component.html',
})
export class DocumentoEtiquetaDetailComponent implements OnInit {
  documentoEtiqueta: IDocumentoEtiqueta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentoEtiqueta }) => {
      this.documentoEtiqueta = documentoEtiqueta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
