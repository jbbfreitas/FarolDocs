export interface IEtiqueta {
  id?: number;
  nome?: string;
}

export class Etiqueta implements IEtiqueta {
  constructor(public id?: number, public nome?: string) {}
}

export function getEtiquetaIdentifier(etiqueta: IEtiqueta): number | undefined {
  return etiqueta.id;
}
