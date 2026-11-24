import { useEffect, useState } from 'react'
import { agricultureAPI, toArray } from '../services/api'
import DataTable from '../components/DataTable'
import Lookup from '../components/Lookup'
import JsonForm from '../components/JsonForm'

export default function Sensors() {
  const [readings, setReadings] = useState([])
  const [error, setError] = useState('')

  const loadReadings = () =>
    agricultureAPI
      .getReadings()
      .then((r) => setReadings(toArray(r)))
      .catch((e) => setError(e.message))

  useEffect(() => {
    loadReadings()
  }, [])

  return (
    <section className="section">
      <h2>📡 Sensors</h2>

      <Lookup title="Capteurs par statut" placeholder="Statut (ex: ACTIVE)" onSearch={agricultureAPI.getSensorsByStatus} />
      <Lookup title="Capteur par ID" placeholder="ID du capteur" onSearch={agricultureAPI.getSensor} />
      <Lookup title="Zone par ID" placeholder="ID de la zone (ex: Zone_D)" onSearch={agricultureAPI.getZone} />
      <Lookup title="Mesure par ID" placeholder="ID de la mesure" onSearch={agricultureAPI.getReading} />

      <JsonForm
        title="Nouvelle mesure"
        initial={{ sensorId: 1, value: 0 }}
        submitLabel="Ajouter la mesure"
        onSubmit={async (body) => {
          const res = await agricultureAPI.createReading(body)
          loadReadings()
          return res
        }}
      />

      <div className="card">
        <h3>Toutes les mesures</h3>
        {error && <p className="error">{error}</p>}
        <DataTable rows={readings} />
      </div>
    </section>
  )
}
