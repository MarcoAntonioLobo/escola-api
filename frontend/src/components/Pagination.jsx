import React from "react";
import "../index.css";

export default function Pagination({ page, totalPages, onPageChange }) {
  const maxButtons = 5;

  const startPage = Math.max(0, page - Math.floor(maxButtons / 2));
  const endPage = Math.min(totalPages - 1, startPage + maxButtons - 1);

  const pages = [];
  for (let i = startPage; i <= endPage; i++) {
    pages.push(i);
  }

  return (
    <div className="pagination-container">
      {/* Botão ANTERIOR */}
      <button
        disabled={page === 0}
        onClick={() => onPageChange(page - 1)}
        className="pagination-btn"
      >
        ◀
      </button>

      {/* Botões numéricos */}
      <div className="pagination-numbers">
        {pages.map((num) => (
          <button
            key={num}
            onClick={() => onPageChange(num)}
            className={`pagination-number ${
              num === page ? "active" : ""
            }`}
          >
            {num + 1}
          </button>
        ))}
      </div>

      {/* Botão PRÓXIMO */}
      <button
        disabled={page === totalPages - 1}
        onClick={() => onPageChange(page + 1)}
        className="pagination-btn"
      >
        ▶
      </button>
    </div>
  );
}
