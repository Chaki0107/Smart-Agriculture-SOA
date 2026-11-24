import { useEffect, useState } from 'react'
import { agricultureAPI } from '../services/api'

export default function Dashboard() {
  const [zones, setZones] = useState([])
  const [cultures, setCultures] = useState([])
  const [selectedZone, setSelectedZone] = useState('')
  const [parcelle, setParcelle] = useState(null)
  const [irrigation, setIrrigation] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  // Charger zones et cultures
  useEffect(() => {
    async function loadInitialData() {
      try {
        setLoading(true)
        setError('')

        const [zonesResponse, culturesResponse] = await Promise.all([
          agricultureAPI.getZones(),
          agricultureAPI.getCultures()
        ])

        const zonesData = zonesResponse.data.zones
        const culturesData = culturesResponse.data.cultures

        setZones(zonesData)
        setCultures(culturesData)

        if (zonesData.length > 0) {
          setSelectedZone(zonesData[0])
        }
      } catch (err) {
        console.error(err)
        setError('Impossible de récupérer les données depuis Mule ESB.')
      } finally {
        setLoading(false)
      }
    }

    loadInitialData()
  }, [])

  // Charger parcelle et irrigation
  useEffect(() => {
    if (!selectedZone) return

    async function loadZoneData() {
      try {
        const [parcelResponse, irrigationResponse] = await Promise.all([
          agricultureAPI.getParcelle(selectedZone),
          agricultureAPI.getIrrigationHistory(selectedZone)
        ])

        setParcelle(parcelResponse.data.parcelle)
        setIrrigation(irrigationResponse.data.historiqueIrrigation)
      } catch (err) {
        console.error(err)
        setParcelle(null)
        setIrrigation([])
      }
    }

    loadZoneData()
  }, [selectedZone])

  return (
    <>
      <section className="welcome">
        <h2>Welcome to SmartFarm 🌱</h2>
        <p>Monitor your agricultural zones, sensors, crops and AI predictions.</p>
      </section>

      {loading && <p>Loading data...</p>}

      {error && <div className="error-message">{error}</div>}

      {!loading && !error && (
        <>
          <section className="stats">
            <div className="stat-card">
              <span>🌾</span>
              <h3>Agricultural Zones</h3>
              <p>{zones.length}</p>
            </div>

            <div className="stat-card">
              <span>🌱</span>
              <h3>Crops</h3>
              <p>{cultures.length}</p>
            </div>

            <div className="stat-card">
              <span>💧</span>
              <h3>Irrigation Records</h3>
              <p>{irrigation.length}</p>
            </div>

            <div className="stat-card">
              <span>📍</span>
              <h3>Selected Zone</h3>
              <p>{selectedZone || 'N/A'}</p>
            </div>
          </section>

          <section className="section">
            <h2>My Agricultural Zones</h2>

            <label>Select a zone:</label>

            <select value={selectedZone} onChange={(e) => setSelectedZone(e.target.value)}>
              {zones.map((zone) => (
                <option key={zone} value={zone}>
                  {zone}
                </option>
              ))}
            </select>

            {parcelle && (
              <div className="zone-card">
                <h3>{parcelle.zoneId}</h3>

                <p>
                  <strong>Latitude:</strong> {parcelle.latitude}
                </p>

                <p>
                  <strong>Longitude:</strong> {parcelle.longitude}
                </p>

                <p>
                  <strong>Growth Stage:</strong> {parcelle.stadeCroissance}
                </p>

                <h4>Crops</h4>

                {parcelle.cultures?.map((crop, index) => (
                  <span className="crop-tag" key={index}>
                    🌱 {crop}
                  </span>
                ))}
              </div>
            )}
          </section>

          <section className="section">
            <h2>Available Crops</h2>

            <div className="crop-list">
              {cultures.map((crop, index) => (
                <div className="crop-card" key={index}>
                  🌱 {crop}
                </div>
              ))}
            </div>
          </section>

          <section className="section">
            <h2>Irrigation History</h2>

            {irrigation.length > 0 ? (
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Amount (L)</th>
                    <th>Flow Rate (L/min)</th>
                  </tr>
                </thead>

                <tbody>
                  {irrigation.map((record, index) => (
                    <tr key={record.id || index}>
                      <td>{record.timestamp}</td>
                      <td>{record.irrigationStatus}</td>
                      <td>{record.irrigationAmountL}</td>
                      <td>{record.waterFlowRateLMin}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>No irrigation history available for this zone.</p>
            )}
          </section>
        </>
      )}
    </>
  )
}
