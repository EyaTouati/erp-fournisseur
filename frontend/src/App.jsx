import { BrowserRouter, Routes, Route } from 'react-router-dom'
import { AuthProvider, useAuth } from './components/AuthContext'
import RouteProtegee from './components/RouteProtegee'
import Menu from './components/Menu'
import Login from './pages/Login'
import Accueil from './pages/Accueil'
import routesA from './pages/a/routesA'
import routesB from './pages/b/routesB'
import routesC from './pages/c/routesC'
import routesD from './pages/d/routesD'

function MiseEnPage({ children }) {
  const { user } = useAuth()
  return (
    <>
      {user && <Menu />}
      {children}
    </>
  )
}

export default function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <MiseEnPage>
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/" element={<RouteProtegee><Accueil /></RouteProtegee>} />
            {routesA}
            {routesB}
            {routesC}
            {routesD}
          </Routes>
        </MiseEnPage>
      </AuthProvider>
    </BrowserRouter>
  )
}