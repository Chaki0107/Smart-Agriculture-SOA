import { useEffect, useState } from 'react'
import { agricultureAPI, toArray } from '../services/api'
import DataTable from '../components/DataTable'
import Lookup from '../components/Lookup'
import JsonForm from '../components/JsonForm'

export default function Alerts() {
  const [alerts, setAlerts] = useState([])
  const [rules, setRules] = useState([])
  const [error, setError] = useState('')

  const load = async () => {
    try {
      setError('')
      const [a, r] = await Promise.all([agricultureAPI.getAlerts(), agricultureAPI.getAlertRules()])
      setAlerts(toArray(a))
      setRules(toArray(r))
    } catch (e) {
      setError(e.message)
    }
  }

  useEffect(() => {
    load()
  }, [])

  const acknowledge = async (id) => {
    try {
      await agricultureAPI.acknowledgeAlert(id)
      load()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <section className="section">
      <h2>🔔 Alerts</h2>
      {error && <p className="error">{error}</p>}

      <div className="card">
        <h3>Alertes</h3>
        <DataTable
          rows={alerts}
          renderActions={(row) => (
            <button className="btn" onClick={() => acknowledge(row.id)}>
              Acquitter
            </button>
          )}
        />
      </div>

      <Lookup title="Alerte par ID" placeholder="ID de l'alerte" onSearch={agricultureAPI.getAlert} />

      <div className="card">
        <h3>Règles d'alerte</h3>
        <DataTable rows={rules} />
      </div>

      <Lookup title="Règles par zone" placeholder="Zone (ex: Zone_D)" onSearch={agricultureAPI.getAlertRulesByZone} />

      <JsonForm
        title="Créer une règle"
        initial={{ zoneId: 'Zone_D', metric: 'soil_moisture', operator: '<', threshold: 30, severity: 'HIGH' }}
        submitLabel="Créer la règle"
        onSubmit={async (body) => {
          const res = await agricultureAPI.createAlertRule(body)
          load()
          return res
        }}
      />

      <JsonForm title="Évaluer les alertes" initial={{}} submitLabel="Évaluer" onSubmit={agricultureAPI.evaluateAlerts} />
      <JsonForm title="Évaluer avec le modèle ML" initial={{}} submitLabel="Évaluer (ML)" onSubmit={agricultureAPI.evaluateAlertsML} />
    </section>
  )
}
