import axios from "axios";

const api = axios.create({
  baseURL: "/api", // vai usar o proxy do Vite
});

export default api;