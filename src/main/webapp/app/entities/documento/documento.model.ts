import { IProjeto } from 'app/entities/projeto/projeto.model';
import { ITipo } from 'app/entities/tipo/tipo.model';

export interface IDocumento {
  id?: number;
  assunto?: string | null;
  descricao?: string | null;
  etiqueta?: string | null;
  ementa?: string | null;
  url?: string | null;
  projeto?: IProjeto | null;
  tipo?: ITipo | null;
}

export class Documento implements IDocumento {
  constructor(
    public id?: number,
    public assunto?: string | null,
    public descricao?: string | null,
    public etiqueta?: string | null,
    public ementa?: string | null,
    public url?: string | null,
    public projeto?: IProjeto | null,
    public tipo?: ITipo | null
  ) {}
}

export function getDocumentoIdentifier(documento: IDocumento): number | undefined {
  return documento.id;
}
