export interface IDocumento {
  id?: number;
  projeto?: string | null;
  assunto?: string | null;
  descricao?: string | null;
  etiqueta?: string | null;
  url?: string | null;
}

export class Documento implements IDocumento {
  constructor(
    public id?: number,
    public projeto?: string | null,
    public assunto?: string | null,
    public descricao?: string | null,
    public etiqueta?: string | null,
    public url?: string | null
  ) {}
}

export function getDocumentoIdentifier(documento: IDocumento): number | undefined {
  return documento.id;
}
