import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    // Avoid CORS during dev: frontend calls `/api/*`, Vite forwards to Spring Boot.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
