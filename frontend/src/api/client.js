import axios from 'axios'

// withCredentials : le cookie de session est envoyé à chaque appel
const api = axios.create({ withCredentials: true })

export default api