import { useState } from 'react'

// Textarea JSON + bouton d'envoi, affiche la réponse
export default function JsonForm({ title, initial, submitLabel = 'Envoyer', onSubmit }) {
  const [text, setText] = useState(JSON.stringify(initial, null, 2))
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')

  const submit = async (e) => {
    e.preventDefault()
    setError('')
    setResult(null)
    try {
      setResult(await onSubmit(JSON.parse(text)))
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <section className="card">
      <h3>{title}</h3>
      <form onSubmit={submit}>
        <textarea rows={10} value={text} onChange={(e) => setText(e.target.value)} />
        <button className="btn" type="submit">{submitLabel}</button>
      </form>
      {error && <p className="error">{error}</p>}
      {result && <pre>{JSON.stringify(result, null, 2)}</pre>}
    </section>
  )
}
