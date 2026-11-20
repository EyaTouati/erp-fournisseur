import { Navbar, Nav, Container, Button } from 'react-bootstrap'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from './AuthContext'
import menuItems from './menuConfig'

export default function Menu() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()

  const liens = menuItems.filter(i => i.roles.includes(user.role))

  const deconnecter = async () => {
    await logout()
    navigate('/login')
  }

  return (
    <Navbar bg="dark" variant="dark" expand="md">
      <Container>
        <Navbar.Brand as={Link} to="/">ERP Fournisseur</Navbar.Brand>
        <Nav className="me-auto">
          {liens.map(l => (
            <Nav.Link as={Link} key={l.chemin} to={l.chemin}>{l.titre}</Nav.Link>
          ))}
        </Nav>
        <span className="text-light me-3">{user.username} ({user.role})</span>
        <Button variant="outline-light" size="sm" onClick={deconnecter}>
          Déconnexion
        </Button>
      </Container>
    </Navbar>
  )
}