import { IEtiqueta } from 'app/entities/etiqueta/etiqueta.model';
import { IDocumento } from 'app/entities/documento/documento.model';

export interface IDocumentoEtiqueta {
  id?: number;
  etiqueta?: IEtiqueta | null;
  documento?: IDocumento | null;
}

export class DocumentoEtiqueta implements IDocumentoEtiqueta {
  constructor(public id?: number, public etiqueta?: IEtiqueta | null, public documento?: IDocumento | null) {}
}

export function getDocumentoEtiquetaIdentifier(documentoEtiqueta: IDocumentoEtiqueta): number | undefined {
  return documentoEtiqueta.id;
}
