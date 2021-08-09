export interface ITipo {
  id?: number;
  nome?: string;
  descricao?: string | null;
}

export class Tipo implements ITipo {
  constructor(public id?: number, public nome?: string, public descricao?: string | null) {}
}

export function getTipoIdentifier(tipo: ITipo): number | undefined {
  return tipo.id;
}
