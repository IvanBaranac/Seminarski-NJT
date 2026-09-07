import { useEffect, useState } from 'react'
import api, { datum, dinari, poruka, preuzmi } from '../api'

export default function AdminPrijave() {
  const [prijave, setPrijave] = useState([])
  const [filter, setFilter] = useState('')
  const [greska, setGreska] = useState('')
  const [uspeh, setUspeh] = useState('')

  function ucitaj() {
    api.get('/api/admin/prijave')
      .then((odgovor) => setPrijave(odgovor.data))
      .catch((izuzetak) => setGreska(poruka(izuzetak)))
  }

  useEffect(ucitaj, [])

  async function evidentiraj(prijava) {
    if (!window.confirm('Evidentirati uplatu od ' + dinari(prijava.ukupnaCena)
                        + ' za kandidata ' + prijava.kandidat + '?')) {
      return
    }

    setGreska('')
    setUspeh('')

    try {
      await api.post('/api/admin/prijave/' + prijava.id + '/uplata', { nacinPlacanja: 'UPLATNICA' })
      setUspeh('Uplata je evidentirana, kandidat je obavešten mejlom.')
      ucitaj()
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  function bojaStatusa(status) {
    if (status === 'PRIJAVLJEN') return 'ok'
    if (status === 'OTKAZANA') return 'stop'
    return 'ceka'
  }

  const prikazane = filter ? prijave.filter((prijava) => prijava.status === filter) : prijave

  return (
    <>
      <h1>Prijave i uplate</h1>
      <p className="mutno">
        Kada evidentirate uplatu, kandidat dobija status „prijavljen” i mejl obaveštenje.
      </p>

      {greska && <div className="poruka greska">{greska}</div>}
      {uspeh && <div className="poruka uspeh">{uspeh}</div>}

      <div className="kartica">
        <label>Status</label>
        <select value={filter} onChange={(e) => setFilter(e.target.value)} style={{ marginBottom: 0 }}>
          <option value="">Sve prijave ({prijave.length})</option>
          <option value="CEKA_UPLATU">Čekaju uplatu</option>
          <option value="PRIJAVLJEN">Prijavljeni</option>
          <option value="OTKAZANA">Otkazane</option>
        </select>
      </div>

      <table>
        <thead>
          <tr>
            <th>Br.</th>
            <th>Kandidat</th>
            <th>Termini</th>
            <th>Datum</th>
            <th className="desno">Iznos</th>
            <th>Poziv na broj</th>
            <th>Status</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {prikazane.length === 0 && (
            <tr><td colSpan={8} className="mutno">Nema prijava.</td></tr>
          )}

          {prikazane.map((prijava) => (
            <tr key={prijava.id}>
              <td>{prijava.id}</td>
              <td>
                {prijava.kandidat}
                <div className="sitno mutno">{prijava.email}</div>
              </td>
              <td className="sitno">
                {prijava.stavke.map((stavka) => <div key={stavka.terminId}>{stavka.opis}</div>)}
              </td>
              <td className="sitno">{datum(prijava.datumPrijave)}</td>
              <td className="desno">{dinari(prijava.ukupnaCena)}</td>
              <td className="sitno">{prijava.pozivNaBroj}</td>
              <td>
                <span className={'oznaka ' + bojaStatusa(prijava.status)}>{prijava.nazivStatusa}</span>
                {prijava.nacinPlacanja && <div className="sitno mutno">{prijava.nacinPlacanja}</div>}
              </td>
              <td>
                <div className="dugmad">
                  {prijava.status === 'CEKA_UPLATU' && (
                    <button className="malo" onClick={() => evidentiraj(prijava)}>Evidentiraj uplatu</button>
                  )}
                  <button
                    className="malo sporedno"
                    onClick={() => preuzmi('/api/prijave/' + prijava.id + '/uplatnica',
                                           'uplatnica-' + prijava.id + '.pdf')}
                  >
                    PDF
                  </button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}
