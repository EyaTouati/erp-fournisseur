import { Route } from 'react-router-dom'
import RouteProtegee from '../../components/RouteProtegee'

const Vide = () => <div className="container mt-4"><h4>Sous-processus C (à venir)</h4></div>

const routesC = [
  <Route key="c" path="/c/*" element={
    <RouteProtegee roles={['ACHATS', 'DIRECTEUR', 'MAGASINIER']}><Vide /></RouteProtegee>
  } />,
]

export default routesC