import { createContext, useContext, useEffect, useState } from 'react'
import api from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

useEffect(() => {
  api.get('/api/auth/me')
    .then(r => setUser(r.data && r.data.username ? r.data : null))
    .catch(() => setUser(null))
    .finally(() => setLoading(false))
}, [])

 const login = async (username, password) => {
  const r = await api.post('/api/auth/login', { username, password })
  if (!r.data || !r.data.username) throw new Error('Connexion invalide')
  setUser(r.data)
  return r.data
}

  const logout = async () => {
    await api.post('/api/auth/logout')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)