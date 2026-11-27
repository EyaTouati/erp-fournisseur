import { Container } from 'react-bootstrap'
import { useAuth } from '../components/AuthContext'

export default function Accueil() {
  const { user } = useAuth()
  return (
    <Container className="mt-4">
      <h3>Bienvenue, {user.username} — rôle : {user.role}</h3>
    </Container>
  )
}