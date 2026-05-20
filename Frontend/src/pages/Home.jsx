import React, { useState, useEffect, useCallback } from 'react';
import CryptoList from '../components/CryptoList/CryptoList';
import {
  getAllCryptos,
  getTopCryptos,
  searchCryptos,
  deleteCrypto,
  updateFromCoinGecko,
} from '../services/apiService';
import { useDebounce } from '../hooks/useDebounce';
import { DEBOUNCE_DELAY, MESSAGES } from '../constants/app.constants';
import '../styles/Home.css';

function Home() {
  const [cryptos, setCryptos] = useState([]);
  const [searchResults, setSearchResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [viewMode, setViewMode] = useState('all');
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCrypto, setSelectedCrypto] = useState(null);

  const debouncedSearchTerm = useDebounce(searchTerm, DEBOUNCE_DELAY);

  const loadCryptos = useCallback(
    async (mode = 'all') => {
      setLoading(true);
      setError(null);
      try {
        const data = mode === 'top' ? await getTopCryptos() : await getAllCryptos();
        setCryptos(data || []);
      } catch (err) {
        setError(`${MESSAGES.ERROR_LOADING}: ${err.message}`);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const handleSearch = useCallback(
    async (term) => {
      if (!term.trim()) {
        setSearchResults([]);
        setError(null);
        return;
      }

      setLoading(true);
      setError(null);
      try {
        const results = await searchCryptos(term);
        setSearchResults(results || []);
      } catch (err) {
        setError(`Erro ao buscar: ${err.message}`);
        setSearchResults([]);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  useEffect(() => {
    loadCryptos(viewMode);
  }, [loadCryptos, viewMode]);

  useEffect(() => {
    handleSearch(debouncedSearchTerm);
  }, [debouncedSearchTerm, handleSearch]);

  const handleViewChange = useCallback(
    (mode) => {
      setViewMode(mode);
      setSearchTerm('');
      setSearchResults([]);
    },
    []
  );

  const handleRefresh = useCallback(() => {
    loadCryptos(viewMode);
  }, [loadCryptos, viewMode]);

  const handleDelete = useCallback(
    async (crypto) => {
      if (!window.confirm(MESSAGES.CONFIRM_DELETE(crypto.name))) {
        return;
      }

      setLoading(true);
      setError(null);
      try {
        await deleteCrypto(crypto.id);
        setCryptos((prev) => prev.filter((item) => item.id !== crypto.id));
        setSearchResults((prev) => prev.filter((item) => item.id !== crypto.id));
      } catch (err) {
        setError(`${MESSAGES.ERROR_DELETE}: ${err.message}`);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const handleUpdate = useCallback(
    async (crypto) => {
      setLoading(true);
      setError(null);
      try {
        await updateFromCoinGecko(crypto.id);
        setError(MESSAGES.SUCCESS_UPDATE);
      } catch (err) {
        setError(`${MESSAGES.ERROR_UPDATE}: ${err.message}`);
      } finally {
        setLoading(false);
      }
    },
    []
  );

  const displayedCryptos = searchResults.length > 0 ? searchResults : cryptos;

  return (
    <div className="home-page">
      <header className="page-header">
        <div className="header-content">
          <h1 className="app-title">💰 CoinGecko API</h1>
          <p className="app-subtitle">
            Acompanhe os preços e informações das principais criptomoedas
          </p>
        </div>
      </header>

      <main className="page-main">
        <div className="container">
          {selectedCrypto && (
            <div className="selected-crypto-info">
              <button
                className="close-btn"
                onClick={() => setSelectedCrypto(null)}
              >
                ✕
              </button>
              <h2>{selectedCrypto.name}</h2>
              {selectedCrypto.description && <p>{selectedCrypto.description}</p>}
            </div>
          )}

          <CryptoList
            cryptos={cryptos}
            searchResults={searchResults}
            loading={loading}
            error={error}
            viewMode={viewMode}
            searchTerm={searchTerm}
            onSearchChange={setSearchTerm}
            onSearchClear={() => {
              setSearchTerm('');
              setSearchResults([]);
              setError(null);
            }}
            onSelectCrypto={(crypto) => {
              setSelectedCrypto(crypto);
              window.scrollTo({ top: 0, behavior: 'smooth' });
            }}
            onViewChange={handleViewChange}
            onRefresh={handleRefresh}
            onDeleteCrypto={handleDelete}
            onUpdateCrypto={handleUpdate}
          />
        </div>
      </main>

      <footer className="page-footer">
        <p>Desenvolvido com ❤️ | CoinGecko API</p>
        <p className="footer-note">Dados fornecidos por CoinGecko API</p>
      </footer>
    </div>
  );
}

export default Home;
