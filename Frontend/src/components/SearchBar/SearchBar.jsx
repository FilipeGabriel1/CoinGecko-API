import React, { useState, useCallback } from 'react';
import { useCrypto } from '../../context/CryptoContext';
import { searchCryptos } from '../../services/apiService';
import './SearchBar.css';

/**
 * Componente: SearchBar
 * Responsabilidade: Permitir busca de criptomoedas
 * SOLID - Single Responsibility: Apenas gerencia busca
 */
function SearchBar() {
  const [searchTerm, setSearchTerm] = useState('');
  const { setSearchResults, setLoading, setError } = useCrypto();
  
  const handleSearch = useCallback(async (e) => {
    const term = e.target.value;
    setSearchTerm(term);
    
    if (term.trim() === '') {
      setSearchResults([]);
      return;
    }
    
    try {
      setLoading(true);
      setError(null);
      const results = await searchCryptos(term);
      setSearchResults(results);
    } catch (error) {
      setError(`Erro ao buscar: ${error.message}`);
      setSearchResults([]);
    } finally {
      setLoading(false);
    }
  }, [setSearchResults, setLoading, setError]);
  
  return (
    <div className="search-bar">
      <div className="search-container">
        <input
          type="text"
          placeholder="Buscar criptomoeda (ex: Bitcoin, BTC)..."
          value={searchTerm}
          onChange={handleSearch}
          className="search-input"
        />
        <span className="search-icon">🔍</span>
      </div>
    </div>
  );
}

export default SearchBar;
