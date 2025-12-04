import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical } from "lucide-react";
import api from "../services/api";

const VALID_SORT_FIELDS = ["schoolName", "cafeteriaName", "location", "studentCount"];

export default function ClientsPage() {
  const [allClients, setAllClients] = useState([]);
  const [clients, setClients] = useState([]);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const [sortBy, setSortBy] = useState("schoolName");
  const [direction, setDirection] = useState("asc");

  const [schoolFilter, setSchoolFilter] = useState("");
  const [cafeteriaFilter, setCafeteriaFilter] = useState("");
  const [locationFilter, setLocationFilter] = useState("");

  const [menuOpen, setMenuOpen] = useState(false);
  const [rowsPerPage, setRowsPerPage] = useState(50);

  // ================================
  // FETCH CLIENTES (USANDO /filter)
  // ================================
  const fetchClients = useCallback(async () => {
    try {
      const params = {};
      if (schoolFilter && schoolFilter.trim() !== "") params.schoolName = schoolFilter.trim();
      if (cafeteriaFilter && cafeteriaFilter.trim() !== "") params.cafeteriaName = cafeteriaFilter.trim();
      if (locationFilter && locationFilter.trim() !== "") params.location = locationFilter.trim();

      const res = await api.get("/clients/filter", { params });
      setAllClients(Array.isArray(res.data) ? res.data : []);
    } catch (err) {
      console.error("Erro ao carregar clientes:", err);
      setAllClients([]);
    }
  }, [schoolFilter, cafeteriaFilter, locationFilter]);

  useEffect(() => {
    const t = setTimeout(() => {
      setPage(0);
      fetchClients();
    }, 300);
    return () => clearTimeout(t);
  }, [schoolFilter, cafeteriaFilter, locationFilter, fetchClients]);

  // ================================
  // PAGINAÇÃO + ORDENAÇÃO (CLIENT-SIDE)
  // ================================
  useEffect(() => {
    if (!allClients.length) {
      setClients([]);
      setTotalPages(1);
      return;
    }

    const sorted = [...allClients].sort((a, b) => {
      const aVal = a[sortBy] ?? "";
      const bVal = b[sortBy] ?? "";
      const aStr = typeof aVal === "string" ? aVal.toLowerCase() : aVal;
      const bStr = typeof bVal === "string" ? bVal.toLowerCase() : bVal;

      if (aStr < bStr) return direction === "asc" ? -1 : 1;
      if (aStr > bStr) return direction === "asc" ? 1 : -1;
      return 0;
    });

    const start = page * rowsPerPage;
    const end = start + rowsPerPage;

    setTotalPages(Math.max(1, Math.ceil(sorted.length / rowsPerPage)));
    setClients(sorted.slice(start, end));
  }, [allClients, sortBy, direction, page, rowsPerPage]);

  const toggleSort = (field) => {
    if (!VALID_SORT_FIELDS.includes(field)) return;
    if (sortBy === field) setDirection(direction === "asc" ? "desc" : "asc");
    else {
      setSortBy(field);
      setDirection("asc");
    }
  };

  const SortHeader = ({ field, label }) => (
    <th className="p-2 text-left cursor-pointer select-none" onClick={() => toggleSort(field)}>
      <div className="flex items-center gap-1">
        <span>{label}</span>
        <ArrowUpDown
          size={14}
          className={`transition-all duration-200 ${
            sortBy === field
              ? direction === "asc"
                ? "text-green-400 rotate-180"
                : "text-red-400 rotate-0"
              : "text-gray-300 opacity-40"
          }`}
        />
      </div>
    </th>
  );

  // ================================
  // CSV
  // ================================
  const downloadCSV = () => {
    if (!allClients.length) return;

    const headers = ["Escola", "Cantina", "Localização", "Alunos"];
    const rows = allClients.map((c) => [
      `"${(c.schoolName || "").replace(/"/g, '""')}"`,
      `"${(c.cafeteriaName || "").replace(/"/g, '""')}"`,
      `"${(c.location || "").replace(/"/g, '""')}"`,
      c.studentCount ?? 0,
    ]);

    const csvContent =
      "data:text/csv;charset=utf-8," + [headers, ...rows].map((e) => e.join(",")).join("\n");

    const link = document.createElement("a");
    link.href = encodeURI(csvContent);
    link.download = "clients.csv";
    link.click();
  };

  // ================================
  // IMPRESSÃO
  // ================================
  const handlePrint = () => {
    if (!clients.length) return;

    const tableHtml = `
      <table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
        <thead>
          <tr style="background-color: #444; color: #fff;">
            <th>Escola</th>
            <th>Cafeteria</th>
            <th>Localização</th>
            <th>Alunos</th>
          </tr>
        </thead>
        <tbody>
          ${clients
            .map(
              (c) => `
            <tr>
              <td>${c.schoolName || ""}</td>
              <td>${c.cafeteriaName || ""}</td>
              <td>${c.location || ""}</td>
              <td>${c.studentCount ?? 0}</td>
            </tr>`
            )
            .join("")}
        </tbody>
      </table>
    `;

    const WinPrint = window.open("", "", "width=900,height=650");
    WinPrint.document.write("<html><head><title>Clientes</title></head><body>");
    WinPrint.document.write("<h2 style='text-align:center;'>Clientes</h2>");
    WinPrint.document.write(tableHtml);
    WinPrint.document.write("</body></html>");
    WinPrint.document.close();
    WinPrint.focus();
    WinPrint.print();
    WinPrint.close();
  };

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100 flex justify-center">
      <Card className="relative w-full max-w-6xl">
        {/* BOTÕES SUPERIOR DIREITA */}
        <div className="absolute top-4 right-4 flex gap-4 z-50">
          <Download
            size={23}
            className="text-gray-100 cursor-pointer hover:text-green-400 transition"
            onClick={downloadCSV}
          />
          <Printer
            size={23}
            className="text-gray-100 cursor-pointer hover:text-green-400 transition"
            onClick={handlePrint}
          />
          <MoreVertical
            size={23}
            className="text-gray-100 cursor-pointer hover:text-green-400 transition"
            onClick={() => setMenuOpen(!menuOpen)}
          />
        </div>

        {/* FILTROS */}
		{menuOpen && (
		  <div className="absolute top-12 right-4 z-50 w-80 flex flex-col gap-4 p-4 bg-gray-900 rounded-lg border border-gray-700 shadow-lg">
		    <input
		      placeholder="Filtrar por Escola"
		      value={schoolFilter}
		      onChange={(e) => setSchoolFilter(e.target.value)}
		      className="!p-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-100"
		    />
		    <input
		      placeholder="Filtrar por Cantina"
		      value={cafeteriaFilter}
		      onChange={(e) => setCafeteriaFilter(e.target.value)}
		      className="!p-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-100"
		    />
		    <input
		      placeholder="Filtrar por Localização"
		      value={locationFilter}
		      onChange={(e) => setLocationFilter(e.target.value)}
		      className="!p-3 border border-gray-700 rounded-lg bg-gray-900 text-gray-100"
		    />
		  </div>
		)}


        {/* TABELA */}
        <CardContent className="mb-6 overflow-x-auto mt-16">
          <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
            <thead className="bg-gray-700 text-gray-100">
              <tr>
                <SortHeader field="schoolName" label="Escola" />
                <SortHeader field="cafeteriaName" label="Cantina" />
                <SortHeader field="location" label="Localização" />
                <SortHeader field="studentCount" label="Alunos" />
              </tr>
            </thead>

            <tbody className="bg-gray-800 divide-y divide-gray-700">
              {clients.length ? (
                clients.map((c, i) => (
                  <tr key={i} className="hover:bg-gray-700">
                    <td className="p-2">{c.schoolName || "—"}</td>
                    <td className="p-2">{c.cafeteriaName || "—"}</td>
                    <td className="p-2">{c.location || "—"}</td>
                    <td className="p-2">{c.studentCount ?? 0}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="4" className="text-center p-4 text-gray-400">
                    Nenhum cliente encontrado
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </CardContent>

        {/* PAGINAÇÃO */}
        <div className="flex justify-between items-center mt-4 mb-8 w-full">
          <div className="flex items-center gap-2">
            <span>Exibir: </span>
            <select
              value={rowsPerPage}
              onChange={(e) => {
                setRowsPerPage(Number(e.target.value));
                setPage(0);
              }}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100"
            >
              <option value={10}>10 Linhas</option>
              <option value={25}>25 Linhas</option>
              <option value={50}>50 Linhas</option>
              <option value={100}>100 Linhas</option>
            </select>
          </div>

          <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
        </div>
      </Card>
    </div>
  );
}