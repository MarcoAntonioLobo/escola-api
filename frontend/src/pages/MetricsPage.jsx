import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical } from "lucide-react";
import api from "../services/api";

const VALID_SORT_FIELDS = [
  "schoolName",
  "totalStudentsRegistered",
  "studentsRegisteredVsTotal",
  "averageTicket",
  "profitPerStudent",
  "totalOrdersMonth",
  "month",
];

export default function MetricsPage() {
  const [allMetrics, setAllMetrics] = useState([]);
  const [metrics, setMetrics] = useState([]);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const [sortBy, setSortBy] = useState("schoolName");
  const [direction, setDirection] = useState("asc");

  const [schoolFilter, setSchoolFilter] = useState("");
  const [monthYearFilter, setMonthYearFilter] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);

  const [rowsPerPage, setRowsPerPage] = useState(50);

  // ================================
  // FETCH DO BACKEND (SEM PAGINAÇÃO)
  // ================================
  const fetchMetrics = useCallback(async () => {
    try {
      const params = {};
      if (schoolFilter) params.schoolName = schoolFilter;
      if (monthYearFilter) {
        const [month, year] = monthYearFilter.split("/").map(Number);
        params.month = month;
        params.year = year;
      }

      const res = await api.get("/metrics", { params });
      const data = Array.isArray(res.data) ? res.data : res.data?.content || [];
      setAllMetrics(data);
    } catch (err) {
      console.error("Erro ao carregar métricas:", err);
      setAllMetrics([]);
    }
  }, [schoolFilter, monthYearFilter]);

  // ================================
  // DEBOUNCE PARA FILTROS
  // ================================
  useEffect(() => {
    const t = setTimeout(() => {
      setPage(0);
      fetchMetrics();
    }, 500);
    return () => clearTimeout(t);
  }, [schoolFilter, monthYearFilter, fetchMetrics]);

  // ================================
  // PAGINAÇÃO E ORDENAÇÃO LOCAL
  // ================================
  useEffect(() => {
    if (!allMetrics.length) {
      setMetrics([]);
      setTotalPages(1);
      return;
    }

    const sorted = [...allMetrics].sort((a, b) => {
      const aVal = a[sortBy] ?? "";
      const bVal = b[sortBy] ?? "";
      if (aVal < bVal) return direction === "asc" ? -1 : 1;
      if (aVal > bVal) return direction === "asc" ? 1 : -1;
      return 0;
    });

    const start = page * rowsPerPage;
    const end = start + rowsPerPage;

    setTotalPages(Math.ceil(sorted.length / rowsPerPage));
    setMetrics(sorted.slice(start, end));
  }, [allMetrics, sortBy, direction, page, rowsPerPage]);

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

  const formatMonth = (monthStr) => {
    if (!monthStr) return "-";
    const monthNumber = new Date(`${monthStr}-01-2025`).getMonth();
    return new Intl.DateTimeFormat("pt-BR", { month: "short" }).format(new Date(2025, monthNumber, 1));
  };

  // ================================
  // DOWNLOAD CSV
  // ================================
  const downloadCSV = () => {
    if (!allMetrics.length) return;
    const headers = ["Cliente", "Alunos Registrados", "% Registrados", "Ticket Médio", "Lucro/Aluno", "Pedidos", "Mês"];
    const rows = allMetrics.map((m) => [
      m.schoolName || "",
      m.totalStudentsRegistered || 0,
      Number(m.studentsRegisteredVsTotal || 0).toFixed(2),
      Number(m.averageTicket || 0).toFixed(2),
      Number(m.profitPerStudent || 0).toFixed(2),
      m.totalOrdersMonth || 0,
      formatMonth(m.month),
    ]);
    const csvContent = "data:text/csv;charset=utf-8," + [headers, ...rows].map((e) => e.join(",")).join("\n");
    const link = document.createElement("a");
    link.href = encodeURI(csvContent);
    link.download = "metrics.csv";
    link.click();
  };

  // ================================
  // IMPRESSÃO
  // ================================
  const handlePrint = () => {
    if (!metrics.length) return;

    const tableHtml = `
      <table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
        <thead>
          <tr style="background-color: #444; color: #fff;">
            <th>Cliente</th>
            <th>Alunos Registrados</th>
            <th>% Registrados</th>
            <th>Ticket Médio</th>
            <th>Lucro/Aluno</th>
            <th>Pedidos</th>
            <th>Mês</th>
          </tr>
        </thead>
        <tbody>
          ${metrics.map(
            (m) => `
            <tr>
              <td>${m.schoolName || ""}</td>
              <td>${m.totalStudentsRegistered || 0}</td>
              <td>${Number(m.studentsRegisteredVsTotal || 0).toFixed(2)}%</td>
              <td>R$ ${Number(m.averageTicket || 0).toFixed(2)}</td>
              <td>R$ ${Number(m.profitPerStudent || 0).toFixed(2)}</td>
              <td>${m.totalOrdersMonth || 0}</td>
              <td>${formatMonth(m.month)}</td>
            </tr>`
          ).join("")}
        </tbody>
      </table>
    `;

    const WinPrint = window.open("", "", "width=900,height=650");
    WinPrint.document.write("<html><head><title>Métricas</title></head><body>");
    WinPrint.document.write("<h2 style='text-align:center;'>Métricas</h2>");
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
        {/* BOTÕES DE CSV, PRINT E MENU */}
        <div className="absolute top-4 right-4 flex gap-2 z-50">
          <Download size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={downloadCSV} />
          <Printer size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={handlePrint} />
          <MoreVertical size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={() => setMenuOpen(!menuOpen)} />
        </div>

        {/* FILTROS */}
        {menuOpen && (
			<CardContent className="mb-6 flex flex-col gap-6 px-6 py-4 space-y-4 bg-gray-900 rounded-lg border border-gray-700">
            <input
              placeholder="Cliente"
              value={schoolFilter}
              onChange={(e) => setSchoolFilter(e.target.value)}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100 w-full"
            />
            <input
              placeholder="Mês/Ano (MM/YYYY)"
              value={monthYearFilter}
              onChange={(e) => setMonthYearFilter(e.target.value)}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100 w-full"
            />
          </CardContent>
        )}

        {/* TABELA */}
        <CardContent className="mb-6">
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <SortHeader field="schoolName" label="Cliente" />
                  <SortHeader field="totalStudentsRegistered" label="Alunos Registrados" />
                  <SortHeader field="studentsRegisteredVsTotal" label="% Registrados" />
                  <SortHeader field="averageTicket" label="Ticket Médio" />
                  <SortHeader field="profitPerStudent" label="Lucro/Aluno" />
                  <SortHeader field="totalOrdersMonth" label="Pedidos" />
                  <SortHeader field="month" label="Mês" />
                </tr>
              </thead>
              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {metrics.length ? metrics.map((m, i) => (
                  <tr key={i} className="hover:bg-gray-700">
                    <td className="p-2">{m.schoolName || "—"}</td>
                    <td className="p-2">{m.totalStudentsRegistered || 0}</td>
                    <td className="p-2">{Number(m.studentsRegisteredVsTotal || 0).toFixed(2)}%</td>
                    <td className="p-2">R$ {Number(m.averageTicket || 0).toFixed(2)}</td>
                    <td className="p-2">R$ {Number(m.profitPerStudent || 0).toFixed(2)}</td>
                    <td className="p-2">{m.totalOrdersMonth || 0}</td>
                    <td className="p-2">{formatMonth(m.month)}</td>
                  </tr>
                )) : (
                  <tr>
                    <td colSpan="7" className="text-center p-4 text-gray-400">Nenhum dado disponível</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </CardContent>

        {/* PAGINAÇÃO + LINHAS POR PÁGINA */}
        <div className="flex justify-between items-center mt-4 mb-8 w-full">
          <div>
            <span>Exibir: </span>
            <select
              value={rowsPerPage}
              onChange={(e) => { setRowsPerPage(Number(e.target.value)); setPage(0); }}
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