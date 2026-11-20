import { useState } from 'react'
import { Container, Form, Button, Alert, Card } from 'react-bootstrap'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../components/AuthContext'

export default function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [erreur, setErreur] = useState(null)
  const { login } = useAuth()
  const navigate = useNavigate()

  const soumettre = async (e) => {
    e.preventDefault()
    setErreur(null)
    try {
      await login(username, password)
      navigate('/')
    } catch {
      setErreur('Identifiant ou mot de passe incorrect')
    }
  }

  return (
    <Container style={{ maxWidth: 400 }} className="mt-5">
      <Card body>
        <h4 className="mb-3">Connexion</h4>
        {erreur && <Alert variant="danger">{erreur}</Alert>}
        <Form onSubmit={soumettre}>
          <Form.Group className="mb-3">
            <Form.Label>Identifiant</Form.Label>
            <Form.Control value={username} onChange={e => setUsername(e.target.value)} required />
          </Form.Group>
          <Form.Group className="mb-3">
            <Form.Label>Mot de passe</Form.Label>
            <Form.Control type="password" value={password} onChange={e => setPassword(e.target.value)} required />
          </Form.Group>
          <Button type="submit" className="w-100">Se connecter</Button>
        </Form>
      </Card>
    </Container>
  )
}