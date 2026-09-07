import { useState } from 'react'
import { Link } from 'react-router-dom'
import api, { poruka } from '../api'

export default function Login({ naPrijavu }) {
  const [email, setEmail] = useState('')
  const [lozinka, setLozinka] = useState('')
  const [greska, setGreska] = useState('')

  async function posalji(dogadjaj) {
    dogadjaj.preventDefault()
    setGreska('')

    try {
      const odgovor = await api.post('/api/auth/login', { email, lozinka })
      naPrijavu(odgovor.data)
    } catch (izuzetak) {
      setGreska(poruka(izuzetak))
    }
  }

  return (
    <div style={{ maxWidth: 380, margin: '30px auto' }}>
      <h1>Prijava</h1>

      {greska && <div className="poruka greska">{greska}</div>}

      <form className="kartica" onSubmit={posalji}>
        <label>Mejl adresa</label>
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />

        <label>Lozinka</label>
        <input type="password" value={lozinka} onChange={(e) => setLozinka(e.target.value)} required />

        <button type="submit" style={{ width: '100%' }}>Prijavi se</button>
      </form>

      <p className="sitno mutno">
        Nemate nalog? <Link to="/registracija">Registrujte se</Link>
      </p>

      <div className="kartica sitno">
        <b>Nalozi za testiranje</b>
        <p className="mutno" style={{ margin: '6px 0 0' }}>
          Administrator: admin@fon.bg.ac.rs / admin123<br />
          Kandidat: pera@primer.rs / kandidat123
        </p>
      </div>
    </div>
  )
}
