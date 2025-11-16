import React, { useEffect, useState } from "react";
import api from "../services/api";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";

export default function MetricsPageTest() {
  const [metrics, setMetrics] = useState([]);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);

  // Carrega métricas do backend sem filtros
  const loadMetrics = async () => {
    console.log("🔹 Carregando métricas, página:", page);
    try {
      const res = await api.get("/metrics", {
        params: { page, size: 5 },
      });
      console.log("✅ Resposta da API:", res.data);

      const data = Array.isArray(res.data) ? res.data : res.data?.content || [];
      setMetrics(data);
      setTotalPages(res.data?.totalPages || 1);
    } catch (err) {
      console.error("❌ Erro ao carregar métricas:", err);
      setMetrics([]);
      setTotalPages(1);
    }
  };

  useEffect(() => {
    loadMetrics();
  }, [page]);

  const formatMonth = (monthStr) => {
    if (!monthStr) return "-";
    const monthNumber = new Date(`${monthStr}-01-2025`).getMonth();
    return new Intl.DateTimeFormat("pt-BR", { month: "short" }).format(
      new Date(2025, monthNumber, 1)
    );
  };

  return (
    <div className="p-6 bg-gray-900 min-h-screen text-gray-100">
      <h2 className="text-2xl font-bold mb-4 text-gray-100">Métricas Teste</h2>

      <Card>
        <CardContent>
          <div className="overflow-x-auto">
            <table className="min-w-full border border-gray-700 divide-y divide-gray-700">
              <thead className="bg-gray-700 text-gray-100">
                <tr>
                  <th className="p-2 text-left">Cliente</th>
                  <th className="p-2 text-left">Alunos Registrados</th>
                  <th className="p-2 text-left">% Registrados</th>
                  <th className="p-2 text-left">Ticket Médio</th>
                  <th className="p-2 text-left">Lucro/Aluno</th>
                  <th className="p-2 text-left">Pedidos</th>
                  <th className="p-2 text-left">Mês</th>
                </tr>
              </thead>

              <tbody className="bg-gray-800 divide-y divide-gray-700">
                {metrics.length > 0 ? (
                  metrics.map((m, i) => (
                    <tr key={i} className="hover:bg-gray-700">
                      <td className="p-2">{m.schoolName || m.clientName || "—"}</td>
                      <td className="p-2">{m.totalStudentsRegistered || m.registeredStudents || 0}</td>
                      <td className="p-2">{Number(m.studentsRegisteredVsTotal || 0).toFixed(2)}%</td>
                      <td className="p-2">R$ {Number(m.averageTicket || 0).toFixed(2)}</td>
                      <td className="p-2">R$ {Number(m.profitPerStudent || 0).toFixed(2)}</td>
                      <td className="p-2">{m.totalOrdersMonth || m.orderCount || 0}</td>
                      <td className="p-2">{formatMonth(m.month || m.monthDate?.split("-")[1])}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="7" className="text-center p-4 text-gray-400">
                      Nenhum dado disponível
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
        </CardContent>
      </Card>
    </div>
  );
}
