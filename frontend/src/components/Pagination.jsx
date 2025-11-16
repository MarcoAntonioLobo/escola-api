import React from "react";

export default function Pagination({ page, totalPages, onPageChange }) {
  return (
    <div className="col-span-full w-full flex justify-center items-center gap-4 my-8">
      <button
        disabled={page === 0}
        onClick={() => onPageChange(page - 1)}
        className="px-4 py-2 bg-gray-300 text-gray-800 rounded-xl disabled:opacity-50 hover:bg-gray-400 transition"
      >
        Anterior
      </button>

      <span className="text-gray-700 font-medium">
        Página {page + 1} de {totalPages}
      </span>

      <button
        disabled={page + 1 >= totalPages}
        onClick={() => onPageChange(page + 1)}
        className="px-4 py-2 bg-gray-300 text-gray-800 rounded-xl disabled:opacity-50 hover:bg-gray-400 transition"
      >
        Próxima
      </button>
    </div>
  );
}
