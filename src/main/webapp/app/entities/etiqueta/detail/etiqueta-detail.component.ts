import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEtiqueta } from '../etiqueta.model';

@Component({
  selector: 'jhi-etiqueta-detail',
  templateUrl: './etiqueta-detail.component.html',
})
export class EtiquetaDetailComponent implements OnInit {
  etiqueta: IEtiqueta | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etiqueta }) => {
      this.etiqueta = etiqueta;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
