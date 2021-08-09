import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipo } from '../tipo.model';

@Component({
  selector: 'jhi-tipo-detail',
  templateUrl: './tipo-detail.component.html',
})
export class TipoDetailComponent implements OnInit {
  tipo: ITipo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipo }) => {
      this.tipo = tipo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
