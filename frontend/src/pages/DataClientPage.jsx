import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";
import { ArrowUpDown } from "lucide-react";

export default function DataClientPage() {
  const [data, setData] = useState([]);
  const [sortBy, setSortBy] = useState("dataId");
  const [direction, setDirection] = useState("asc");

  const fetchData = async () => {
    try {
      const res = await api.get("/client-data/filter", {
        params: {
          sortBy,
          direction,
        },
      });

      const fixed = res.data.map((d) => ({
        ...d,
        registeredStudents: d.registeredStudents ?? 0,
      }));

      setData(fixed);
    } catch (err) {
      console.error("Erro ao buscar:", err);
    }
  };

  useEffect(() => {
    fetchData();
  }, [sortBy, direction]);

  const toggleSort = (field) => {
    if (sortBy === field) {
      setDirection(direction === "asc" ? "desc" : "asc");
    } else {
      setSortBy(field);
      setDirection("asc");
    }
  };

  const SortHeader = ({ field, label }) => (
    <th
      className="p-2 text-left cursor-pointer select-none"
      onClick={() => toggleSort(field)}
    >
      <div className="flex items-center gap-1">
        <span>{label}</span>
        <span
          className={`transition-all duration-200 ${
            sortBy === field
              ? direction === "asc"
                ? "text-green-400 rotate-180"
                : "text-red-400 rotate-0"
              : "text-gray-300 opacity-40"
          }`}
        >
          <ArrowUpDown size={14} />
        </span>
      </div>
    </th>
  );

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100">
      <h2 className="text-2xl font-bold mb-4">Dados dos Clientes</h2>

      <Card>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <SortHeader field="dataId" label="ID Registro" />
                  <SortHeader field="clientId" label="ID Cliente" />
                  <SortHeader field="monthDate" label="Mês" />
                  <SortHeader field="revenue" label="Receita" />
                  <SortHeader field="expenses" label="Despesas" />
                  <SortHeader field="orderCount" label="Pedidos" />
                  <SortHeader field="registeredStudents" label="Alunos Registrados" />
                  <SortHeader field="notes" label="Notas" />
                </tr>
              </thead>

              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {data.map((d) => (
                  <tr key={d.dataId} className="hover:bg-gray-700">
                    <td className="p-2">{d.dataId}</td>
                    <td className="p-2">{d.clientId}</td>
                    <td className="p-2">
                      {new Date(d.monthDate).toLocaleDateString("pt-BR", {
                        month: "long",
                        year: "numeric",
                      })}
                    </td>
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