export default function DataTable({ rows, renderActions }) {
  if (!rows || rows.length === 0) return <p>Aucune donnée.</p>

  const columns = Object.keys(rows[0])
  const show = (v) => (v !== null && typeof v === 'object' ? JSON.stringify(v) : String(v ?? ''))

  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            {columns.map((c) => <th key={c}>{c}</th>)}
            {renderActions && <th>Actions</th>}
          </tr>
        </thead>
        <tbody>
          {rows.map((row, i) => (
            <tr key={row.id ?? i}>
              {columns.map((c) => <td key={c}>{show(row[c])}</td>)}
              {renderActions && <td>{renderActions(row)}</td>}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  )
}
