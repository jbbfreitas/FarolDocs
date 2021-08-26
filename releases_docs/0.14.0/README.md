## Documentação da Realease 0.14.0

Antes de começar, certifique-se de que o seu repositório esteja atualizado até a release `0.13.0` e que sua aplicação esteja funcionando normalmente pois em caso de `crash` na aplicação você poderá retornar a esse estado.

### O que iremos fazer nesta release? 
- Implementar uma consulta do tipo Mestre-Detalhe para Documentos e Etiquetas

Por que isso?
Lembre-se que na release 0.13.0 nós excluimos a relação `M:N` entre as entidades Documento e Etiqueta e criamos uma nova entidade denominada `Documento-Etiqueta` com relação na razão de `M:1` com a entidade `Documento`.

Pois bem, nessa relação o lado `1` é denominado `Mestre` e o lado `M` é denominado `Detalhe`, ou seja, para `1` ocorrência de `Documento`(Mestre), teremos `M` ocorrências de `Documento-Etiqueta`(Detalhe).

Acontece que o jhipster não implementa automaticamente a apresentação desse tipo de relação. Por isso teremos que realizar essas mudanças na 'mão'.

## Implementando as mudanças

Serão necessárias pequenas alterações no `back-end`(java) e no `front-end`. Vamos a elas:

::: :walking: Passo a passo :::

### 1-Criar um botão na página de Documento

Isso será necessário para, a partir da página de documentos, gerenciarmos as etiquetas.

<p align="center">
   <strong>Listagem 1 - Arquivo documento-update.component.html </strong> 
</p>

Insira o trecho de código logo abaixo da tag `</form>`

```html
    <hr>
    <div>
      <button
      type="button"
      [routerLink]="['/documento/',documento.id,'etiqueta']"
      queryParamsHandling="merge"
      class="btn btn-primary btn-sm"
      data-cy="entityEditButton"
    >
      <fa-icon icon="pencil-alt"></fa-icon>
      <span class="d-none d-md-inline" jhiTranslate="entity.action.edit">Edit</span>
    </button>

    </div>
    <hr>
```
O que estamos fazendo? Criando um botão que, ao ser clicado, vai para o endereço que está em `routerLink`.

### 2-Criar o roteamento correto para o click do botão

Isso será necessário para criar o resolver o endereço que foi desiginado em `routerLink`.

<p align="center">
   <strong>Listagem 2 - Arquivo documento-routing.module.ts </strong> 
</p>

Insira o trecho de código conforme orientação abaixo

```typescript
/* ... continuação do código exitente */
  {
    path: ':id/edit',
    component: DocumentoUpdateComponent,
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  /* Final do código existente */
  
  /* Início do trecho a ser incluído <<<----*/
  {
    path: ':id/etiqueta',
    component: DocumentoEtiquetaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    resolve: {
      documento: DocumentoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  /* final do trecho a ser incluído' ---->> */ 

/* ... continuação do código exitente */
];

@NgModule({
  imports: [RouterModule.forChild(documentoRoute)],
  exports: [RouterModule],
})
export class DocumentoRoutingModule {}

```

O que são os `routing.module` ?

Como o próprio nome já diz são módulos de roteamento, ou seja, onde estão especificadas as rotas (path) que o navegador deverá seguir. No caso acima foi criada a rota `path: ':id/etiqueta'`. Na verdade a rota completa será algo como: `http://localhost:8080/documento/3/etiqueta`. Onde o número `3` é o `id` do documento.

Isso significa o seguinte: quando clicarmos no botão recém-criado ele irá seguir essa rota (caminho). Observe que o `id` do documento é passado no próprio endereço url.

Aqui tem um "pulo do gato". Observe o trechinho de código abaixo:

```typescript
    resolve: {
      documento: DocumentoRoutingResolveService,
```
Esse trecho está dizendo que quem irá obter os dados do documento (resolver) é a classe `DocumentoRoutingResolveService`. Ou seja, a partir do `id` do documento essa classe irá instanciar o objeto `documento` buscando os demais dados no `back-end`.

### 3-Criar uma variável de instância para o documento

São apenas 2 pequenas alterações

Declare a variável `documento` logo acima do método construtor

<p align="center">
   <strong>Listagem 3.1 - Arquivo documento-update.component.ts </strong> 
</p>

```typescript
documento: any; /* incluído <<<----*/
  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected documentoService: DocumentoService,
    protected projetoService: ProjetoService,
    protected tipoService: TipoService,
    protected orgaoEmissorService: OrgaoEmissorService,
    protected tipoNormaService: TipoNormaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}
```
<p align="center">
   <strong>Listagem 3.2 - Arquivo documento-update.component.ts </strong> 
</p>

Faça a alteração no método `ngOnInit()` conforme o trecho de código abaixo.

```typescript
  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ documento }) => { /* observer de documento */
      this.documento = documento /* incluído <<<----*/
      if (documento.id === undefined) {
        const today = dayjs().startOf('day');
        documento.criacao = today;
      }

      this.updateForm(documento);

      this.loadRelationshipsOptions();
    });
  }
```
O que foi feito é seguinte: o método `ngOnInit()` possui um `subscriber` que observa pelas alterações em documento, no nosso caso o documento cujo id=3. Essa instância de documento está sendo atribuída à variável `documento`criada na listagem 3.1. Isso por que iremos utilizar o id do documento no `[routerLink]="['/documento/',documento.id,'etiqueta']" `, vide Listagem 1. Esse `documento.id` contém, justamente, a o `id` (número 3) da variavel de instância criada na Listagem 3.1.


### 4-Criar um novo método na camada view para trazer somente as etiquetas de um documento

O que nós pretendemos é que quando clicarmos no botão recémcriado, seja apresentado um formulário contendo as etiquetas do documento que estamos editando. No exemplo que está sendo usado, o documento cujo id =3.

Esse formulário já existe, foi criado pelo jHipster na release anterior (0.13.0). 
Ocorre que ele não está funcionando como a gente deseja, pois traz todas as etiquetas de todos os documentos e não apenas aquelas que se referem ao documento pretendido.

Então vamos ter que fazer uma  alteração na classe `TypeScript` desse formulário. Como são várias pequenas alterações, vamos apresentar a versão final da classe. Sugiro que você utilize o `diff` do git para fazer a comparação linha a linha do que foi alterado.

<p align="center">
   <strong>Listagem 4 - Arquivo documento-etiqueta.component.ts </strong> 
</p>

```typescript
import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDocumentoEtiqueta } from '../documento-etiqueta.model';

import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/config/pagination.constants';
import { DocumentoEtiquetaService } from '../service/documento-etiqueta.service';
import { DocumentoEtiquetaDeleteDialogComponent } from '../delete/documento-etiqueta-delete-dialog.component';
import { IDocumento } from 'app/entities/documento/documento.model';

@Component({
  selector: 'jhi-documento-etiqueta',
  templateUrl: './documento-etiqueta.component.html',
})
export class DocumentoEtiquetaComponent implements OnInit {
  documentoEtiquetas?: IDocumentoEtiqueta[];
  currentSearch: string;
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  documento!: IDocumento;

  constructor(
    protected documentoEtiquetaService: DocumentoEtiquetaService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    if (this.currentSearch) {
      this.documentoEtiquetaService
        .search({
          page: pageToLoad - 1,
          query: this.currentSearch,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe(
          (res: HttpResponse<IDocumentoEtiqueta[]>) => {
            this.isLoading = false;
            this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
          },
          () => {
            this.isLoading = false;
            this.onError();
          }
        );
      return;
    }
    this.loadAllEtiquetasFromDocuments(1, true)//alterado aqui
  return   
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAllEtiquetasFromDocuments(1, true)//alterado aqui

  }

  ngOnInit(): void {
    this.handleNavigation();
    this.loadAllEtiquetasFromDocuments(1, true) //alterado aqui

  }
  
//Método criado para buscar apenas as etiquetas de um determinado documento
    loadAllEtiquetasFromDocuments(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;    
    this.documentoEtiquetaService
    .queryAllEtiquetasFromDocuments({ //Método a ser criado na Listagem 5
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      //sort: this.sort(),
      id: this.documento.id //Este dicionário é parâmetro do request 
    })
    .subscribe(
      (res: HttpResponse<IDocumentoEtiqueta[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );

  }

  trackId(index: number, item: IDocumentoEtiqueta): number {
    return item.id!;
  }

  delete(documentoEtiqueta: IDocumentoEtiqueta): void {
    const modalRef = this.modalService.open(DocumentoEtiquetaDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.documentoEtiqueta = documentoEtiqueta;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAllEtiquetasFromDocuments(1,true);
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      this.documento = data.documento;//
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadAllEtiquetasFromDocuments(pageNumber, true)//alterado aqui

      }
    });
  }

  protected onSuccess(data: IDocumentoEtiqueta[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    this.ngbPaginationPage = this.page;
    if (navigate) {
      this.router.navigate(['/documento-etiqueta'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          search: this.currentSearch,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.documentoEtiquetas = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}

```

### 5-Criar um novo método de serviço para interagir com o back-end

Como o Angular é um framework `mvc` a alteração feita na camada `view` no item 4 deve ter uma correspondência com a camada `controller`. É isso que iremos fazer.

Crie o método `queryAllEtiquetasFromDocuments` em qualquer lugar dentro da classe.

<p align="center">
   <strong>Listagem 5 - Arquivo documento-etiqueta.service.ts </strong> 
</p>

```typescript

queryAllEtiquetasFromDocuments(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    const id: number = req.id ?? 0 ; //Aqui é recebido o id do documento  
    return this.http.get<IDocumentoEtiqueta[]>(`${this.resourceUrl}/doc/${id}`, { params: options, observe: 'response' });
  }
```

Observe a assinatura do método `queryAllEtiquetasFromDocuments(req?: any)`.
Este médoto recebe como parâmetro um dicionário, chave-valor tendo como 
uma de suas chaves  `id: this.documento.id`, vide Listagem 4.

Esse método interage com o `back-end` usando `REST` por intermédio dp método `http.get`.
O método `http.get` é assíncrono e recebe de volta um array de `IDocumentoEtiqueta`. As alterações no `back-end` serão tratadas nos tópicos 6 a 8.

### 6-Criar um novo `Resource`  interagir com o front-end

Na comunicação `front-end` com o `back-end` a classe java que interagem com o angular tem o sufixo `Resource`.

No caso em tela, é a classe `DocumentoEtiquetaResource`. Nessa classe crie um novo método conforme Listagem 6.

No final das contas o que estaos fazendo é invocar um método denominado `findAllEtiquetasDocumento()` para buscar as etiquetas de um determinado documento. Só que esse método está na Classe `DocumentoEtiquetaService`.  O próximo passo então será detalhar esse método nessa classe: esse é o princípio da delegação ou, se preferir, adotar o padrão especialista preconizado pelo Craig Larman no livro "Utilizando UML e Padrões". Leia mais sobre os padrões [GRASP](https://pt.wikipedia.org/wiki/GRASP_(padr%C3%A3o_orientado_a_objetos) preconizado por Larman.


<p align="center">
   <strong>Listagem 6 - Arquivo DocumentoEtiquetaResource.java </strong> 
</p>

```java
/**
     * {@code GET  /documento-etiquetas/doc/{id}} : get all  Etiquetas of one Documento.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentoEtiquetas in body.
     */
    @GetMapping("/documento-etiquetas/doc/{id}")
    public ResponseEntity<List<DocumentoEtiqueta>> getAllEtiquetasDoc(Pageable pageable, @PathVariable Long id ) {
        
        log.debug("request to get a page of etiquetasDoc");
        
        Page<DocumentoEtiqueta> page = documentoEtiquetaService.findAllEtiquetasDocumento(id,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

```
### 7-Criar o método  findAllEtiquetasDocumento()

No nosso ecosistema de ferramentas, estamos utilizando o Spring como o mecanismo de [injeção de dependência](https://blog.algaworks.com/injecao-de-dependencias-spring/) no java. Isso significa que devemos trabalhar com interfaces e com suas respectivas implementações. Portanto esse método deverá ser criado primeiramente na classe `DocumentoEtiquetaService` e depois na sua implementação `DocmentoEtiquetaServiceImpl`.

Então vamos lá, adicione na última linha da classe a linha de código da Listagem 7.

<p align="center">
   <strong>Listagem 7.1 - Arquivo DocumentoEtiquetaService.java </strong> 
</p>

```java
   Page<DocumentoEtiqueta> findAllEtiquetasDocumento(Long id, Pageable pageable);
```

Observe que esse método não tem corpo, visto que a Classe `DocumentoEtiquetaService` é uma interface. O corpo do método ( sua implementação) será detahado na Classe `DocumentoEtiquetaServiceImpl`, Listagem 8.


<p align="center">
   <strong>Listagem 7.2 - Arquivo DocumentoEtiquetaServiceImpl.java </strong> 
</p>

```java
@Override
    @Transactional(readOnly = true)
    public Page<DocumentoEtiqueta> findAllEtiquetasDocumento(Long id,Pageable pageable) {
        
         return documentoEtiquetaRepository.findAllEtiquetasDocumento(id, pageable);
    }

```

Mais uma vez a delegação. Parece meio estranho delegarmos tanto. Ocorre que estamos desenvolvendo em camadas e a camada que irá fazer o trabalho de verdade é a `Repository`. Ela tem esse nome não por acaso pois é a responsável por buscar as informações no repositório de dados. Vamos fazer isso no passo 8.

### 8-Criar o método  findAllEtiquetasDocumento() na camada de dados.

<p align="center">
   <strong>Listagem 8 - Arquivo DocumentoEtiquetaRepository.java </strong> 
</p>

```java
@Query("SELECT e FROM DocumentoEtiqueta e WHERE e.documento.id = :id")
Page<DocumentoEtiqueta> findAllEtiquetasDocumento(@Param("id") Long id,Pageable pageable);

```

Para esse arquivo será necessário importar os seguintes pacotes:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

```

Vamos desmitificar tudo isso!
Primeiramente é importante frisar que estamos desenvolvendo o projeto na arquitetura em camadas. Há inúmeros benefícios em fazer isso, dentre eles, o isolamento entre as camadas permitindo um baixo acoplamento. Leia mais sobre o padrão [camadas](http://www.dsc.ufcg.edu.br/~jacques/cursos/map/html/arqu/camadas.html).

Depois de entender um pouco mais sobre o padrão camadas, é importante frisar que a classe `Repository` encontra-se na camada mais baixa, ou seja, mais próxima aos dados. A camada mais alta no `back-end` é a `Resource`, a porta de entrada e de saída das informações. Foi por isso que começamos por ela. 

A camada de serviços, `Service`, fica no meio. 

Podemos dividir o que está na listagem 8 em duas partes:

1- A anotação `@Query("SELECT e FROM DocumentoEtiqueta e WHERE e.documento.id = :id")`.

É uma anotação (começa com @). E como [anotação](https://pt.wikipedia.org/wiki/Annotation_(java)) é uma diretiva. Neste caso dizendo ao Spring para associar uma `OQL` (Object Query Language) ao método. A OQL é uma SQL para objetos. 

2- A segunda parte é o método propriamente dito:

```java
Page<DocumentoEtiqueta> findAllEtiquetasDocumento(@Param("id") Long id,Pageable pageable);
```

Essas duas partes trabalham em conjunto e não podem ser dissociadas uma da outra, ou seja, a implementação (corpo do método) será feita em tempo de execução pelo Spring e que, no final das contas, vai executar a query. A query está dizendo o seguinte:

> "Selecione todas as etiquetas do documento cujo id será fornecido como parâmetro."


::: :pushpin: Importante :::

Ao final de todo o processo, você terá que ter 9 mudanças no Git.


Salve, compile, execute e teste.

```
mvn
```