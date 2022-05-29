export interface IPronounce {
  id?: number;
  login?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  preferredName?: string | null;
  country?: string | null;
  language?: string | null;
  pronunciationContentType?: string | null;
  pronunciation?: string | null;
}

export class Pronounce implements IPronounce {
  constructor(
    public id?: number,
    public login?: string | null,
    public firstName?: string | null,
    public lastName?: string | null,
    public preferredName?: string | null,
    public country?: string | null,
    public language?: string | null,
    public pronunciationContentType?: string | null,
    public pronunciation?: string | null
  ) {}
}

export function getPronounceIdentifier(pronounce: IPronounce): number | undefined {
  return pronounce.id;
}
