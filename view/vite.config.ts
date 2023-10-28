import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from "path";

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  server: {
    proxy: {
      '^/images': {
        target: 'http://localhost:8080' // Spring boot server address
      }
    }
  },
  build: {
    outDir: 'target/dist',
    assetsDir: 'static'
  }
})