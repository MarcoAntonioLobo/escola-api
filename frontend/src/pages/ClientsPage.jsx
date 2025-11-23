import React, { useEffect, useState, useCallback } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical } from "lucide-react";
import "../index.css";

const VALID_SORT_FIELDS = [
  "clientId",
  "externalId",
  "schoolName",
  "cafeteriaName",
  "location",
  "studentCount",
];

export default function ClientsPage() {
  const [allClients, setAllClients] = useState([]);
  const [clients, setClients] = useState([]);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const [sortBy, setSortBy] = useState("studentCount");
  const [direction, setDirection] = useState("asc");

  const [schoolFilter, setSchoolFilter] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);
  const [rowsPerPage, setRowsPerPage] = useState(50);

  // ================================
  // FETCH DO BACKEND (SEM PAGINAÇÃO)
  // ================================
  const fetchClients = useCallback(async () => {
    try {
      const params = {};
      if (schoolFilter) params.schoolName = schoolFilter;

      const res = await api.get("/clients/filter", { params });
      const data = Array.isArray(res.data) ? res.data : res.data?.content || [];
      setAllClients(data);
    } catch (err) {
      console.error("Erro ao carregar clientes:", err);
      setAllClients([]);
    }
  }, [schoolFilter]);

  // ================================
  // DEBOUNCE PARA FILTRO
  // ================================
  useEffect(() => {
    const handler = setTimeout(() => {
      setPage(0);
      fetchClients();
    }, 500);
    return () => clearTimeout(handler);
  }, [schoolFilter, fetchClients]);

  // ================================
  // PAGINAÇÃO + ORDENAÇÃO LOCAL
  // ================================
  useEffect(() => {
    if (!allClients.length) {
      setClients([]);
      setTotalPages(1);
      return;
    }

    const sorted = [...allClients].sort((a, b) => {
      const fieldA = a[sortBy];
      const fieldB = b[sortBy];

      if (fieldA < fieldB) return direction === "asc" ? -1 : 1;
      if (fieldA > fieldB) return direction === "asc" ? 1 : -1;
      return 0;
    });

    const start = page * rowsPerPage;
    const end = start + rowsPerPage;

    setTotalPages(Math.ceil(sorted.length / rowsPerPage));
    setClients(sorted.slice(start, end));
  }, [allClients, sortBy, direction, page, rowsPerPage]);

  // ================================
  // TROCAR TIPO DE ORDENAÇÃO
  // ================================
  const toggleSort = (field) => {
    if (!VALID_SORT_FIELDS.includes(field)) return;
    if (sortBy === field) setDirection(direction === "asc" ? "desc" : "asc");
    else {
      setSortBy(field);
      setDirection("asc");
    }
  };

  const SortHeader = ({ field, label }) => (
    <th className="p-3 text-left cursor-pointer select-none" onClick={() => toggleSort(field)}>
      <div className="flex items-center gap-2">
        <span>{label}</span>
        <ArrowUpDown
          size={16}
          className={`transition-all duration-200 ${
            sortBy === field
              ? direction === "asc"
                ? "text-green-400 rotate-180"
                : "text-red-400 rotate-0"
              : "text-gray-400 opacity-50"
          }`}
        />
      </div>
    </th>
  );

  // ================================
  // DOWNLOAD CSV
  // ================================
  const handleDownload = () => {
    if (!allClients.length) return;
    const headers = ["ID", "ID Externo", "Escola", "Cantina", "Localização", "Total Alunos"];
    const rows = allClients.map((c) => [
      c.clientId,
      c.externalId,
      c.schoolName,
      c.cafeteriaName,
      c.location,
      c.studentCount,
    ]);
    const csvContent = "data:text/csv;charset=utf-8," + [headers, ...rows].map((e) => e.join(",")).join("\n");
    const link = document.createElement("a");
    link.href = encodeURI(csvContent);
    link.download = "clientes.csv";
    link.click();
  };

  // ================================
  // IMPRESSÃO PADRÃO
  // ================================
  const handlePrint = () => {
    if (!allClients.length) return;

    const tableHtml = `
      <table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
        <thead>
          <tr style="background-color: #444; color: #fff;">
            <th>ID</th>
            <th>ID Externo</th>
            <th>Escola</th>
            <th>Cantina</th>
            <th>Localização</th>
            <th>Total Alunos</th>
          </tr>
        </thead>
        <tbody>
          ${allClients.map(
            (c) => `
            <tr>
              <td>${c.clientId}</td>
              <td>${c.externalId}</td>
              <td>${c.schoolName}</td>
              <td>${c.cafeteriaName}</td>
              <td>${c.location}</td>
              <td>${c.studentCount}</td>
            </tr>`
          ).join("")}
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
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100 flex flex-col w-full">
      <Card className="relative w-full max-w-6xl mx-auto">
        <div className="absolute top-4 right-4 flex gap-3 z-50">
          <Download size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={handleDownload} />
          <Printer size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={handlePrint} />
          <MoreVertical size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={() => setMenuOpen(!menuOpen)} />
        </div>

        {menuOpen && (
          <CardContent className="mb-6 flex flex-col gap-3">
            <input
              type="text"
              placeholder="Digite o nome da escola"
              value={schoolFilter}
              onChange={(e) => setSchoolFilter(e.target.value)}
              className="p-3 border border-gray-600 rounded-lg bg-gray-900 text-gray-100 w-full"
            />
          </CardContent>
        )}

        <CardContent className="mb-6">
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700 rounded-lg">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <SortHeader field="clientId" label="ID" />
                  <SortHeader field="externalId" label="ID Externo" />
                  <SortHeader field="schoolName" label="Escola" />
                  <SortHeader field="cafeteriaName" label="Cantina" />
                  <SortHeader field="location" label="Localização" />
                  <SortHeader field="studentCount" label="Total Alunos" />
                </tr>
              </thead>
              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {clients.length > 0 ? clients.map((c) => (
                  <tr key={c.clientId} className="hover:bg-gray-700">
                    <td className="p-3">{c.clientId}</td>
                    <td className="p-3">{c.externalId}</td>
                    <td className="p-3">{c.schoolName}</td>
                    <td className="p-3">{c.cafeteriaName}</td>
                    <td className="p-3">{c.location}</td>
                    <td className="p-3">{c.studentCount}</td>
                  </tr>
                )) : (
                  <tr>
                    <td colSpan="6" className="text-center p-6 text-gray-400">
                      Nenhum dado disponível
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </CardContent>
      </Card>

      {/* PAGINAÇÃO + LINHAS POR PÁGINA */}
      <div className="flex justify-between items-center mt-6 mb-8 w-full max-w-6xl mx-auto">
        <div>
          <span>Linhas por página: </span>
          <select
            value={rowsPerPage}
            onChange={(e) => { setRowsPerPage(Number(e.target.value)); setPage(0); }}
            className="p-2 border border-gray-600 rounded-lg bg-gray-900 text-gray-100"
          >
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>
        </div>
        <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
      </div>
    </div>
  );
}
