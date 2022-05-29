export interface IPronounce {
  id?: number;
  preferredName?: string | null;
  pronunciationContentType?: string | null;
  pronunciation?: string | null;
}

export class Pronounce implements IPronounce {
  constructor(
    public id?: number,
    public preferredName?: string | null,
    public pronunciationContentType?: string | null,
    public pronunciation?: string | null
  ) {}
}

export function getPronounceIdentifier(pronounce: IPronounce): number | undefined {
  return pronounce.id;
}
