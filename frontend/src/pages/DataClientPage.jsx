import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, MoreVertical, Eye, EyeOff } from "lucide-react";
import api from "../services/api";

export default function DataClientPage() {
	const [allData, setAllData] = useState([]);
	const [data, setData] = useState([]);
	const [page, setPage] = useState(0);
	const [totalPages, setTotalPages] = useState(1);
	const [sortBy, setSortBy] = useState("dataId");
	const [direction, setDirection] = useState("asc");

	const [clientFilter] = useState("");
	const [dateStartFilter, setDateStartFilter] = useState("");
	const [dateEndFilter, setDateEndFilter] = useState("");
	const [locationFilter, setLocationFilter] = useState("");
	const [schoolFilter, setSchoolFilter] = useState("");
	const [cafeteriaFilter, setCafeteriaFilter] = useState("");

	const [menuOpen, setMenuOpen] = useState(false);
	const [rowsPerPage, setRowsPerPage] = useState(50);

	const [visibleColumns, setVisibleColumns] = useState({
		dataId: true,
		description: true,
		cafeteria: true,
		location: true,
		studentCount: true,
		monthDate: true,
		vlupt: true,
		cantinaPercent: true,
		registeredStudents: true,
		averageCantinaPerStudent: true,
		averagePedagogicalPerStudent: true,
		orderCount: true,
		revenue: true,
		profitability: true,
		revenueLoss: true,
		ordersOutsideVpt: true,
		averageTicketApp: true,
	});

	const VALID_SORT_FIELDS = Object.keys(visibleColumns);

	// ================================
	// FETCH DADOS
	// ================================
	const fetchData = useCallback(async () => {
		try {
			const clientsRes = await api.get("/clients");
			const clients = clientsRes.data || [];

			const params = {};
			if (clientFilter) params.clientId = clientFilter;
			if (dateStartFilter) params.dateStart = dateStartFilter;
			if (dateEndFilter) params.dateEnd = dateEndFilter;
			if (locationFilter) params.location = locationFilter;
			if (schoolFilter) params.school = schoolFilter;
			if (cafeteriaFilter) params.cafeteria = cafeteriaFilter;

			const dataRes = await api.get("/client-data/filter", { params });
			const dataClient = Array.isArray(dataRes.data?.content) ? dataRes.data.content : dataRes.data || [];

			const combined = dataClient.map((d) => {
				const client = clients.find((c) => c.clientId === d.clientId) || {};
				return {
					...d,
					description: client.schoolName || "-",
					cafeteria: client.cafeteriaName || "-",
					location: client.location || "-",
					studentCount: client.studentCount ?? "-",
				};
			});

			setAllData(combined);
		} catch (err) {
			console.error(err);
			setAllData([]);
		}
	}, [clientFilter, dateStartFilter, dateEndFilter, locationFilter, schoolFilter, cafeteriaFilter]);

	useEffect(() => {
		const t = setTimeout(() => {
			setPage(0);
			fetchData();
		}, 500);
		return () => clearTimeout(t);
	}, [clientFilter, dateStartFilter, dateEndFilter, locationFilter, schoolFilter, cafeteriaFilter, fetchData]);

	// ================================
	// PAGINAÇÃO + ORDENAÇÃO
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
			const aStr = typeof aVal === "string" ? aVal.toLowerCase() : aVal;
			const bStr = typeof bVal === "string" ? bVal.toLowerCase() : bVal;

			if (aStr < bStr) return direction === "asc" ? -1 : 1;
			if (aStr > bStr) return direction === "asc" ? 1 : -1;
			return 0;
		});

		const start = page * rowsPerPage;
		const end = start + rowsPerPage;

		setTotalPages(Math.max(1, Math.ceil(sorted.length / rowsPerPage)));
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

	const toggleColumn = (column) => {
		setVisibleColumns((prev) => ({ ...prev, [column]: !prev[column] }));
	};

	const SortHeader = ({ field, label }) => (
		<th className="p-2 text-left select-none">
			<div className="flex items-center justify-between">
				<span className="cursor-pointer flex items-center gap-1" onClick={() => toggleSort(field)}>
					{label}
					<ArrowUpDown
						size={14}
						className={`transition-all duration-200 ${sortBy === field
								? direction === "asc"
									? "text-green-400 rotate-180"
									: "text-red-400"
								: "text-gray-300 opacity-40"
							}`}
					/>
				</span>
				<span onClick={() => toggleColumn(field)} className="cursor-pointer">
					{visibleColumns[field] ? <Eye size={14} /> : <EyeOff size={14} />}
				</span>
			</div>
		</th>
	);

	// ================================
	// FORMATAÇÃO
	// ================================
	const formatMoney = (value) => value != null ? new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value) : "-";
	const formatPercent = (value) => value != null ? `${(value * 100).toFixed(2)}%` : "-";
	const formatUnit = (value) => value != null ? new Intl.NumberFormat("pt-BR").format(value) : "-";
	const formatDate = (value) => value ? new Date(value).toLocaleDateString("pt-BR") : "-";

	// ================================
	// CSV EXPORT (FORMATADO)
	// ================================
	const downloadCSV = () => {
		if (!allData.length) return;

		const columnNames = {
			dataId: "ID",
			description: "Escola",
			cafeteria: "Cantina",
			location: "Localização",
			studentCount: "Qtd. Alunos",
			monthDate: "Data",
			cantinaPercent: "Vpt x Escola %",
			registeredStudents: "L. Aluno Cad",
			averageCantinaPerStudent: "T. Méd Cant.",
			averagePedagogicalPerStudent: "Med. Ped. Aluno",
			orderCount: "Qtde Pedido M",
			revenue: "Faturamento",
			profitability: "Rentabilidade",
			revenueLoss: "Evasão de $$$",
			ordersOutsideVpt: "Ped. Fora Vpt",
			averageTicketApp: "Ticket M. App."
		};

		const headers = Object.keys(visibleColumns)
			.filter((col) => visibleColumns[col])
			.map((col) => `"${columnNames[col]}"`);

		const rows = allData.map((d) =>
			Object.keys(visibleColumns)
				.filter((col) => visibleColumns[col])
				.map((col) => {
					let val = d[col];
					if (val === null || val === undefined) val = "";
					if (["revenue", "profitability", "revenueLoss", "averageCantinaPerStudent", "averagePedagogicalPerStudent", "averageTicketApp", "registeredStudents"].includes(col)) val = formatMoney(val);
					if (["vlupt"].includes(col)) val = formatPercent(val);
					if (["orderCount", "ordersOutsideVpt", "studentCount"].includes(col)) val = formatUnit(val);
					if (col === "monthDate") val = formatDate(val);
					if (typeof val === "string") val = val.replace(/"/g, '""');
					return `"${val}"`;
				})
		);

		const csvContent =
			"data:text/csv;charset=utf-8," + [headers, ...rows].map((e) => e.join(",")).join("\n");

		const link = document.createElement("a");
		link.href = encodeURI(csvContent);
		link.download = "client_data.csv";
		link.click();
	};

	// ================================
	// IMPRESSÃO (FORMATADO)
	// ================================
	const handlePrint = () => {
	  if (!data.length) return;

	  const columnNames = {
	    dataId: "ID",
	    description: "Escola",
	    cafeteria: "Cantina",
	    location: "Localização",
	    studentCount: "Qtd. Alunos",
	    monthDate: "Data",
	    cantinaPercent: "Vpt x Escola %",
	    registeredStudents: "L. Aluno Cad",
	    averageCantinaPerStudent: "T. Méd Cant.",
	    averagePedagogicalPerStudent: "Med. Ped. Aluno",
	    orderCount: "Qtde Pedido M",
	    revenue: "Faturamento",
	    profitability: "Rentabilidade",
	    revenueLoss: "Evasão de $$$",
	    ordersOutsideVpt: "Ped. Fora Vpt",
	    averageTicketApp: "Ticket M. App."
	  };

	  // Lista consistente de colunas visíveis que têm label definido
	  const visibleCols = Object.keys(visibleColumns)
	    .filter((col) => visibleColumns[col] && columnNames[col] !== undefined);

	  const tableHtml = `
	    <table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
	      <thead>
	        <tr style="background-color: #444; color: #fff;">
	          ${visibleCols.map((col) => `<th>${columnNames[col]}</th>`).join("")}
	        </tr>
	      </thead>
	      <tbody>
	        ${data
	          .map((d) => {
	            return `<tr>${
	              visibleCols
	                .map((col) => {
	                  let val = d[col];
	                  if (val === null || val === undefined) val = "";

	                  if (["revenue", "profitability", "revenueLoss", "averageCantinaPerStudent", "averagePedagogicalPerStudent", "averageTicketApp", "registeredStudents"].includes(col)) {
	                    val = formatMoney(val);
	                  } else if (["cantinaPercent"].includes(col)) {
	                    val = formatPercent(val);
	                  } else if (["orderCount", "ordersOutsideVpt", "studentCount"].includes(col)) {
	                    val = formatUnit(val);
	                  } else if (col === "monthDate") {
	                    val = formatDate(val);
	                  }

	                  // garantir string segura para HTML
	                  if (typeof val === "string") val = val.replace(/</g, "&lt;").replace(/>/g, "&gt;");

	                  return `<td>${val}</td>`;
	                })
	                .join("")
	            }</tr>`;
	          })
	          .join("")}
	      </tbody>
	    </table>
	  `;

	  const WinPrint = window.open("", "", "width=900,height=650");
	  WinPrint.document.write("<html><head><title>Client Data</title></head><body>");
	  WinPrint.document.write("<h2 style='text-align:center;'>Client Data</h2>");
	  WinPrint.document.write(tableHtml);
	  WinPrint.document.write("</body></html>");
	  WinPrint.document.close();
	  WinPrint.focus();
	  WinPrint.print();
	  WinPrint.close();
	};

	// ================================
	// RENDER
	// ================================
	return (
		<div className="p-6 bg-gray-900 min-h-screen text-gray-100 flex justify-center">
			<Card className="relative w-full max-w-7xl">
				<div className="absolute top-4 right-4 flex gap-2 z-50">
					<Download size={23} className="cursor-pointer" onClick={downloadCSV} />
					<Printer size={23} className="cursor-pointer" onClick={handlePrint} />
					<MoreVertical size={23} className="cursor-pointer" onClick={() => setMenuOpen(!menuOpen)} />
				</div>

				{menuOpen && (
					<CardContent className="mb-6 flex flex-col gap-4 px-6 py-4 bg-gray-900 rounded-lg border border-gray-700">
						<input
							placeholder="Filtrar por Escola"
							value={schoolFilter}
							onChange={(e) => setSchoolFilter(e.target.value)}
							className="input"
						/>
						<input
							placeholder="Filtrar por Cantina"
							value={cafeteriaFilter}
							onChange={(e) => setCafeteriaFilter(e.target.value)}
							className="input"
						/>
						<input
							placeholder="Filtrar por Localização"
							value={locationFilter}
							onChange={(e) => setLocationFilter(e.target.value)}
							className="input"
						/>
						<input
							type="text"
							placeholder="Data Inicial"
							value={dateStartFilter}
							onFocus={(e) => e.target.type = "date"}
							onBlur={(e) => { if (!e.target.value) e.target.type = "text"; }}
							onChange={(e) => setDateStartFilter(e.target.value)}
							className="input"
						/>
						<input
							type="text"
							placeholder="Data Final"
							value={dateEndFilter}
							onFocus={(e) => e.target.type = "date"}
							onBlur={(e) => { if (!e.target.value) e.target.type = "text"; }}
							onChange={(e) => setDateEndFilter(e.target.value)}
							className="input"
						/>
					</CardContent>
				)}

				<CardContent className="mb-6 px-6 py-4 overflow-x-auto">
					<table className="min-w-full border border-gray-700 divide-y divide-gray-700 rounded-lg overflow-hidden shadow-lg">
						<thead>
							{/* LINHA DE AGRUPAMENTO */}
							<tr>
								{visibleColumns.description && <th colSpan={6} className="p-2 text-left">Descrição</th>}
								{visibleColumns.cafeteria && <th colSpan={6} className="p-2 text-left">Cantina</th>}
								{visibleColumns.vlupt && <th colSpan={4} className="p-2 text-left">Vlupt</th>}
							</tr>

							{/* LINHA DE SUB-CABECALHO */}
							<tr>
								{visibleColumns.description && <SortHeader field="dataId" label="ID" />}
								{visibleColumns.description && <SortHeader field="description" label="Escola" />}
								{visibleColumns.cafeteria && <SortHeader field="cafeteria" label="Cantina" />}
								{visibleColumns.location && <SortHeader field="location" label="Localização" />}
								{visibleColumns.studentCount && <SortHeader field="studentCount" label="Qtd. Alunos" />}
								{visibleColumns.monthDate && <SortHeader field="monthDate" label="Data" />}
								{visibleColumns.cantinaPercent && <SortHeader field="cantinaPercent" label="Vpt x Escola %" />}
								{visibleColumns.registeredStudents && <SortHeader field="registeredStudents" label="L. Aluno Cad" />}
								{visibleColumns.averageCantinaPerStudent && <SortHeader field="averageCantinaPerStudent" label="T. Méd Cant." />}
								{visibleColumns.averagePedagogicalPerStudent && <SortHeader field="averagePedagogicalPerStudent" label="Med. Ped. Aluno" />}
								{visibleColumns.orderCount && <SortHeader field="orderCount" label="Qtde Pedido M" />}
								{visibleColumns.revenue && <SortHeader field="revenue" label="Faturamento" />}
								{visibleColumns.profitability && <SortHeader field="profitability" label="Rentabilidade" />}
								{visibleColumns.revenueLoss && <SortHeader field="revenueLoss" label="Evasão de $$$" />}
								{visibleColumns.ordersOutsideVpt && <SortHeader field="ordersOutsideVpt" label="Ped. Fora Vpt" />}
								{visibleColumns.averageTicketApp && <SortHeader field="averageTicketApp" label="Ticket M. App." />}
							</tr>
						</thead>

						<tbody className="bg-gray-800 divide-y divide-gray-700">
							{data.length ? data.map((d) => (
								<tr key={d.dataId} className="hover:bg-gray-700">
									{visibleColumns.dataId && <td className="p-2">{d.dataId}</td>}
									{visibleColumns.description && <td className="p-2">{d.description}</td>}
									{visibleColumns.cafeteria && <td className="p-2">{d.cafeteria}</td>}
									{visibleColumns.location && <td className="p-2">{d.location}</td>}
									{visibleColumns.studentCount && <td className="p-2">{formatUnit(d.studentCount)}</td>}
									{visibleColumns.monthDate && <td className="p-2">{formatDate(d.monthDate)}</td>}
									{visibleColumns.cantinaPercent && <td className="p-2">{formatPercent(d.cantinaPercent)}</td>}
									{visibleColumns.registeredStudents && <td className="p-2">{formatMoney(d.registeredStudents)}</td>}
									{visibleColumns.averageCantinaPerStudent && <td className="p-2">{formatMoney(d.averageCantinaPerStudent)}</td>}
									{visibleColumns.averagePedagogicalPerStudent && <td className="p-2">{formatMoney(d.averagePedagogicalPerStudent)}</td>}
									{visibleColumns.orderCount && <td className="p-2">{formatUnit(d.orderCount)}</td>}
									{visibleColumns.revenue && <td className="p-2">{formatMoney(d.revenue)}</td>}
									{visibleColumns.profitability && <td className="p-2">{formatMoney(d.profitability)}</td>}
									{visibleColumns.revenueLoss && <td className="p-2">{formatMoney(d.revenueLoss)}</td>}
									{visibleColumns.ordersOutsideVpt && <td className="p-2">{formatUnit(d.ordersOutsideVpt)}</td>}
									{visibleColumns.averageTicketApp && <td className="p-2">{formatMoney(d.averageTicketApp)}</td>}
								</tr>
							)) : (
								<tr>
									<td colSpan="16" className="text-center p-4 text-gray-400">Nenhum dado disponível</td>
								</tr>
							)}
						</tbody>
					</table>
				</CardContent>

				<div className="flex justify-between items-center mt-4 mb-8 w-full">
					<div className="flex items-center gap-2">
						<span>Exibir:</span>
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
