import JsonForm from '../components/JsonForm'
import { agricultureAPI } from '../services/api'

// Ajoute dans le JSON les features attendues par tes modèles (température, humidité, etc.)
const SAMPLE = { crop_type: 'Wheat', growth_stage: 'Flowering', field_zone: 'Zone_D' }

export default function Predictions() {
  return (
    <section className="section">
      <h2>🤖 AI Predictions</h2>
      <JsonForm title="Prédiction de maladie" initial={SAMPLE} submitLabel="Prédire" onSubmit={agricultureAPI.predictDisease} />
      <JsonForm title="Prédiction de rendement" initial={SAMPLE} submitLabel="Prédire" onSubmit={agricultureAPI.predictYield} />
    </section>
  )
}
