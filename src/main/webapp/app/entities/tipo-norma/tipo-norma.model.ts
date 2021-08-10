export interface ITipoNorma {
  id?: number;
  tipo?: string;
  descricao?: string | null;
}

export class TipoNorma implements ITipoNorma {
  constructor(public id?: number, public tipo?: string, public descricao?: string | null) {}
}

export function getTipoNormaIdentifier(tipoNorma: ITipoNorma): number | undefined {
  return tipoNorma.id;
}
