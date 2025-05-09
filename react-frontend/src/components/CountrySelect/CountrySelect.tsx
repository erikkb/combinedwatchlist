import "./CountrySelect.css"

const countries = ["AF", "AL", "DZ", "AD", "AO", "AG", "AR", "AM", "AU", "AT", "AZ",
    "BS", "BH", "BD", "BB", "BY", "BE", "BZ", "BJ", "BT", "BO", "BA",
    "BW", "BR", "BN", "BG", "BF", "BI", "KH", "CM", "CA", "CV", "CF",
    "TD", "CL", "CN", "CO", "KM", "CD", "CG", "CR", "CI", "HR", "CU",
    "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ", "ER",
    "EE", "ET", "FJ", "FI", "FR", "GA", "GM", "GE", "GH", "GR", "GD",
    "GT", "GN", "GW", "GY", "HT", "HN", "HU", "IS", "IN", "ID", "IR",
    "IQ", "IE", "IL", "IT", "JM", "JP", "JO", "KZ", "KE", "KI", "KP",
    "KR", "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT",
    "LU", "MG", "MW", "MY", "MV", "ML", "MT", "MH", "MR", "MU", "MX",
    "FM", "MD", "MC", "MN", "ME", "MA", "MZ", "MM", "NA", "NR", "NP",
    "NL", "NZ", "NI", "NE", "NG", "MK", "NO", "OM", "PK", "PW", "PS",
    "PA", "PG", "PY", "PE", "PH", "PL", "PT", "QA", "RO", "RU", "RW",
    "KN", "LC", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL",
    "SG", "SK", "SI", "SB", "SO", "ZA", "SS", "ES", "LK", "SD", "SR",
    "SE", "CH", "SY", "TJ", "TZ", "TH", "TL", "TG", "TO", "TT", "TN",
    "TR", "TM", "TV", "UG", "UA", "AE", "UY", "UZ", "VA", "VE", "VN",
    "VU", "YE", "ZM", "ZW"];

export default function CountrySelect({ selectedCountry, onChange }: { selectedCountry: string, onChange: (c: string) => void }) {
  return (
    <label>
      Select Country:&nbsp;
      <select value={selectedCountry} onChange={(e) => onChange(e.target.value)}>
        <option key={"VPN"} value={"VPN"}>VPN</option>
        <option disabled>──</option>
        <option key={"DE"} value={"DE"}>DE</option>
        <option key={"GB"} value={"GB"}>GB</option>
        <option key={"US"} value={"US"}>US</option>
        <option disabled>──</option>
        {countries.map(c => (
          <option key={c} value={c}>{c}</option>
        ))}
      </select>
    </label>
  );
}
