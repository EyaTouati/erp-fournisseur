import { createContext, useContext, useEffect, useState } from 'react'
import client from '../api/client'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    (async () => {
      try {
        await client.get('/auth/csrf')          // pose le cookie XSRF-TOKEN
        const r = await client.get('/auth/me')
        setUser(r.data)
      } catch {
        setUser(null)
      } finally {
        setLoading(false)
      }
    })()
  }, [])

  const login = async (username, password) => {
    const r = await client.post('/auth/login', { username, password })
    await client.get('/auth/csrf')              // nouveau jeton après connexion
    setUser(r.data)
    return r.data
  }

  const logout = async () => {
    await client.post('/auth/logout')
    await client.get('/auth/csrf')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => useContext(AuthContext)