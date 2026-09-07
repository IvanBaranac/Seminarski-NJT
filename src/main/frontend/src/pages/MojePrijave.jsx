import { useEffect, useState } from 'react'
import api, { datum, dinari, poruka, preuzmi } from '../api'

export default function MojePrijave() {
  const [prijave, setPrijave] = useState([])
  const [greska, setGreska] = useState('')
  const [uspeh, setUspeh] = useState('')
  const [obavestenje, setObavestenje] = useState(null)

  function ucitaj() {
    api.get('/api/prijave/moje')
      .then((odgovor) => setPrijave(odgovor.data))
      .catch((izuzetak) => setGreska(poruka(izuzetak)))
  }

  useEffect(ucitaj, [])

  async function otkazi(id) {
    if (!window.confirm('Otkazati prijavu?')) {
      return
    }

    setGreska('')
    setUspeh('')

    try {
      await api.delete('/api/prijave/' + id)
      setUspeh('Prijava je otkazana, mesta su oslobođena.')
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

  return (
    <>
      <h1>Moje prijave</h1>

      {greska && <div className="poruka greska">{greska}</div>}
      {uspeh && <div className="poruka uspeh">{uspeh}</div>}

      {prijave.length === 0 && <p className="mutno">Još uvek nemate nijednu prijavu.</p>}

      {prijave.map((prijava) => (
        <div className="kartica" key={prijava.id}>
          <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
            <div>
              <h3>
                Prijava br. {prijava.id}{' '}
                <span className={'oznaka ' + bojaStatusa(prijava.status)}>{prijava.nazivStatusa}</span>
              </h3>
              <p className="sitno mutno">Kreirana {datum(prijava.datumPrijave)}</p>
            </div>
            <div className="desno">
              <b>{dinari(prijava.ukupnaCena)}</b>
              {prijava.popustProcenat > 0 && (
                <div className="sitno mutno">popust {prijava.popustProcenat}%</div>
              )}
            </div>
          </div>

          <ul className="sitno">
            {prijava.stavke.map((stavka) => (
              <li key={stavka.terminId}>{stavka.opis} — {dinari(stavka.cena)}</li>
            ))}
          </ul>

          {prijava.status === 'CEKA_UPLATU' && (
            <div className="poruka info sitno">
              Poziv na broj (model 97): <b>{prijava.pozivNaBroj}</b> · rok do {datum(prijava.rokZaUplatu)}
            </div>
          )}

          <div className="dugmad">
            <button
              className="malo sporedno"
              onClick={() => preuzmi('/api/prijave/' + prijava.id + '/uplatnica',
                                     'uplatnica-' + prijava.id + '.pdf')}
            >
              Preuzmi uplatnicu (PDF)
            </button>

            {prijava.status === 'CEKA_UPLATU' && (
              <>
                <button className="malo" onClick={() => setObavestenje(prijava.id)}>Plati preko PayPal-a</button>
                <button className="malo opasno" onClick={() => otkazi(prijava.id)}>Otkaži prijavu</button>
              </>
            )}
          </div>

          {obavestenje === prijava.id && (
            <div className="poruka info sitno" style={{ marginTop: 10 }}>
              Plaćanje preko PayPal-a nije implementirano. Uplatu izvršite po uplatnici,
              a studentska služba će je evidentirati.
            </div>
          )}
        </div>
      ))}
    </>
  )
}
