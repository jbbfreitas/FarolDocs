import { IUser } from 'app/entities/user/user.model';
import { IDocumento } from 'app/entities/documento/documento.model';

export interface IDocumentoUser {
  id?: number;
  user?: IUser | null;
  documento?: IDocumento | null;
}

export class DocumentoUser implements IDocumentoUser {
  constructor(public id?: number, public user?: IUser | null, public documento?: IDocumento | null) {}
}

export function getDocumentoUserIdentifier(documentoUser: IDocumentoUser): number | undefined {
  return documentoUser.id;
}
