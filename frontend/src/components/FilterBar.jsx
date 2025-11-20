import React from "react";
import { Card, CardContent } from "./ui/Card";
import { Button } from "./ui/Button";
import { School } from "lucide-react";

export default function FilterBar({ filters, onChange, onFilter, onClear }) {
  return (
    <Card className="mb-6 shadow-xl bg-gray-900 border border-gray-700">
      <CardContent>
        <div className="flex flex-col md:flex-row md:items-end gap-4 w-full">
          {/* INPUTS */}
          <div className="flex flex-col md:flex-row gap-4 flex-1">
            <div className="flex flex-col">
              <label className="font-semibold text-gray-200 mb-2 flex items-center gap-2">
                <School size={16} className="text-indigo-400" /> Escola
              </label>
              <input
                type="text"
                name="schoolName"
                value={filters.schoolName || ""}
                onChange={onChange}
                placeholder="Filtrar por nome da escola"
                className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200"
              />
            </div>

            <div className="flex flex-col">
              <label className="font-semibold text-gray-200 mb-2">Localização</label>
              <input
                type="text"
                name="location"
                value={filters.location || ""}
                onChange={onChange}
                placeholder="Filtrar por localização"
                className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200"
              />
            </div>

            <div className="flex flex-col">
              <label className="font-semibold text-gray-200 mb-2">Cantina</label>
              <input
                type="text"
                name="cafeteriaName"
                value={filters.cafeteriaName || ""}
                onChange={onChange}
                placeholder="Filtrar por cantina"
                className="p-3 border border-gray-700 rounded-lg bg-gray-800 text-gray-200"
              />
            </div>
          </div>

          {/* FILTRAR / LIMPAR */}
          <div className="flex gap-4 ml-auto">
            <Button variant="primary" onClick={onFilter} className="px-6 py-2">Filtrar</Button>
            {onClear && <Button variant="secondary" onClick={onClear} className="px-6 py-2">Limpar</Button>}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}