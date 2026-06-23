import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

import path from "path";

export default defineConfig({
  plugins: [react()],
  css: {
    modules: {
      generateScopedName: "[name]_[local]__[hash:base64:5]",
    },
    devSourcemap: true,
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    }
  },
  server: {
    host: '0.0.0.0',
    port: 5173, // hoặc port bạn muốn
  }
})
