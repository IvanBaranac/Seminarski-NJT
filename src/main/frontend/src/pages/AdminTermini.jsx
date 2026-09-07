import { useEffect, useState } from 'react'
import api, { datum, dinari, poruka, preuzmi, vreme } from '../api'

const PRAZAN = {
  datum: '',
  vremePocetka: '10:00',
  vrstaIspita: 'MATEMATIKA',
  adresa: '',
  kapacitet: 40,
  cena: 2500
}

export default function AdminTermini() {
  const [termini, setTermini] = useState([])
  const [forma, setForma] = useState(PRAZAN)
  const [izmenaId, setIzmenaId] = useState(null)
  const [spisak, setSpisak] = useState(null)
  const [greska, setGreska] = useState('')
  const [uspeh, setUspeh] = useState('')

  function ucitaj() {
    api.get('/api/admin/termini')
      .then((odgovor) => setTermini(odgovor.data))
      .catch((izuzetak) => setGreska(poruka(izuzetak)))
  }

  useEffect(ucitaj, [])

  function promeni(dogadjaj) {
    setForma({ ...forma, [dogadjaj.target.name]: dogadjaj.target.value })
  }

  async function sacuvaj(dogadjaj) {
    dogadjaj.preventDefault()
    setGreska('')
    setUspeh('')

    const zahtev = {
      datum: forma.datum,
      vremePocetka: forma.vremePocetka + ':00',
      vrstaIspita: forma.vrstaIspita,
      adresa: forma.adresa,
      kapacitet: Number(forma.kapacitet),
      cena: Number(forma.cena)
    }

    try {
      if (izmenaId) {
        await api.put('/api/admin/termini/' + izmenaId, zahtev)
        setUspeh('Termin je izmenjen.')
      } else {
        await api.post('/api/admin/termini', zahtev)
        setUspeh('Termin je kreiran.')
      }

      setForma(PRAZAN)
      setIzmenaId(null)
      ucitaj()
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  function pripremiIzmenu(termin) {
    setIzmenaId(termin.id)
    setForma({
      datum: termin.datum,
      vremePocetka: vreme(termin.vremePocetka),
      vrstaIspita: termin.vrstaIspita,
      adresa: termin.adresa,
      kapacitet: termin.kapacitet,
      cena: termin.cena
    })
    window.scrollTo(0, 0)
  }

  async function obrisi(id) {
    if (!window.confirm('Obrisati termin?')) {
      return
    }

    setGreska('')
    setUspeh('')

    try {
      await api.delete('/api/admin/termini/' + id)
      setUspeh('Termin je obrisan.')
      ucitaj()
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  async function prikaziSpisak(termin) {
    setGreska('')

    try {
      const odgovor = await api.get('/api/admin/termini/' + termin.id + '/kandidati')
      setSpisak({ termin, kandidati: odgovor.data })
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  return (
    <>
      <h1>Termini</h1>

      {greska && <div className="poruka greska">{greska}</div>}
      {uspeh && <div className="poruka uspeh">{uspeh}</div>}

      <form className="kartica" onSubmit={sacuvaj}>
        <h3>{izmenaId ? 'Izmena termina br. ' + izmenaId : 'Novi termin'}</h3>

        <div className="red-polja">
          <div>
            <label>Datum</label>
            <input type="date" name="datum" value={forma.datum} onChange={promeni} required />
          </div>
          <div>
            <label>Vreme početka</label>
            <input type="time" name="vremePocetka" value={forma.vremePocetka} onChange={promeni} required />
          </div>
        </div>

        <label>Vrsta ispita</label>
        <select name="vrstaIspita" value={forma.vrstaIspita} onChange={promeni}>
          <option value="MATEMATIKA">Matematika</option>
          <option value="OPSTA_INFORMISANOST">Test opšte informisanosti</option>
        </select>

        <label>Adresa održavanja</label>
        <input name="adresa" value={forma.adresa} onChange={promeni} required />

        <div className="red-polja">
          <div>
            <label>Kapacitet</label>
            <input type="number" name="kapacitet" min={1} value={forma.kapacitet} onChange={promeni} required />
          </div>
          <div>
            <label>Cena (RSD)</label>
            <input type="number" name="cena" min={0} step="0.01" value={forma.cena} onChange={promeni} required />
          </div>
        </div>

        <div className="dugmad">
          <button type="submit">{izmenaId ? 'Sačuvaj izmene' : 'Dodaj termin'}</button>
          {izmenaId && (
            <button type="button" className="sporedno" onClick={() => { setIzmenaId(null); setForma(PRAZAN) }}>
              Odustani
            </button>
          )}
        </div>
      </form>

      <table>
        <thead>
          <tr>
            <th>Datum</th>
            <th>Vreme</th>
            <th>Vrsta</th>
            <th>Adresa</th>
            <th>Popunjeno</th>
            <th className="desno">Cena</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {termini.map((termin) => (
            <tr key={termin.id}>
              <td>{datum(termin.datum)}</td>
              <td>{vreme(termin.vremePocetka)}</td>
              <td>{termin.nazivVrste}</td>
              <td className="sitno">{termin.adresa}</td>
              <td>{termin.popunjenoMesta} / {termin.kapacitet}</td>
              <td className="desno">{dinari(termin.cena)}</td>
              <td>
                <div className="dugmad">
                  <button className="malo sporedno" onClick={() => prikaziSpisak(termin)}>Spisak</button>
                  <button className="malo sporedno" onClick={() => pripremiIzmenu(termin)}>Izmeni</button>
                  <button className="malo opasno" onClick={() => obrisi(termin.id)}>Obriši</button>
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {spisak && (
        <div className="kartica" style={{ marginTop: 18 }}>
          <div style={{ display: 'flex', justifyContent: 'space-between', gap: 12 }}>
            <h3>Spisak prijavljenih — {datum(spisak.termin.datum)}, {spisak.termin.nazivVrste}</h3>
            <div className="dugmad">
              <button
                className="malo"
                onClick={() => preuzmi('/api/admin/termini/' + spisak.termin.id + '/kandidati/excel',
                                       'spisak-' + spisak.termin.id + '.xlsx')}
              >
                Preuzmi Excel
              </button>
              <button className="malo sporedno" onClick={() => setSpisak(null)}>Zatvori</button>
            </div>
          </div>

          {spisak.kandidati.length === 0 ? (
            <p className="mutno sitno">Nema kandidata sa evidentiranom uplatom.</p>
          ) : (
            <table>
              <thead>
                <tr>
                  <th>Rb.</th>
                  <th>Kandidat</th>
                  <th>Mejl</th>
                  <th>Škola</th>
                  <th>Poziv na broj</th>
                </tr>
              </thead>
              <tbody>
                {spisak.kandidati.map((prijava, redniBroj) => (
                  <tr key={prijava.id}>
                    <td>{redniBroj + 1}</td>
                    <td>{prijava.kandidat}</td>
                    <td className="sitno">{prijava.email}</td>
                    <td className="sitno">{prijava.skola || '—'}</td>
                    <td className="sitno">{prijava.pozivNaBroj}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}
    </>
  )
}
