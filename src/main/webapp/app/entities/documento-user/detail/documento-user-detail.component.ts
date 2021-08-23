import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocumentoUser } from '../documento-user.model';

@Component({
  selector: 'jhi-documento-user-detail',
  templateUrl: './documento-user-detail.component.html',
})
export class DocumentoUserDetailComponent implements OnInit {
  documentoUser: IDocumentoUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documentoUser }) => {
      this.documentoUser = documentoUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
