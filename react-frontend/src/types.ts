export interface Movie {
    id: number;
    title: string;
    poster_path: string | null;
    release_date: string;
    providers: Providers | null;
    providerinfo_lastupdate: string | null;
  }
  
  export interface Show {
    id: number;
    name: string;
    poster_path: string | null;
    first_air_date: string;
    providers: Providers | null;
    providerinfo_lastupdate: string | null;
  }

  export interface User {
    userId: number;
    username: string;
    role: string;
    email?: string;
  }

export type Providers = { [countryCode: string]: ProvidersPerCountry };

export interface ProvidersPerCountry {
  flatrate?: Provider[];
  buy?: Provider[];
  rent?: Provider[];
}

export interface Provider {
  providerId: number;
  providerName: string;
  logoPath: string;
}
