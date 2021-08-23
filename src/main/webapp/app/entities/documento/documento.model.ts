import * as dayjs from 'dayjs';
import { IProjeto } from 'app/entities/projeto/projeto.model';
import { ITipo } from 'app/entities/tipo/tipo.model';
import { IOrgaoEmissor } from 'app/entities/orgao-emissor/orgao-emissor.model';
import { ITipoNorma } from 'app/entities/tipo-norma/tipo-norma.model';
import { SituacaoDocumento } from 'app/entities/enumerations/situacao-documento.model';

export interface IDocumento {
  id?: number;
  assunto?: string | null;
  descricao?: string | null;
  ementa?: string | null;
  url?: string | null;
  numero?: string | null;
  ano?: number | null;
  situacao?: SituacaoDocumento | null;
  criacao?: dayjs.Dayjs | null;
  projeto?: IProjeto | null;
  tipo?: ITipo | null;
  orgaoEmissor?: IOrgaoEmissor | null;
  tipoNorma?: ITipoNorma | null;
}

export class Documento implements IDocumento {
  constructor(
    public id?: number,
    public assunto?: string | null,
    public descricao?: string | null,
    public ementa?: string | null,
    public url?: string | null,
    public numero?: string | null,
    public ano?: number | null,
    public situacao?: SituacaoDocumento | null,
    public criacao?: dayjs.Dayjs | null,
    public projeto?: IProjeto | null,
    public tipo?: ITipo | null,
    public orgaoEmissor?: IOrgaoEmissor | null,
    public tipoNorma?: ITipoNorma | null
  ) {}
}

export function getDocumentoIdentifier(documento: IDocumento): number | undefined {
  return documento.id;
}
