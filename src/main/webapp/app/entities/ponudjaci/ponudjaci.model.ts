export interface IPonudjaci {
  id?: number;
  nazivPonudjaca?: string | null;
}

export class Ponudjaci implements IPonudjaci {
  constructor(public id?: number, public nazivPonudjaca?: string | null) {}
}

export function getPonudjaciIdentifier(ponudjaci: IPonudjaci): number | undefined {
  return ponudjaci.id;
}
