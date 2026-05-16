import React, { useState } from 'react';
import SearchBar from '../components/SearchBar/SearchBar';
import CryptoList from '../components/CryptoList/CryptoList';
import { useCrypto } from '../context/CryptoContext';
import '../styles/Home.css';

/**
 * Página: Home
 * Responsabilidade: Integrar todos os componentes
 * SOLID - Single Responsibility: Apenas gerencia layout da página
 * Padrão: Container Component Pattern
 */
function Home() {
  const [selectedCrypto, setSelectedCrypto] = useState(null);
  const { state: { error } } = useCrypto();
  
  const handleSelectCrypto = (crypto) => {
    setSelectedCrypto(crypto);
    // Scroll para detalhes se necessário
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };
  
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
          <SearchBar />
          
          {selectedCrypto && (
            <div className="selected-crypto-info">
              <button
                className="close-btn"
                onClick={() => setSelectedCrypto(null)}
              >
                ✕
              </button>
              <h2>{selectedCrypto.name}</h2>
              {selectedCrypto.description && (
                <p>{selectedCrypto.description}</p>
              )}
            </div>
          )}
          
          <CryptoList onSelectCrypto={handleSelectCrypto} />
        </div>
      </main>
      
      <footer className="page-footer">
        <p>
          Desenvolvido com ❤️ | Padrões de Projeto + SOLID Principles
        </p>
        <p className="footer-note">
          Dados fornecidos por CoinGecko API
        </p>
      </footer>
    </div>
  );
}

export default Home;
