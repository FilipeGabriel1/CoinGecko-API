import React, { useState } from 'react';
import { useCrypto } from '../../context/CryptoContext';
import { deleteCrypto, updateFromCoinGecko } from '../../services/apiService';
import './CryptoCard.css';

/**
 * Componente: CryptoCard
 * Responsabilidade: Exibir informações de uma criptomoeda
 * SOLID - Single Responsibility: Apenas renderiza um card
 * Props: crypto (objeto com dados de criptomoeda)
 */
function CryptoCard({ crypto, onSelect }) {
  const { removeCrypto, updateCrypto, setLoading, setError } = useCrypto();
  const [isUpdating, setIsUpdating] = useState(false);
  
  const handleDelete = async () => {
    if (window.confirm(`Deletar ${crypto.name}?`)) {
      try {
        setLoading(true);
        await deleteCrypto(crypto.id);
        removeCrypto(crypto.id);
        setError(null);
      } catch (err) {
        setError(`Erro ao deletar: ${err.message}`);
      } finally {
        setLoading(false);
      }
    }
  };
  
  const handleUpdate = async () => {
    try {
      setIsUpdating(true);
      setError(null);
      await updateFromCoinGecko(crypto.id);
      // Recarregar dados (na prática, fazer uma nova requisição)
      setError("Cripto atualizada com sucesso!");
    } catch (err) {
      setError(`Erro ao atualizar: ${err.message}`);
    } finally {
      setIsUpdating(false);
    }
  };
  
  const priceChange = crypto.price?.priceChangePercentage24h || 0;
  const isPositive = priceChange >= 0;
  
  return (
    <div className="crypto-card" onClick={() => onSelect && onSelect(crypto)}>
      <div className="crypto-header">
        <div className="crypto-info">
          {crypto.imageUrl && (
            <img src={crypto.imageUrl} alt={crypto.name} className="crypto-image" />
          )}
          <div className="crypto-details">
            <h3 className="crypto-name">{crypto.name}</h3>
            <p className="crypto-symbol">{crypto.symbol?.toUpperCase()}</p>
            {crypto.marketCapRank && (
              <p className="crypto-rank">#{crypto.marketCapRank}</p>
            )}
          </div>
        </div>
        <div className="crypto-rank-badge">
          {crypto.marketCapRank && `#${crypto.marketCapRank}`}
        </div>
      </div>
      
      {crypto.price && (
        <div className="crypto-prices">
          <div className="price-item">
            <span className="price-label">USD</span>
            <span className="price-value">
              ${crypto.price.priceUsd?.toFixed(2)}
            </span>
          </div>
          <div className="price-item">
            <span className="price-label">EUR</span>
            <span className="price-value">
              €{crypto.price.priceEur?.toFixed(2)}
            </span>
          </div>
          <div className="price-item">
            <span className="price-label">BRL</span>
            <span className="price-value">
              R${crypto.price.priceBrl?.toFixed(2)}
            </span>
          </div>
        </div>
      )}
      
      <div className="crypto-change">
        <span className={`change-percentage ${isPositive ? 'positive' : 'negative'}`}>
          {isPositive ? '▲' : '▼'} {Math.abs(priceChange).toFixed(2)}%
        </span>
      </div>
      
      {crypto.price?.volume24hUsd && (
        <div className="crypto-volume">
          <span className="volume-label">Vol 24h:</span>
          <span className="volume-value">
            ${(crypto.price.volume24hUsd / 1000000).toFixed(2)}M
          </span>
        </div>
      )}
      
      <div className="crypto-actions">
        <button
          className="btn btn-update"
          onClick={(e) => {
            e.stopPropagation();
            handleUpdate();
          }}
          disabled={isUpdating}
        >
          {isUpdating ? 'Atualizando...' : '🔄'}
        </button>
        <button
          className="btn btn-delete"
          onClick={(e) => {
            e.stopPropagation();
            handleDelete();
          }}
        >
          🗑️
        </button>
      </div>
    </div>
  );
}

export default CryptoCard;
