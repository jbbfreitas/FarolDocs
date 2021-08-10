import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrgaoEmissor } from '../orgao-emissor.model';

@Component({
  selector: 'jhi-orgao-emissor-detail',
  templateUrl: './orgao-emissor-detail.component.html',
})
export class OrgaoEmissorDetailComponent implements OnInit {
  orgaoEmissor: IOrgaoEmissor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgaoEmissor }) => {
      this.orgaoEmissor = orgaoEmissor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
