import { Route } from 'react-router-dom'
import RouteProtegee from '../../components/RouteProtegee'

const Vide = () => <div className="container mt-4"><h4>Sous-processus A (à venir)</h4></div>

const routesA = [
  <Route key="a" path="/a/*" element={
    <RouteProtegee roles={['ACHATS', 'DIRECTEUR']}><Vide /></RouteProtegee>
  } />,
]

export default routesA