import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import api, { datum, dinari, poruka, vreme } from '../api'

export default function Termini({ korisnik }) {
  const [termini, setTermini] = useState([])
  const [vrsta, setVrsta] = useState('')
  const [izabrani, setIzabrani] = useState([])
  const [greska, setGreska] = useState('')
  const [uspeh, setUspeh] = useState('')
  const navigacija = useNavigate()

  function ucitaj() {
    api.get('/api/termini')
      .then((odgovor) => setTermini(odgovor.data))
      .catch((izuzetak) => setGreska(poruka(izuzetak)))
  }

  useEffect(ucitaj, [])

  function prebaci(id) {
    if (izabrani.includes(id)) {
      setIzabrani(izabrani.filter((stavka) => stavka !== id))
    } else {
      setIzabrani([...izabrani, id])
    }
  }

  async function posalji() {
    if (!korisnik) {
      navigacija('/login')
      return
    }

    setGreska('')
    setUspeh('')

    try {
      await api.post('/api/prijave', { terminIds: izabrani })
      setUspeh('Prijava je kreirana. Uplatnica je poslata na vašu mejl adresu.')
      setIzabrani([])
      ucitaj()
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  const prikazani = vrsta ? termini.filter((termin) => termin.vrstaIspita === vrsta) : termini
  const osnovnaCena = termini
    .filter((termin) => izabrani.includes(termin.id))
    .reduce((zbir, termin) => zbir + Number(termin.cena), 0)
  const popust = izabrani.length >= 3 ? 15 : izabrani.length === 2 ? 10 : 0
  const ukupno = osnovnaCena - (osnovnaCena * popust) / 100

  return (
    <>
      <h1>Termini probnog prijemnog ispita</h1>
      <p className="mutno">Za dva termina popust je 10%, za tri i više 15%.</p>

      {greska && <div className="poruka greska">{greska}</div>}
      {uspeh && <div className="poruka uspeh">{uspeh}</div>}

      <div className="kartica">
        <label>Vrsta ispita</label>
        <select value={vrsta} onChange={(e) => setVrsta(e.target.value)} style={{ marginBottom: 0 }}>
          <option value="">Sve vrste</option>
          <option value="MATEMATIKA">Matematika</option>
          <option value="OPSTA_INFORMISANOST">Test opšte informisanosti</option>
        </select>
      </div>

      <table>
        <thead>
          <tr>
            <th></th>
            <th>Datum</th>
            <th>Vreme</th>
            <th>Vrsta ispita</th>
            <th>Adresa</th>
            <th>Slobodno</th>
            <th className="desno">Cena</th>
          </tr>
        </thead>
        <tbody>
          {prikazani.length === 0 && (
            <tr><td colSpan={7} className="mutno">Nema objavljenih termina.</td></tr>
          )}

          {prikazani.map((termin) => (
            <tr key={termin.id}>
              <td>
                <input
                  type="checkbox"
                  style={{ width: 16, margin: 0 }}
                  checked={izabrani.includes(termin.id)}
                  disabled={termin.slobodnihMesta <= 0}
                  onChange={() => prebaci(termin.id)}
                />
              </td>
              <td>{datum(termin.datum)}</td>
              <td>{vreme(termin.vremePocetka)}</td>
              <td>{termin.nazivVrste}</td>
              <td className="sitno">{termin.adresa}</td>
              <td>{termin.slobodnihMesta} / {termin.kapacitet}</td>
              <td className="desno">{dinari(termin.cena)}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {izabrani.length > 0 && (
        <div className="kartica" style={{ marginTop: 16 }}>
          <h3>Pregled prijave</h3>
          <p className="sitno">
            Izabrano termina: <b>{izabrani.length}</b><br />
            Osnovna cena: {dinari(osnovnaCena)}<br />
            Popust: {popust}%<br />
            <b>Za uplatu: {dinari(ukupno)}</b>
          </p>
          <button onClick={posalji}>
            {korisnik ? 'Potvrdi prijavu' : 'Prijavi se na sistem da nastaviš'}
          </button>
        </div>
      )}
    </>
  )
}
