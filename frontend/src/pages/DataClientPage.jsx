import React, { useEffect, useState, useCallback } from "react";
import { Card, CardContent } from "../components/ui/Card";
import Pagination from "../components/Pagination";
import { ArrowUpDown, Printer, Download, Search, Eye, EyeOff, FolderUp } from "lucide-react";
import api from "../services/api";
import * as XLSX from "xlsx";

export default function DataClientPage() {
	// ===============================
	// ESTADOS PRINCIPAIS
	// ===============================
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

	const [showUpload, setShowUpload] = useState(false);
	const [menuOpen, setMenuOpen] = useState(false);
	const [rowsPerPage, setRowsPerPage] = useState(50);

	const [visibleColumns, setVisibleColumns] = useState({
		dataId: true,
		description: true,
		cafeteria: true,
		location: true,
		studentCount: true,
		monthDate: true,
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

	// ===============================
	// ESTADOS UPLOAD EXCEL
	// ===============================
	const [excelFile, setExcelFile] = useState(null);

	const handleExcelUpload = async () => {
		if (!excelFile) {
			alert("Selecione um arquivo!");
			return;
		}

		const formData = new FormData();
		formData.append("file", excelFile);
		formData.append("table", "client_data");

		try {
			const response = await api.post("/convert/excel-to-db", formData, {
				headers: { "Content-Type": "multipart/form-data" },
			});

			alert(response.data);
			fetchData();
			setExcelFile(null);
		} catch (err) {
			console.error(err);
			alert("Erro ao enviar arquivo!");
		}
	};

	// ===============================
	// CONFIGURAÇÃO DE COLUNAS
	// ===============================
	const groupDescription = ["dataId", "description", "cafeteria", "location", "studentCount", "monthDate"];
	const groupCantina = ["cantinaPercent", "registeredStudents", "averageCantinaPerStudent", "averagePedagogicalPerStudent", "orderCount", "revenue"];
	const groupVlupt = ["profitability", "revenueLoss", "ordersOutsideVpt", "averageTicketApp"];

	const countVisible = (group) => group.filter((col) => visibleColumns[col]).length;
	const VALID_SORT_FIELDS = Object.keys(visibleColumns);

	// ===============================
	// FETCH DADOS
	// ===============================
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

	// ===============================
	// ORDENAÇÃO + PAGINAÇÃO
	// ===============================
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

	// ===============================
	// FORMATAÇÃO
	// ===============================
	const formatMoney = (value) =>
		value != null ? new Intl.NumberFormat("pt-BR", { style: "currency", currency: "BRL" }).format(value) : "-";
	const formatPercent = (value) => (value != null ? `${(value * 100).toFixed(2)}%` : "-");
	const formatUnit = (value) => (value != null ? new Intl.NumberFormat("pt-BR").format(value) : "-");
	const formatDate = (value) => (value ? new Date(value).toLocaleDateString("pt-BR") : "-");

	// ===============================
	// EXPORT XLSX
	// ===============================
	const downloadXLSX = () => {
		if (!allData.length) return;

		const visibleCols = Object.keys(visibleColumns).filter(col => visibleColumns[col]);

		const formattedData = allData.map((d) => {
			const row = {};

			visibleCols.forEach((col) => {
				let val = d[col];

				if (val === null || val === undefined) {
					row[col] = "";
					return;
				}

				// ===== TRATAMENTO ESPECIAL =====
				if (col === "monthDate") {
					row[col] = new Date(val);
				}
				else if (col === "cantinaPercent") {
					row[col] = Number(val);
				}
				else if ([
					"revenue",
					"profitability",
					"revenueLoss",
					"averageCantinaPerStudent",
					"averagePedagogicalPerStudent",
					"averageTicketApp"
				].includes(col)) {
					row[col] = Number(val);
				}
				else if ([
					"orderCount",
					"ordersOutsideVpt",
					"studentCount",
					"registeredStudents"
				].includes(col)) {
					row[col] = Number(val);
				}
				else {
					row[col] = val;
				}
			});

			return row;
		});

		const worksheet = XLSX.utils.json_to_sheet(formattedData);

		const columnWidths = visibleCols.map(col => ({
			wch: Math.max(10, col.length + 3)
		}));
		worksheet['!cols'] = columnWidths;

		const workbook = XLSX.utils.book_new();
		XLSX.utils.book_append_sheet(workbook, worksheet, "Client Data");
		XLSX.writeFile(workbook, "client_data.xlsx");
	};


	// ===============================
	// IMPRESSÃO
	// ===============================
	const formatPrintValue = (col, value) => {
		if (value === null || value === undefined) return "";

		switch (col) {
			case "monthDate":
				return new Date(value).toLocaleDateString("pt-BR");

			case "cantinaPercent":
				return `${(value * 100).toFixed(2)}%`;

			case "revenue":
			case "profitability":
			case "revenueLoss":
			case "averageCantinaPerStudent":
			case "averagePedagogicalPerStudent":
			case "averageTicketApp":
				return new Intl.NumberFormat("pt-BR", {
					style: "currency",
					currency: "BRL",
				}).format(value);

			case "orderCount":
			case "ordersOutsideVpt":
			case "studentCount":
			case "registeredStudents":
				return new Intl.NumberFormat("pt-BR").format(value);

			default:
				return value;
		}
	};

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
			averageTicketApp: "Ticket M. App.",
		};

		const visibleCols = Object.keys(visibleColumns).filter(
			(col) => visibleColumns[col] && columnNames[col] !== undefined
		);

		const tableHtml = `
		<table border="1" cellspacing="0" cellpadding="4" style="border-collapse: collapse; width: 100%;">
			<thead>
				<tr style="background-color: #444; color: #fff;">
					${visibleCols.map(col => `<th>${columnNames[col]}</th>`).join("")}
				</tr>
			</thead>
			<tbody>
				${data
					.map(
						d => `
						<tr>
							${visibleCols
								.map(col => `<td>${formatPrintValue(col, d[col])}</td>`)
								.join("")}
						</tr>
					`
					)
					.join("")}
			</tbody>
		</table>
		`;

		const WinPrint = window.open("", "", "width=900,height=650");
		WinPrint.document.write(`
			<html>
				<head>
					<title>Client Data</title>
				</head>
				<body>
					<h2 style="text-align:center;">Client Data</h2>
					${tableHtml}
				</body>
			</html>
		`);
		WinPrint.document.close();
		WinPrint.focus();
		WinPrint.print();
		WinPrint.close();
	};


	// ===============================
	// RENDER
	// ===============================
	return (
		<div className="p-6 bg-gray-900 min-h-screen text-gray-100 flex justify-center">
			<Card className="relative w-full max-w-7xl">
				{/* BOTÕES SUPERIORES */}
				<div className="absolute top-4 right-4 flex gap-2 z-50">
					<Download size={28} className="cursor-pointer" onClick={downloadXLSX} />
					<Printer size={28} className="cursor-pointer" onClick={handlePrint} />
					<Search size={28} className="cursor-pointer" onClick={() => setMenuOpen(!menuOpen)} />
					<FolderUp
						size={28}
						className="cursor-pointer"
						onClick={() => setShowUpload((prev) => !prev)}
						title="Upload Excel"
					/>
				</div>

				{/* UPLOAD EXCEL OCULTO */}
				{showUpload && (
					<CardContent className="mt-16 mb-6 px-6 py-4 bg-gray-800 rounded-lg border border-gray-700">
						<h2 className="text-xl font-bold mb-4">Insira aqui a planilha excel</h2>
						<div className="flex flex-col gap-3">
							<input
								type="file"
								accept=".xlsx"
								onChange={(e) => setExcelFile(e.target.files[0])}
								className="p-2 border border-gray-700 rounded bg-gray-900"
							/>
							<button
								onClick={handleExcelUpload}
								className="bg-blue-600 hover:bg-blue-700 p-2 rounded-lg font-bold"
							>
								Enviar
							</button>
						</div>
					</CardContent>
				)}

				{/* FILTROS */}
				{menuOpen && (
					<CardContent className="mb-6 flex flex-col gap-4 px-6 py-4 bg-gray-900 rounded-lg border border-gray-700">
						<input placeholder="Filtrar por Escola" value={schoolFilter} onChange={(e) => setSchoolFilter(e.target.value)} className="input" />
						<input placeholder="Filtrar por Cantina" value={cafeteriaFilter} onChange={(e) => setCafeteriaFilter(e.target.value)} className="input" />
						<input placeholder="Filtrar por Localização" value={locationFilter} onChange={(e) => setLocationFilter(e.target.value)} className="input" />
						<input type="text" placeholder="Data Inicial" value={dateStartFilter} onFocus={(e) => (e.target.type = "date")} onBlur={(e) => { if (!e.target.value) e.target.type = "text"; }} onChange={(e) => setDateStartFilter(e.target.value)} className="input" />
						<input type="text" placeholder="Data Final" value={dateEndFilter} onFocus={(e) => (e.target.type = "date")} onBlur={(e) => { if (!e.target.value) e.target.type = "text"; }} onChange={(e) => setDateEndFilter(e.target.value)} className="input" />
					</CardContent>
				)}

				{/* TABELA */}
				<CardContent className="mb-6 px-6 py-4 overflow-x-auto">
					<table className="min-w-full border border-gray-700 divide-y divide-gray-700 rounded-lg overflow-hidden shadow-lg">
						<thead>
							<tr>
								{countVisible(groupDescription) > 0 && <th colSpan={countVisible(groupDescription)} className="p-2 text-left">Descrição</th>}
								{countVisible(groupCantina) > 0 && <th colSpan={countVisible(groupCantina)} className="p-2 text-left">Cantina</th>}
								{countVisible(groupVlupt) > 0 && <th colSpan={countVisible(groupVlupt)} className="p-2 text-left">Vlupt</th>}
							</tr>

							<tr>
								{groupDescription.map(col => visibleColumns[col] && <SortHeader key={col} field={col} label={{
									dataId: "ID",
									description: "Escola",
									cafeteria: "Cantina",
									location: "Localização",
									studentCount: "Qtd. Alunos",
									monthDate: "Data"
								}[col]} />)}

								{groupCantina.map(col => visibleColumns[col] && <SortHeader key={col} field={col} label={{
									cantinaPercent: "Vpt x Escola %",
									registeredStudents: "L. Aluno Cad",
									averageCantinaPerStudent: "T. Méd Cant.",
									averagePedagogicalPerStudent: "Med. Ped. Aluno",
									orderCount: "Qtde Pedido M",
									revenue: "Faturamento"
								}[col]} />)}

								{groupVlupt.map(col => visibleColumns[col] && <SortHeader key={col} field={col} label={{
									profitability: "Rentabilidade",
									revenueLoss: "Evasão de $$$",
									ordersOutsideVpt: "Ped. Fora Vpt",
									averageTicketApp: "Ticket M. App."
								}[col]} />)}
							</tr>
						</thead>

						<tbody className="bg-gray-800 divide-y divide-gray-700">
							{data.length ? data.map(d => (
								<tr key={d.dataId} className="hover:bg-gray-700">
									{groupDescription.map(col => visibleColumns[col] && (
										<td key={col} className="p-2">
											{col === "studentCount"
												? formatUnit(d[col])
												: col === "monthDate"
													? formatDate(d[col])
													: d[col]}
										</td>
									))}

									{groupCantina.map(col => visibleColumns[col] && (
										<td key={col} className="p-2">
											{col === "cantinaPercent"
												? formatPercent(d[col])
												: ["registeredStudents", "orderCount"].includes(col)
													? formatUnit(d[col])
													: col === "averagePedagogicalPerStudent"
														? formatUnit(d[col], 1)
														: ["averageCantinaPerStudent", "revenue"].includes(col)
															? formatMoney(d[col])
															: d[col]}
										</td>
									))}


									{groupVlupt.map(col => visibleColumns[col] && (
										<td key={col} className="p-2">
											{["ordersOutsideVpt"].includes(col)
												? formatUnit(d[col])
												: formatMoney(d[col])}
										</td>
									))}
								</tr>
							)) : (
								<tr>
									<td
										colSpan={
											countVisible(groupDescription) +
											countVisible(groupCantina) +
											countVisible(groupVlupt)
										}
										className="text-center p-4 text-gray-400"
									>
										Nenhum dado disponível
									</td>
								</tr>
							)}
						</tbody>
					</table>
				</CardContent>

				<div className="flex justify-between items-center mt-4 mb-8 w-full">
					<div className="flex items-center gap-2">
						<span>Exibir:</span>
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