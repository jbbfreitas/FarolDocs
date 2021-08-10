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
      {
        path: 'tipo',
        data: { pageTitle: 'farolDocsApp.tipo.home.title' },
        loadChildren: () => import('./tipo/tipo.module').then(m => m.TipoModule),
      },
      {
        path: 'etiqueta',
        data: { pageTitle: 'farolDocsApp.etiqueta.home.title' },
        loadChildren: () => import('./etiqueta/etiqueta.module').then(m => m.EtiquetaModule),
      },
      {
        path: 'orgao-emissor',
        data: { pageTitle: 'farolDocsApp.orgaoEmissor.home.title' },
        loadChildren: () => import('./orgao-emissor/orgao-emissor.module').then(m => m.OrgaoEmissorModule),
      },
      {
        path: 'tipo-norma',
        data: { pageTitle: 'farolDocsApp.tipoNorma.home.title' },
        loadChildren: () => import('./tipo-norma/tipo-norma.module').then(m => m.TipoNormaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
