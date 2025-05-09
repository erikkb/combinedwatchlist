import "./ProviderSelect.css"

const highlightedProviders = [
  "Amazon",
  "Apple",
  "Crunchyroll",
  "Disney",
  "Discovery",
  "Hulu",
  "ITVX",
  "Joyn",
  "Magenta",
  "Max",
  "Netflix",
  "NOW",
  "Paramount",
  "Peacock",
  "RTL",
  "Sky",
  "WOW",
  "iQIYI"]; // consider fetching dynamically

export default function ProviderSelect({ selectedProviders, onChange }: { selectedProviders: string[], onChange: (c: string[]) => void }) {
  function handleToggle(provider: string) {
    if (selectedProviders.includes(provider)) {
      onChange(selectedProviders.filter(p => p !== provider));
    } else {
      onChange([...selectedProviders, provider]);
    }
  }

  return (
    <fieldset className="fieldset">
      <legend>Highlight Providers:</legend>
      <div className="checkbox-grid">
        {highlightedProviders.map(provider => (
          <label key={provider} style={{ marginRight: "1rem" }}>
            <input
              type="checkbox"
              checked={selectedProviders.includes(provider)}
              onChange={() => handleToggle(provider)}
            />
            {provider}
          </label>
        ))}
      </div>
    </fieldset>
  );
}
