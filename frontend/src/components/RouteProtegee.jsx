import { Navigate } from 'react-router-dom'
import { useAuth } from './AuthContext'

export default function RouteProtegee({ roles, children }) {
  const { user, loading } = useAuth()
  if (loading) return <p className="p-3">Chargement...</p>
  if (!user) return <Navigate to="/login" replace />
  if (roles && !roles.includes(user.role)) return <Navigate to="/" replace />
  return children
}
