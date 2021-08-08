import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjeto } from '../projeto.model';

@Component({
  selector: 'jhi-projeto-detail',
  templateUrl: './projeto-detail.component.html',
})
export class ProjetoDetailComponent implements OnInit {
  projeto: IProjeto | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ projeto }) => {
      this.projeto = projeto;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
