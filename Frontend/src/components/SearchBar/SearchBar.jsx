import React from 'react';
import './SearchBar.css';

function SearchBar({ value, onChange, onClear }) {
  return (
    <div className="search-bar">
      <div className="search-container">
        <input
          type="text"
          placeholder="Buscar criptomoeda (ex: Bitcoin, BTC)..."
          value={value}
          onChange={(event) => onChange(event.target.value)}
          className="search-input"
          aria-label="Buscar criptomoeda"
        />
        {value && (
          <button
            className="search-clear"
            onClick={onClear}
            aria-label="Limpar busca"
            title="Limpar"
          >
            ✕
          </button>
        )}
        <span className="search-icon">🔍</span>
      </div>
    </div>
  );
}

export default SearchBar;
