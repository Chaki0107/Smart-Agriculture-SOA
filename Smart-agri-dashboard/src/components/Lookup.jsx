import { useState } from 'react'
import DataTable from './DataTable'

// Champ + bouton -> appelle onSearch(valeur) et affiche le résultat
export default function Lookup({ title, placeholder, onSearch }) {
  const [value, setValue] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      setResult(await onSearch(value.trim()))
    } catch (err) {
      setResult(null)
      setError(err.message)
    }
  }

  return (
    <section className="card">
      <h3>{title}</h3>
      <form onSubmit={submit} className="row">
        <input value={value} onChange={(e) => setValue(e.target.value)} placeholder={placeholder} />
        <button className="btn" type="submit">Rechercher</button>
      </form>
      {error && <p className="error">{error}</p>}
      {Array.isArray(result) ? (
        <DataTable rows={result} />
      ) : (
        result && <pre>{JSON.stringify(result, null, 2)}</pre>
      )}
    </section>
  )
}
