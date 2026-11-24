import { Routes, Route } from 'react-router-dom'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import Crops from './pages/Crops'
import Sensors from './pages/Sensors'
import Alerts from './pages/Alerts'
import Irrigation from './pages/Irrigation'
import Predictions from './pages/Predictions'

export default function App() {
  return (
    <Routes>
      <Route element={<Layout />}>
        <Route index element={<Dashboard />} />
        <Route path="crops" element={<Crops />} />
        <Route path="sensors" element={<Sensors />} />
        <Route path="alerts" element={<Alerts />} />
        <Route path="irrigation" element={<Irrigation />} />
        <Route path="predictions" element={<Predictions />} />
      </Route>
    </Routes>
  )
}
