import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Le proxy envoie les appels /api vers le backend Spring Boot (port 8080)
export default defineConfig({
  plugins: [react()],
  server: { proxy: { '/api': 'http://localhost:8080' } }
})
