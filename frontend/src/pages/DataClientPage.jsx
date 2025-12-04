import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical } from "lucide-react";
import api from "../services/api";

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

  const VALID_SORT_FIELDS = [
    "dataId",
    "clientId",
    "monthDate",
    "revenue",
    "expenses",
    "orderCount",
    "registeredStudents",
    "notes",
    // Cantina
    "studentCount", "avgCafeteria", "avgPerStudent", "monthlyOrders", "billing",
    // Vlupt
    "profitability", "evasion", "outsideOrders", "appTicket"
  ];

  const fetchData = useCallback(async () => {
    try {
      const params = {};
      if (clientFilter) params.clientId = clientFilter;
      if (dateStartFilter) params.dateStart = dateStartFilter;
      if (dateEndFilter) params.dateEnd = dateEndFilter;

      const res = await api.get("/client-data/filter", { params });
      const content = Array.isArray(res.data?.content) ? res.data.content : res.data || [];
      setAllData(content.map(d => ({
        ...d,
        registeredStudents: d.registeredStudents ?? 0,
        studentCount: d.studentCount ?? 0,
        avgCafeteria: d.avgCafeteria ?? 0,
        avgPerStudent: d.avgPerStudent ?? 0,
        monthlyOrders: d.orderCount ?? 0,
        billing: d.revenue ?? 0,
        expenses: d.expenses ?? 0,
        profitability: d.profitability ?? 0,
        evasion: d.evasion ?? 0,
        outsideOrders: d.outsideOrders ?? 0,
        appTicket: d.appTicket ?? 0
      })));
    } catch (err) {
      console.error(err);
      setAllData([]);
    }
  }, [clientFilter, dateStartFilter, dateEndFilter]);

  useEffect(() => {
    const t = setTimeout(() => {
      setPage(0);
      fetchData();
    }, 500);
    return () => clearTimeout(t);
  }, [clientFilter, dateStartFilter, dateEndFilter, fetchData]);

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

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100 flex justify-center">
      <Card className="relative w-full max-w-6xl">
        {/* Menu, Download e Print */}
        <div className="absolute top-4 right-4 flex gap-2 z-50">
          <Download size={23} className="text-gray-100 cursor-pointer hover:text-green-400 transition" />
          <Printer size={23} className="text-gray-100 cursor-pointer hover:text-green-400 transition" />
          <MoreVertical size={23} className="text-gray-100 cursor-pointer hover:text-green-400 transition" onClick={() => setMenuOpen(!menuOpen)} />
        </div>

        {menuOpen && (
          <CardContent className="mb-6 flex flex-col gap-6 px-6 py-4 space-y-4 bg-gray-900 rounded-lg border border-gray-700">
            <input placeholder="ID Cliente" type="number" value={clientFilter} onChange={(e) => setClientFilter(e.target.value)}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-300 placeholder-gray-500" />
            <input placeholder="Data Inicial" type="date" value={dateStartFilter} onChange={(e) => setDateStartFilter(e.target.value)}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-400 placeholder-gray-400" />
            <input placeholder="Data Final" type="date" value={dateEndFilter} onChange={(e) => setDateEndFilter(e.target.value)}
              className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-400 placeholder-gray-400" />
          </CardContent>
        )}

        {/* Tabela única dividida em Cantina e Vlupt */}
        <CardContent className="mb-6 px-6 py-4 overflow-x-auto">
          <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
            <thead className="bg-gray-700 text-gray-100">
              <tr>
                <th colSpan={5} className="text-center p-2 border-b border-gray-600">Cantina</th>
                <th colSpan={5} className="text-center p-2 border-b border-gray-600">Vlupt</th>
              </tr>
              <tr>
                <SortHeader field="studentCount" label="L. Aluno Cad" />
                <SortHeader field="avgCafeteria" label="T. Méd Cant." />
                <SortHeader field="avgPerStudent" label="Med. Ped. aluno" />
                <SortHeader field="monthlyOrders" label="Qtde Pedido M" />
                <SortHeader field="billing" label="Faturamento" />
                
                <SortHeader field="profitability" label="Rentabilidade" />
                <SortHeader field="evasion" label="Evasão de $$$" />
                <SortHeader field="outsideOrders" label="Ped. Fora Vpt" />
                <SortHeader field="appTicket" label="Ticket M. App." />
                <SortHeader field="expenses" label="Despesas" />
              </tr>
            </thead>
            <tbody className="bg-gray-800 divide-y divide-gray-700">
              {data.length ? data.map(d => (
                <tr key={d.dataId} className="hover:bg-gray-700">
                  <td className="p-2">{d.studentCount}</td>
                  <td className="p-2">{d.avgCafeteria}</td>
                  <td className="p-2">{d.avgPerStudent}</td>
                  <td className="p-2">{d.monthlyOrders}</td>
                  <td className="p-2">{d.billing}</td>

                  <td className="p-2">{d.profitability}</td>
                  <td className="p-2">{d.evasion}</td>
                  <td className="p-2">{d.outsideOrders}</td>
                  <td className="p-2">{d.appTicket}</td>
                  <td className="p-2">{d.expenses}</td>
                </tr>
              )) : (
                <tr>
                  <td colSpan="10" className="text-center p-4 text-gray-400">Nenhum dado disponível</td>
                </tr>
              )}
            </tbody>
          </table>
        </CardContent>

        {/* PAGINAÇÃO */}
        <div className="flex justify-between items-center mt-4 mb-8 w-full">
          <div>
            <span>Exibir: </span>
            <select value={rowsPerPage} onChange={(e) => { setRowsPerPage(Number(e.target.value)); setPage(0); }} className="p-2 border border-gray-700 rounded-lg bg-gray-900 text-gray-100">
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
