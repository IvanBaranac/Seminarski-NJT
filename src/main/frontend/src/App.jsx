import { useEffect, useState } from 'react'
import { Link, Navigate, Route, Routes, useNavigate } from 'react-router-dom'
import api from './api'

import Login from './pages/Login'
import Registracija from './pages/Registracija'
import Termini from './pages/Termini'
import MojePrijave from './pages/MojePrijave'
import AdminTermini from './pages/AdminTermini'
import AdminPrijave from './pages/AdminPrijave'

export default function App() {
  const [korisnik, setKorisnik] = useState(null)
  const [ucitavanje, setUcitavanje] = useState(true)
  const navigacija = useNavigate()

  useEffect(() => {
    if (!localStorage.getItem('token')) {
      setUcitavanje(false)
      return
    }

    api.get('/api/auth/ja')
      .then((odgovor) => setKorisnik(odgovor.data))
      .catch(() => localStorage.removeItem('token'))
      .finally(() => setUcitavanje(false))
  }, [])

  function prijaviSe(odgovor) {
    localStorage.setItem('token', odgovor.token)
    setKorisnik(odgovor.korisnik)
    navigacija(odgovor.korisnik.uloga === 'ADMIN' ? '/admin/prijave' : '/moje-prijave')
  }

  function odjaviSe() {
    localStorage.removeItem('token')
    setKorisnik(null)
    navigacija('/')
  }

  if (ucitavanje) {
    return <div className="omotac">Učitavanje…</div>
  }

  const jeAdmin = korisnik && korisnik.uloga === 'ADMIN'

  return (
    <>
      <nav className="nav">
        <div className="nav-sadrzaj">
          <span className="logo">Probni prijemni — FON</span>

          <Link to="/">Termini</Link>
          {korisnik && !jeAdmin && <Link to="/moje-prijave">Moje prijave</Link>}
          {jeAdmin && <Link to="/admin/termini">Termini (admin)</Link>}
          {jeAdmin && <Link to="/admin/prijave">Prijave i uplate</Link>}

          <span className="kraj">
            {korisnik ? (
              <>
                <span className="sitno">{korisnik.ime} {korisnik.prezime}</span>
                <button className="malo sporedno" onClick={odjaviSe}>Odjava</button>
              </>
            ) : (
              <>
                <Link to="/login">Prijava</Link>
                <Link to="/registracija">Registracija</Link>
              </>
            )}
          </span>
        </div>
      </nav>

      <div className="omotac">
        <Routes>
          <Route path="/" element={<Termini korisnik={korisnik} />} />
          <Route path="/login" element={<Login naPrijavu={prijaviSe} />} />
          <Route path="/registracija" element={<Registracija naPrijavu={prijaviSe} />} />
          <Route path="/moje-prijave" element={korisnik ? <MojePrijave /> : <Navigate to="/login" />} />
          <Route path="/admin/termini" element={jeAdmin ? <AdminTermini /> : <Navigate to="/" />} />
          <Route path="/admin/prijave" element={jeAdmin ? <AdminPrijave /> : <Navigate to="/" />} />
        </Routes>
      </div>
    </>
  )
}
