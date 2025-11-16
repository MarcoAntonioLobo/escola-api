import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path"; // <- importar path

export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000,
    proxy: {
      "/api": "http://localhost:8080"
    }
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src") // <- configurar alias @ para src
    }
  },
  build: {
    outDir: "dist"
  }
});
