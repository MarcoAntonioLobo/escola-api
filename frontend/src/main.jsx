import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, NavLink } from "react-router-dom";
import App from "./App.jsx";
import ClientsPage from "./pages/ClientsPage.jsx";
import DataClientPage from "./pages/DataClientPage.jsx";
import MetricsPage from "./pages/MetricsPage.jsx";
import "./index.css";

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <App />
    {/* Menu de navegação */}
    <nav style={{ display: "flex", gap: "1rem", padding: "1rem", background: "#eee" }}>
      <NavLink
        to="/"
        style={({ isActive }) => ({
          color: isActive ? "#fff" : "#888",
        })}
      >
        Clientes
      </NavLink>

      <NavLink
        to="/data"
        style={({ isActive }) => ({
          color: isActive ? "#fff" : "#888",
        })}
      >
        Dados Clientes
      </NavLink>

      <NavLink
        to="/metrics"
        style={({ isActive }) => ({
          color: isActive ? "#fff" : "#888",
        })}
      >
        Métricas
      </NavLink>
    </nav>

    {/* Rotas das páginas */}
    <Routes>
      <Route path="/" element={<ClientsPage />} />
      <Route path="/data" element={<DataClientPage />} />
      <Route path="/metrics" element={<MetricsPage />} />
    </Routes>
  </BrowserRouter>
);
