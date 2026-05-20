import React from 'react';
import { MESSAGES } from '../../constants/app.constants';
import SearchBar from '../SearchBar/SearchBar';
import CryptoCard from '../CryptoCard/CryptoCard';
import LoadingSpinner from '../LoadingSpinner/LoadingSpinner';
import EmptyState from '../EmptyState/EmptyState';
import './CryptoList.css';

function CryptoList({
  cryptos,
  searchResults,
  loading,
  error,
  viewMode,
  searchTerm,
  onSearchChange,
  onSearchClear,
  onViewChange,
  onRefresh,
  onSelectCrypto,
  onDeleteCrypto,
  onUpdateCrypto,
}) {
  const displayedCryptos = searchResults.length > 0 ? searchResults : cryptos;

  return (
    <div className="crypto-list-container">
      <div className="list-header">
        <h2>Criptomoedas</h2>
        <div className="list-actions-row">
          <SearchBar
            value={searchTerm}
            onChange={onSearchChange}
            onClear={onSearchClear}
          />
          <div className="view-controls">
          <button
            className={`view-btn ${viewMode === 'all' ? 'active' : ''}`}
            onClick={() => onViewChange('all')}
            aria-label="Visualizar todas as criptomoedas"
          >
            Todas
          </button>
          <button
            className={`view-btn ${viewMode === 'top' ? 'active' : ''}`}
            onClick={() => onViewChange('top')}
            aria-label="Visualizar top 10 criptomoedas"
          >
            Top 10
          </button>
          <button
            className="view-btn refresh-btn"
            onClick={onRefresh}
            disabled={loading}
            aria-label="Recarregar lista"
            title="Recarregar"
          >
            🔄 {loading ? 'Atualizando...' : 'Recarregar'}
          </button>
          </div>
        </div>
      </div>

      {error && (
        <div className="error-message" role="alert">
          ⚠️ {error}
        </div>
      )}

      {loading && <LoadingSpinner message={MESSAGES.LOADING} />}

      {!loading && displayedCryptos.length === 0 && (
        <EmptyState
          message={
            searchResults.length === 0
              ? MESSAGES.EMPTY_SEARCH
              : 'Nenhuma criptomoeda encontrada'
          }
        />
      )}

      <div className="crypto-list" role="grid">
        {displayedCryptos.map((crypto) => (
          <CryptoCard
            key={crypto.id}
            crypto={crypto}
            onSelect={onSelectCrypto}
            onDelete={onDeleteCrypto}
            onUpdate={onUpdateCrypto}
            disabled={loading}
          />
        ))}
      </div>

      {displayedCryptos.length > 0 && (
        <div className="list-footer">
          Total: <strong>{displayedCryptos.length}</strong> criptomoeda(s)
        </div>
      )}
    </div>
  );
}

export default CryptoList;
