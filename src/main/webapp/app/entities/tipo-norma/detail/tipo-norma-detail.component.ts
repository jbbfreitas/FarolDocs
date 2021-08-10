import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoNorma } from '../tipo-norma.model';

@Component({
  selector: 'jhi-tipo-norma-detail',
  templateUrl: './tipo-norma-detail.component.html',
})
export class TipoNormaDetailComponent implements OnInit {
  tipoNorma: ITipoNorma | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoNorma }) => {
      this.tipoNorma = tipoNorma;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
