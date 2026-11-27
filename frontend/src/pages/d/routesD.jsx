import { Route } from 'react-router-dom'
import RouteProtegee from '../../components/RouteProtegee'

const Vide = () => <div className="container mt-4"><h4>Sous-processus D (à venir)</h4></div>

const routesD = [
  <Route key="d" path="/d/*" element={
    <RouteProtegee roles={['COMPTABLE']}><Vide /></RouteProtegee>
  } />,
]

export default routesD