import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";
import FilterBar from "../components/FilterBar";
import { ArrowUpDown, Printer, ArrowUp } from "lucide-react";

const VALID_SORT_FIELDS = [
  "clientId",
  "externalId",
  "schoolName",
  "cafeteriaName",
  "location",
  "studentCount",
];

export default function ClientsPage() {
  const [clients, setClients] = useState([]);
  const [filters, setFilters] = useState({ schoolName: "", location: "", cafeteriaName: "" });
  const [sortConfig, setSortConfig] = useState({ sortBy: "studentCount", sortDirection: "asc" });

  useEffect(() => {
    handleFilter();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFilters((prev) => ({ ...prev, [name]: value }));
  };

  const handleFilter = async (sortBy = sortConfig.sortBy, sortDirection = sortConfig.sortDirection) => {
    try {
      const params = {};
      if (filters.schoolName) params.schoolName = filters.schoolName;
      if (filters.location) params.location = filters.location;
      if (filters.cafeteriaName) params.cafeteriaName = filters.cafeteriaName;

      if (VALID_SORT_FIELDS.includes(sortBy)) params.sortBy = sortBy;
      params.sortDirection = sortDirection;

      const res = await api.get("/clients/filter", { params });
      setClients(res.data);
    } catch (err) {
      console.error("Erro ao filtrar clientes:", err);
    }
  };

  const handleClear = () => {
    const reset = { sortBy: "studentCount", sortDirection: "asc" };
    setFilters({ schoolName: "", location: "", cafeteriaName: "" });
    setSortConfig(reset);
    handleFilter(reset.sortBy, reset.sortDirection);
  };

  const handleSort = (column) => {
    if (!VALID_SORT_FIELDS.includes(column)) return;

    let direction = "asc";
    if (sortConfig.sortBy === column && sortConfig.sortDirection === "asc") direction = "desc";

    const newSort = { sortBy: column, sortDirection: direction };
    setSortConfig(newSort);
    handleFilter(column, direction);
  };

  const handlePrint = () => {
    const printContent = document.getElementById("clients-table");
    const WinPrint = window.open("", "", "width=900,height=650");
    WinPrint.document.write("<html><head><title>Clientes</title></head><body>");
    WinPrint.document.write(printContent.outerHTML);
    WinPrint.document.write("</body></html>");
    WinPrint.document.close();
    WinPrint.focus();
    WinPrint.print();
    WinPrint.close();
  };

  const handleDownload = () => {
    const csvRows = [
      ["ID", "ID Externo", "Escola", "Cantina", "Localização", "Total Alunos"],
      ...clients.map((c) => [
        `"${c.clientId}"`,
        `"${c.externalId}"`,
        `"${c.schoolName}"`,
        `"${c.cafeteriaName}"`,
        `"${c.location}"`,
        `"${c.studentCount}"`,
      ]),
    ];

    const csvContent = csvRows.map((e) => e.join(",")).join("\n");
    const blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.setAttribute("href", url);
    link.setAttribute("download", "clientes.csv");
    link.click();
  };

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100">
      <h2 className="text-2xl font-bold mb-4 text-gray-100">Clientes</h2>

      <FilterBar filters={filters} onChange={handleChange} onFilter={() => handleFilter()} onClear={handleClear} />

      <Card>
        <CardContent>
          <div className="relative">
            <div className="absolute top-2 right-2 flex gap-2 z-10">
              <Printer size={20} className="text-gray-200 cursor-pointer hover:text-indigo-400" onClick={handlePrint} />
              <ArrowUp size={20} className="text-gray-200 cursor-pointer hover:text-indigo-400" onClick={handleDownload} />
            </div>

            <div id="clients-table" className="overflow-x-auto">
              <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
                <thead className="bg-gray-700 text-gray-100">
                  <tr>
                    {["clientId", "externalId", "schoolName", "cafeteriaName", "location", "studentCount"].map((col) => (
                      <th key={col} className="p-2 text-left cursor-pointer select-none" onClick={() => handleSort(col)}>
                        <div className="flex items-center gap-1">
                          <span>
                            {col === "clientId" && "ID"}
                            {col === "externalId" && "ID Externo"}
                            {col === "schoolName" && "Escola"}
                            {col === "cafeteriaName" && "Cantina"}
                            {col === "location" && "Localização"}
                            {col === "studentCount" && "Total Alunos"}
                          </span>

                          {/* DESIGN IGUAL DATACLIENT */}
                          <span
                            className={`transition-all duration-200 ${
                              sortConfig.sortBy === col
                                ? sortConfig.sortDirection === "asc"
                                  ? "text-green-400 rotate-180"
                                  : "text-red-400 rotate-0"
                                : "text-gray-300 opacity-40"
                            }`}
                          >
                            <ArrowUpDown size={14} />
                          </span>
                        </div>
                      </th>
                    ))}
                  </tr>
                </thead>

                <tbody className="bg-gray-800 divide-y divide-gray-700">
                  {clients.map((c) => (
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
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
