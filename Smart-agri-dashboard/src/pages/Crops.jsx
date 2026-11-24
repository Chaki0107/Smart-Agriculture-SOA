import Lookup from '../components/Lookup'
import { agricultureAPI } from '../services/api'

export default function Crops() {
  return (
    <section className="section">
      <h2>🌱 Crops</h2>
      <Lookup
        title="Cultures par type"
        placeholder="Type de culture (ex: Wheat)"
        onSearch={agricultureAPI.getCrop}
      />
    </section>
  )
}
