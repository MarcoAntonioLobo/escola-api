import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";

export default function DataClientPage() {
  const [data, setData] = useState([]);

  useEffect(() => {
    api.get("/client-data").then(res => setData(res.data));
  }, []);

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100">
      <h2 className="text-2xl font-bold mb-4 text-gray-100">Dados dos Clientes</h2>
      <Card>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <th className="p-2 text-left">ID Registro</th>
                  <th className="p-2 text-left">ID Cliente</th>
                  <th className="p-2 text-left">Mês</th>
                  <th className="p-2 text-left">Receita</th>
                  <th className="p-2 text-left">Despesas</th>
                  <th className="p-2 text-left">Pedidos</th>
                  <th className="p-2 text-left">Alunos Registrados</th>
                  <th className="p-2 text-left">Notas</th>
                </tr>
              </thead>
              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {data.map(d => (
                  <tr key={d.dataId} className="hover:bg-gray-700">
                    <td className="p-2">{d.dataId}</td>
                    <td className="p-2">{d.clientId}</td>
                    <td className="p-2">{d.monthDate}</td>
                    <td className="p-2">{d.revenue}</td>
                    <td className="p-2">{d.expenses}</td>
                    <td className="p-2">{d.orderCount}</td>
                    <td className="p-2">{d.registeredStudents}</td>
                    <td className="p-2">{d.notes}</td>
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
