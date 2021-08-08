import { IProjeto } from 'app/entities/projeto/projeto.model';

export interface IDocumento {
  id?: number;
  assunto?: string | null;
  descricao?: string | null;
  etiqueta?: string | null;
  url?: string | null;
  ementa?: string | null;
  projeto?: IProjeto | null;
}

export class Documento implements IDocumento {
  constructor(
    public id?: number,
    public assunto?: string | null,
    public descricao?: string | null,
    public etiqueta?: string | null,
    public url?: string | null,
    public ementa?: string | null,
    public projeto?: IProjeto | null
  ) {}
}

export function getDocumentoIdentifier(documento: IDocumento): number | undefined {
  return documento.id;
}
