import axios from 'axios'

const api = axios.create()

api.interceptors.request.use((konfiguracija) => {
  const token = localStorage.getItem('token')

  if (token) {
    konfiguracija.headers.Authorization = 'Bearer ' + token
  }

  return konfiguracija
})

export function poruka(greska) {
  if (!greska.response) {
    return 'Server nije dostupan.'
  }

  return greska.response.data?.poruka || 'Došlo je do greške.'
}

export async function preuzmi(putanja, naziv) {
  const odgovor = await api.get(putanja, { responseType: 'blob' })
  const veza = document.createElement('a')

  veza.href = URL.createObjectURL(new Blob([odgovor.data]))
  veza.download = naziv
  veza.click()

  URL.revokeObjectURL(veza.href)
}

export function datum(vrednost) {
  if (!vrednost) {
    return ''
  }

  const [godina, mesec, dan] = vrednost.split('-')
  return dan + '.' + mesec + '.' + godina + '.'
}

export function vreme(vrednost) {
  return vrednost ? vrednost.slice(0, 5) : ''
}

export function dinari(vrednost) {
  return Number(vrednost).toFixed(2) + ' RSD'
}

export default api
