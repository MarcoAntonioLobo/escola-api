import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, Link } from "react-router-dom";
import App from "./App.jsx";
import ClientsPage from "./pages/ClientsPage.jsx";
import DataClientPage from "./pages/DataClientPage.jsx";
import MetricsPage from "./pages/MetricsPage.jsx";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <App />
    <nav style={{ display: "flex", gap: "1rem", padding: "1rem", background: "#eee" }}>
      <Link to="/">Clientes</Link>
      <Link to="/data">Dados Clientes</Link>
      <Link to="/metrics">Métricas</Link>
    </nav>
    <Routes>
      <Route path="/" element={<ClientsPage />} />
      <Route path="/data" element={<DataClientPage />} />
      <Route path="/metrics" element={<MetricsPage />} />
    </Routes>
  </BrowserRouter>
);
