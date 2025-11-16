import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "./ui/Card";
import { Button } from "./ui/Button";
import { School, Calendar, ListFilter } from "lucide-react";

export default function FilterBar({ filters, onChange, onFilter, onClear }) {
  const [clients, setClients] = useState([]);

  useEffect(() => {
    api.get("/clients").then((res) => setClients(res.data));
  }, []);

  return (
    <Card className="mb-6 shadow-xl bg-gray-900 border border-gray-700">
      <CardContent>
        <div className="grid grid-cols-1 md:grid-cols-5 gap-6">

          {/* ESCOLA */}
          <div className="flex flex-col">
            <label className="font-semibold text-gray-200 mb-2 flex items-center gap-2">
              <School size={16} className="text-indigo-400" />
              Escola
            </label>
            <select
              name="clientId"
              value={filters.clientId || "ALL"}
              onChange={onChange}
              className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200 
                         focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition appearance-none"
            >
              <option value="ALL">Todas</option>
              {clients.map((c) => (
                <option key={c.clientId} value={c.clientId}>
                  {c.schoolName}
                </option>
              ))}
            </select>
          </div>

          {/* DATA INICIAL */}
          <div className="flex flex-col">
            <label className="font-semibold text-gray-200 mb-2 flex items-center gap-2">
              <Calendar size={16} className="text-indigo-400" />
              Data Inicial
            </label>

            <input
              type="date"
              name="startDate"
              value={filters.startDate || ""}
              onChange={onChange}
              className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200 
                         focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
            />
          </div>

          {/* DATA FINAL */}
          <div className="flex flex-col">
            <label className="font-semibold text-gray-200 mb-2 flex items-center gap-2">
              <Calendar size={16} className="text-indigo-400" />
              Data Final
            </label>

            <input
              type="date"
              name="endDate"
              value={filters.endDate || ""}
              onChange={onChange}
              className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200 
                         focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition"
            />
          </div>

          {/* BOTÕES */}
          <div className="md:col-span-5 flex justify-end gap-4 mt-4">
            <Button variant="primary" onClick={onFilter} className="px-6 py-2">
              Filtrar
            </Button>

            {onClear && (
              <Button variant="secondary" onClick={onClear} className="px-6 py-2">
                Limpar
              </Button>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
