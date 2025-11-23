import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical } from "lucide-react";
import api from "../services/api";

const VALID_SORT_FIELDS = [
  "dataId",
  "clientId",
  "monthDate",
  "revenue",
  "expenses",
  "orderCount",
  "registeredStudents",
  "notes",
];

export default function DataClientPage() {
  const [allData, setAllData] = useState([]);
  const [data, setData] = useState([]);

  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  const [sortBy, setSortBy] = useState("dataId");
  const [direction, setDirection] = useState("asc");

  const [clientFilter, setClientFilter] = useState("");
  const [dateStartFilter, setDateStartFilter] = useState("");
  const [dateEndFilter, setDateEndFilter] = useState("");
  const [menuOpen, setMenuOpen] = useState(false);

  const [rowsPerPage, setRowsPerPage] = useState(50);

  // ================================
  // FETCH DO BACKEND (SEM PAGINAÇÃO)
  // ================================
  const fetchData = useCallback(async () => {
    try {
      const params = {};
      if (clientFilter) params.clientId = clientFilter;
      if (dateStartFilter) params.dateStart = dateStartFilter;
      if (dateEndFilter) params.dateEnd = dateEndFilter;

      const res = await api.get("/client-data/filter", { params });
      const content = Array.isArray(res.data?.content) ? res.data.content : res.data || [];
      const fixed = content.map((d) => ({ ...d, registeredStudents: d.registeredStudents ?? 0 }));
      setAllData(fixed);
    } catch (err) {
      console.error(err);
      setAllData([]);
    }
  }, [clientFilter, dateStartFilter, dateEndFilter]);

  // ================================
  // DEBOUNCE PARA FILTROS
  // ================================
  useEffect(() => {
    const t = setTimeout(() => {
      setPage(0);
      fetchData();
    }, 500);
    return () => clearTimeout(t);
  }, [clientFilter, dateStartFilter, dateEndFilter, fetchData]);

  // ================================
  // PAGINAÇÃO E ORDENAÇÃO LOCAL
  // ================================
  useEffect(() => {
    if (!allData.length) {
      setData([]);
      setTotalPages(1);
      return;
    }

    const sorted = [...allData].sort((a, b) => {
      const aVal = a[sortBy] ?? "";
      const bVal = b[sortBy] ?? "";
      if (aVal < bVal) return direction === "asc" ? -1 : 1;
      if (aVal > bVal) return direction === "asc" ? 1 : -1;
      return 0;
    });

    const start = page * rowsPerPage;
    const end = start + rowsPerPage;

    setTotalPages(Math.ceil(sorted.length / rowsPerPage));
    setData(sorted.slice(start, end));
  }, [allData, sortBy, direction, page, rowsPerPage]);

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
            sortBy === field ? (direction === "asc" ? "text-green-400 rotate-180" : "text-red-400 rotate-0") : "text-gray-300 opacity-40"
          }`}
        />
      </div>
    </th>
  );

  const downloadCSV = () => {
    if (!allData.length) return;
    const headers = ["ID Registro", "ID Cliente", "Mês", "Receita", "Despesas", "Pedidos", "Alunos Registrados", "Notas"];
    const rows = allData.map((d) => [
      d.dataId,
      d.clientId,
      new Date(d.monthDate).toLocaleDateString("pt-BR", { month: "long", year: "numeric" }),
      d.revenue,
      d.expenses,
      d.orderCount,
      d.registeredStudents,
      d.notes || "",
    ]);
    const csvContent = "data:text/csv;charset=utf-8," + [headers, ...rows].map((e) => e.join(",")).join("\n");
    const link = document.createElement("a");
    link.href = encodeURI(csvContent);
    link.download = "client-data.csv";
    link.click();
  };

  const handlePrint = () => {
    if (!data.length) return;

    const tableHtml = `
      <table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
        <thead>
          <tr style="background-color: #444; color: #fff;">
            <th>ID Registro</th>
            <th>ID Cliente</th>
            <th>Mês</th>
            <th>Receita</th>
            <th>Despesas</th>
            <th>Pedidos</th>
            <th>Alunos Registrados</th>
            <th>Notas</th>
          </tr>
        </thead>
        <tbody>
          ${data
            .map(
              (d) => `
            <tr>
              <td>${d.dataId}</td>
              <td>${d.clientId}</td>
              <td>${new Date(d.monthDate).toLocaleDateString("pt-BR", { month: "long", year: "numeric" })}</td>
              <td>${d.revenue}</td>
              <td>${d.expenses}</td>
              <td>${d.orderCount}</td>
              <td>${d.registeredStudents}</td>
              <td>${d.notes || ""}</td>
            </tr>`
            )
            .join("")}
        </tbody>
      </table>
    `;
    const WinPrint = window.open("", "", "width=900,height=650");
    WinPrint.document.write("<html><head><title>Dados dos Clientes</title></head><body>");
    WinPrint.document.write("<h2 style='text-align:center;'>Dados dos Clientes</h2>");
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
        <div className="absolute top-4 right-4 flex gap-2 z-50">
          <Download size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={downloadCSV} />
          <Printer size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={handlePrint} />
          <MoreVertical size={20} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={() => setMenuOpen(!menuOpen)} />
        </div>

        {menuOpen && (
          <CardContent className="mb-4 flex flex-col gap-2">
            <input placeholder="ID Cliente" type="number" value={clientFilter} onChange={(e) => setClientFilter(e.target.value)} className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100 no-spin" />
            <input placeholder="Data Inicial" type="date" value={dateStartFilter} onChange={(e) => setDateStartFilter(e.target.value)} className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100" />
            <input placeholder="Data Final" type="date" value={dateEndFilter} onChange={(e) => setDateEndFilter(e.target.value)} className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100" />
            <select value={rowsPerPage} onChange={(e) => { setRowsPerPage(Number(e.target.value)); setPage(0); }} className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100 w-40">
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
          </CardContent>
        )}

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
                {data.length ? data.map((d) => (
                  <tr key={d.dataId} className="hover:bg-gray-700">
                    <td className="p-2">{d.dataId}</td>
                    <td className="p-2">{d.clientId}</td>
                    <td className="p-2">{new Date(d.monthDate).toLocaleDateString("pt-BR", { month: "long", year: "numeric" })}</td>
                    <td className="p-2">{d.revenue}</td>
                    <td className="p-2">{d.expenses}</td>
                    <td className="p-2">{d.orderCount}</td>
                    <td className="p-2">{d.registeredStudents}</td>
                    <td className="p-2">{d.notes}</td>
                  </tr>
                )) : (
                  <tr>
                    <td colSpan="8" className="text-center p-4 text-gray-400">Nenhum dado disponível</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          <div className="w-full flex justify-center mt-6">
            <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
