import { useEffect, useState } from 'react'
import { agricultureAPI } from '../services/api'
import DataTable from '../components/DataTable'

// zones peut être ["Zone_A", ...] ou [{ zoneId: "Zone_A" }, ...]
const zoneId = (z) => (typeof z === 'string' ? z : z.zoneId)

export default function Irrigation() {
  const [zones, setZones] = useState([])
  const [selected, setSelected] = useState('')
  const [history, setHistory] = useState([])
  const [error, setError] = useState('')

  useEffect(() => {
    agricultureAPI
      .getZones()
      .then((res) => {
        const list = (res.data?.zones ?? []).map(zoneId)
        setZones(list)
        if (list.length) setSelected(list[0])
      })
      .catch((e) => setError(e.message))
  }, [])

  useEffect(() => {
    if (!selected) return
    agricultureAPI
      .getIrrigationHistory(selected)
      .then((res) => setHistory(res.data?.historiqueIrrigation ?? []))
      .catch((e) => setError(e.message))
  }, [selected])

  return (
    <section className="section">
      <h2>💧 Irrigation</h2>
      {error && <p className="error">{error}</p>}

      <label>Select a zone:</label>
      <select value={selected} onChange={(e) => setSelected(e.target.value)}>
        {zones.map((z) => (
          <option key={z} value={z}>
            {z}
          </option>
        ))}
      </select>

      <div className="card">
        <h3>Historique d'irrigation ({history.length})</h3>
        <DataTable rows={history} />
      </div>
    </section>
  )
}
