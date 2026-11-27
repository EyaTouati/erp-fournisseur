import { Route } from 'react-router-dom'
import RouteProtegee from '../../components/RouteProtegee'

const Vide = () => <div className="container mt-4"><h4>Sous-processus B (à venir)</h4></div>

const routesB = [
  <Route key="b" path="/b/*" element={
    <RouteProtegee roles={['ACHATS', 'MAGASINIER']}><Vide /></RouteProtegee>
  } />,
]

export default routesB