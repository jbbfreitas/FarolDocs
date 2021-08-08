import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'documento',
        data: { pageTitle: 'farolDocsApp.documento.home.title' },
        loadChildren: () => import('./documento/documento.module').then(m => m.DocumentoModule),
      },
      {
        path: 'projeto',
        data: { pageTitle: 'farolDocsApp.projeto.home.title' },
        loadChildren: () => import('./projeto/projeto.module').then(m => m.ProjetoModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
