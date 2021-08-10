export interface IOrgaoEmissor {
  id?: number;
  sigla?: string;
  nome?: string;
}

export class OrgaoEmissor implements IOrgaoEmissor {
  constructor(public id?: number, public sigla?: string, public nome?: string) {}
}

export function getOrgaoEmissorIdentifier(orgaoEmissor: IOrgaoEmissor): number | undefined {
  return orgaoEmissor.id;
}
