import { IPonudjaci } from 'app/entities/ponudjaci/ponudjaci.model';

export interface IPonude {
  id?: number;
  sifraPostupka?: number | null;
  nazivLijeka?: string | null;
  iznos?: string | null;
  ponudjaci?: IPonudjaci | null;
}

export class Ponude implements IPonude {
  constructor(
    public id?: number,
    public sifraPostupka?: number | null,
    public nazivLijeka?: string | null,
    public iznos?: string | null,
    public ponudjaci?: IPonudjaci | null
  ) {}
}

export function getPonudeIdentifier(ponude: IPonude): number | undefined {
  return ponude.id;
}
