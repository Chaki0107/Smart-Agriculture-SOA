import { NavLink, Outlet } from 'react-router-dom'

const MENU = [
  { to: '/', label: '📊 Dashboard', end: true },
  { to: '/crops', label: '🌱 Crops' },
  { to: '/sensors', label: '📡 Sensors' },
  { to: '/alerts', label: '🔔 Alerts' },
  { to: '/irrigation', label: '💧 Irrigation' },
  { to: '/predictions', label: '🤖 AI Predictions' }
]

export default function Layout() {
  return (
    <div className="app">
      <aside className="sidebar">
        <h2>🌿 SmartFarm</h2>
        <nav>
          {MENU.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              end={item.end}
              className={({ isActive }) => (isActive ? 'active' : '')}
            >
              {item.label}
            </NavLink>
          ))}
        </nav>
      </aside>

      <main className="main">
        <header className="header">
          <h1>Smart Agriculture Dashboard</h1>
          <span>👨‍🌾 Farmer</span>
        </header>
        <Outlet />
      </main>
    </div>
  )
}
