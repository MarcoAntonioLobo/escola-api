import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";

export default function ClientsPage() {
  const [clients, setClients] = useState([]);

  useEffect(() => {
    api.get("/clients").then(res => setClients(res.data));
  }, []);

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100">
      <h2 className="text-2xl font-bold mb-4 text-gray-100">Clientes</h2>
      <Card>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <th className="p-2 text-left">ID</th>
                  <th className="p-2 text-left">ID Externo</th>
                  <th className="p-2 text-left">Escola</th>
                  <th className="p-2 text-left">Cantina</th>
                  <th className="p-2 text-left">Localização</th>
                  <th className="p-2 text-left">Total Alunos</th>
                </tr>
              </thead>
              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {clients.map(c => (
                  <tr key={c.clientId} className="hover:bg-gray-700">
                    <td className="p-2">{c.clientId}</td>
                    <td className="p-2">{c.externalId}</td>
                    <td className="p-2">{c.schoolName}</td>
                    <td className="p-2">{c.cafeteriaName}</td>
                    <td className="p-2">{c.location}</td>
                    <td className="p-2">{c.studentCount}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
