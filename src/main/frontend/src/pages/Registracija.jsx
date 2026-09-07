import { useState } from 'react'
import api, { poruka } from '../api'

const PRAZAN = { ime: '', prezime: '', email: '', lozinka: '', telefon: '', srednjaSkola: '' }

export default function Registracija({ naPrijavu }) {
  const [podaci, setPodaci] = useState(PRAZAN)
  const [greska, setGreska] = useState('')

  function promeni(dogadjaj) {
    setPodaci({ ...podaci, [dogadjaj.target.name]: dogadjaj.target.value })
  }

  async function posalji(dogadjaj) {
    dogadjaj.preventDefault()
    setGreska('')

    try {
      const odgovor = await api.post('/api/auth/registracija', podaci)
      naPrijavu(odgovor.data)
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  return (
    <div style={{ maxWidth: 480, margin: '30px auto' }}>
      <h1>Registracija kandidata</h1>

      {greska && <div className="poruka greska">{greska}</div>}

      <form className="kartica" onSubmit={posalji}>
        <div className="red-polja">
          <div>
            <label>Ime</label>
            <input name="ime" value={podaci.ime} onChange={promeni} required />
          </div>
          <div>
            <label>Prezime</label>
            <input name="prezime" value={podaci.prezime} onChange={promeni} required />
          </div>
        </div>

        <label>Mejl adresa</label>
        <input type="email" name="email" value={podaci.email} onChange={promeni} required />

        <label>Lozinka (najmanje 6 karaktera)</label>
        <input type="password" name="lozinka" value={podaci.lozinka} onChange={promeni} minLength={6} required />

        <label>Telefon</label>
        <input name="telefon" value={podaci.telefon} onChange={promeni} />

        <label>Srednja škola</label>
        <input name="srednjaSkola" value={podaci.srednjaSkola} onChange={promeni} />

        <button type="submit" style={{ width: '100%' }}>Otvori nalog</button>
      </form>
    </div>
  )
}
