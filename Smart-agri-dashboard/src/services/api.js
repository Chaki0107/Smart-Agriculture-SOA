const API_BASE = '/api/agriculture'
const enc = encodeURIComponent

async function apiRequest(path, { method = 'GET', body } = {}) {
  const options = { method }
  if (body !== undefined) {
    options.headers = { 'Content-Type': 'application/json' }
    options.body = JSON.stringify(body)
  }

  const response = await fetch(`${API_BASE}${path}`, options)
  const text = await response.text()

  if (!response.ok) {
    throw new Error(`HTTP ${response.status}: ${text}`)
  }
  // Certaines réponses (PATCH) peuvent être vides
  return text ? JSON.parse(text) : null
}

// Transforme une réponse en tableau (tableau direct ou { content: [...] })
export const toArray = (res) =>
  Array.isArray(res) ? res : Array.isArray(res?.content) ? res.content : res ? [res] : []

export const agricultureAPI = {
  // ---------- GraphQL ----------
  getZones: () => apiRequest('/graphql/zones'),
  getCultures: () => apiRequest('/graphql/cultures'),
  getParcelle: (zoneId) => apiRequest(`/graphql/parcelles/${enc(zoneId)}`),
  getIrrigationHistory: (zoneId) => apiRequest(`/graphql/irrigation/${enc(zoneId)}`),

  // ---------- IoT ----------
  getZone: (zoneId) => apiRequest(`/zones/${enc(zoneId)}`),
  getCrop: (cropType) => apiRequest(`/crops/${enc(cropType)}`),
  getSensorsByStatus: (status) => apiRequest(`/sensors/status/${enc(status)}`),
  getSensor: (sensorId) => apiRequest(`/sensors/${enc(sensorId)}`),
  getReadings: () => apiRequest('/readings'),
  getReading: (id) => apiRequest(`/readings/${enc(id)}`),
  createReading: (reading) => apiRequest('/readings', { method: 'POST', body: reading }),

  // ---------- Alertes ----------
  getAlerts: () => apiRequest('/alerts'),
  getAlert: (id) => apiRequest(`/alerts/${enc(id)}`),
  acknowledgeAlert: (id) => apiRequest(`/alerts/${enc(id)}/acknowledge`, { method: 'PATCH' }),
  getAlertRules: () => apiRequest('/alertes/rules'), // (typo "alertes" dans le flow Mule)
  getAlertRulesByZone: (zoneId) => apiRequest(`/alerts/rules?zoneId=${enc(zoneId)}`),
  createAlertRule: (rule) => apiRequest('/alerts/rules', { method: 'POST', body: rule }),
  evaluateAlerts: (data) => apiRequest('/alerts/evaluate', { method: 'POST', body: data }),
  evaluateAlertsML: (data) => apiRequest('/alerts/evaluate-ml', { method: 'POST', body: data }),

  // ---------- Prédictions IA ----------
  predictDisease: (sensors) => apiRequest('/predict/disease', { method: 'POST', body: sensors }),
  predictYield: (sensors) => apiRequest('/predict/yield', { method: 'POST', body: sensors })
}
