import { defineConfig } from 'vite'
import { svelte } from '@sveltejs/vite-plugin-svelte'

export default defineConfig({
  plugins: [svelte()],

  build: {
    outDir: '../backend/src/main/resources/public',
    emptyOutDir: true
  },

  server: {
    proxy: {
      '/oauth': 'http://localhost:8080',
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})