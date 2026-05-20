import React from 'react';
import {
  formatUSD,
  formatEUR,
  formatBRL,
  formatPercentage,
  formatVolume,
} from '../../utils/formatters';
import './CryptoCard.css';

function CryptoCard({ crypto, onSelect, onDelete, onUpdate, disabled }) {
  const price = crypto.price || crypto.currentPrice || {};
  const change = price.priceChangePercentage24h ?? 0;
  const isPositive = change >= 0;

  return (
    <article
      className="crypto-card"
      onClick={() => onSelect?.(crypto)}
      role="article"
      aria-label={`Criptomoeda: ${crypto.name}`}
    >
      <div className="crypto-header">
        {crypto.imageUrl && (
          <img
            src={crypto.imageUrl}
            alt={crypto.name}
            className="crypto-image"
            loading="lazy"
          />
        )}
        <div className="crypto-details">
          <h3 className="crypto-name">{crypto.name}</h3>
          <p className="crypto-symbol">{(crypto.symbol || '').toUpperCase()}</p>
          {crypto.marketCapRank && (
            <span className="crypto-rank">#{crypto.marketCapRank}</span>
          )}
        </div>
      </div>

      <div className="crypto-prices">
        <div className="price-item">
          <span className="price-label">USD</span>
          <span className="price-value">{formatUSD(price.priceUsd)}</span>
        </div>
        <div className="price-item">
          <span className="price-label">EUR</span>
          <span className="price-value">{formatEUR(price.priceEur)}</span>
        </div>
        <div className="price-item">
          <span className="price-label">BRL</span>
          <span className="price-value">{formatBRL(price.priceBrl)}</span>
        </div>
      </div>

      <div className="crypto-stats">
        <span className={`change-percentage ${isPositive ? 'positive' : 'negative'}`}>
          {formatPercentage(change)}
        </span>
        {price.volume24hUsd && (
          <span className="stat-item">Vol 24h: {formatVolume(price.volume24hUsd)}</span>
        )}
        {price.marketCap && (
          <span className="stat-item">Market Cap: {formatVolume(price.marketCap)}</span>
        )}
      </div>

      <div className="crypto-actions">
        <button
          className="btn btn-update"
          onClick={(event) => {
            event.stopPropagation();
            onUpdate?.(crypto);
          }}
          disabled={disabled}
          aria-label="Atualizar de CoinGecko"
          title="Atualizar"
        >
          🔄
        </button>
        <button
          className="btn btn-delete"
          onClick={(event) => {
            event.stopPropagation();
            onDelete?.(crypto);
          }}
          aria-label="Deletar criptomoeda"
          title="Deletar"
        >
          <span aria-hidden="true">🗑️</span>
        </button>
      </div>
    </article>
  );
}

export default CryptoCard;
