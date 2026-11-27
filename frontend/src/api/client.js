import axios from 'axios'

const client = axios.create({ baseURL: '/api', withCredentials: true })
// axios renvoie tout seul le cookie XSRF-TOKEN dans l'en-tête X-XSRF-TOKEN (même origine via le proxy)

client.interceptors.response.use(r => r, err => {
  if (err.response?.status === 401 && !location.pathname.startsWith('/login')) {
    location.href = '/login'
  }
  return Promise.reject(err)
})

export default client