import React, { useEffect, useState } from 'react';
import { useCrypto } from '../../context/CryptoContext';
import { getAllCryptos, getTopCryptos } from '../../services/apiService';
import CryptoCard from '../CryptoCard/CryptoCard';
import './CryptoList.css';

/**
 * Componente: CryptoList
 * Responsabilidade: Listar e exibir criptomoedas
 * SOLID - Single Responsibility: Apenas gerencia lista
 * Padrão: Composition - composto por CryptoCard
 */
function CryptoList({ onSelectCrypto }) {
  const { 
    state: { cryptos, searchResults, loading, error, filter },
    setCryptos,
    setLoading,
    setError
  } = useCrypto();
  
  const [viewMode, setViewMode] = useState('all'); // 'all' ou 'top'
  
  useEffect(() => {
    loadCryptos();
  }, []);
  
  const loadCryptos = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getAllCryptos();
      setCryptos(data);
    } catch (err) {
      setError(`Erro ao carregar criptomoedas: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };
  
  const loadTopCryptos = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await getTopCryptos();
      setCryptos(data);
    } catch (err) {
      setError(`Erro ao carregar top criptos: ${err.message}`);
    } finally {
      setLoading(false);
    }
  };
  
  // Definir quais criptomoedas exibir
  const displayedCryptos = searchResults.length > 0 ? searchResults : cryptos;
  
  const handleViewChange = async (mode) => {
    setViewMode(mode);
    if (mode === 'top') {
      await loadTopCryptos();
    } else {
      await loadCryptos();
    }
  };
  
  return (
    <div className="crypto-list-container">
      <div className="list-header">
        <h2>Criptomoedas</h2>
        <div className="view-controls">
          <button
            className={`view-btn ${viewMode === 'all' ? 'active' : ''}`}
            onClick={() => handleViewChange('all')}
          >
            Todas
          </button>
          <button
            className={`view-btn ${viewMode === 'top' ? 'active' : ''}`}
            onClick={() => handleViewChange('top')}
          >
            Top 10
          </button>
          <button
            className="view-btn refresh-btn"
            onClick={loadCryptos}
          >
            🔄 Recarregar
          </button>
        </div>
      </div>
      
      {error && (
        <div className="error-message">
          ⚠️ {error}
        </div>
      )}
      
      {loading && (
        <div className="loading">
          <div className="spinner"></div>
          <p>Carregando criptomoedas...</p>
        </div>
      )}
      
      {!loading && displayedCryptos.length === 0 && (
        <div className="empty-state">
          <p>📭 Nenhuma criptomoeda encontrada</p>
        </div>
      )}
      
      <div className="crypto-list">
        {displayedCryptos.map((crypto) => (
          <CryptoCard
            key={crypto.id}
            crypto={crypto}
            onSelect={onSelectCrypto}
          />
        ))}
      </div>
      
      {displayedCryptos.length > 0 && (
        <div className="list-footer">
          Total: {displayedCryptos.length} criptomoeda(s)
        </div>
      )}
    </div>
  );
}

export default CryptoList;
