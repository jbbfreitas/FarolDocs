import * as dayjs from 'dayjs';
import { SituacaoProjeto } from 'app/entities/enumerations/situacao-projeto.model';

export interface IProjeto {
  id?: number;
  nome?: string;
  inicio?: dayjs.Dayjs;
  fim?: dayjs.Dayjs | null;
  situacao?: SituacaoProjeto | null;
}

export class Projeto implements IProjeto {
  constructor(
    public id?: number,
    public nome?: string,
    public inicio?: dayjs.Dayjs,
    public fim?: dayjs.Dayjs | null,
    public situacao?: SituacaoProjeto | null
  ) {}
}

export function getProjetoIdentifier(projeto: IProjeto): number | undefined {
  return projeto.id;
}
