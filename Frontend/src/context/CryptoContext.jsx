import React, { createContext, useContext, useReducer, useCallback } from 'react';

/**
 * CryptoContext
 * Padrão: Context Pattern (similar a Singleton global + Observer)
 * Padrão: Reducer Pattern (gerenciamento de estado)
 * Responsabilidade: Gerenciar estado global de criptomoedas
 * SOLID - Single Responsibility: Apenas gerencia estado global
 */

const CryptoContext = createContext();

const initialState = {
  cryptos: [],
  searchResults: [],
  selectedCrypto: null,
  loading: false,
  error: null,
  filter: '',
  sortBy: 'marketCapRank'
};

/**
 * Reducer: gerencia transições de estado
 * Padrão: Reducer Pattern - previsível e testável
 */
function cryptoReducer(state, action) {
  switch (action.type) {
    case 'SET_CRYPTOS':
      return {
        ...state,
        cryptos: action.payload,
        error: null
      };
    
    case 'SET_SEARCH_RESULTS':
      return {
        ...state,
        searchResults: action.payload,
        error: null
      };
    
    case 'SET_SELECTED_CRYPTO':
      return {
        ...state,
        selectedCrypto: action.payload
      };
    
    case 'SET_LOADING':
      return {
        ...state,
        loading: action.payload
      };
    
    case 'SET_ERROR':
      return {
        ...state,
        error: action.payload
      };
    
    case 'SET_FILTER':
      return {
        ...state,
        filter: action.payload
      };
    
    case 'SET_SORT_BY':
      return {
        ...state,
        sortBy: action.payload
      };
    
    case 'ADD_CRYPTO':
      return {
        ...state,
        cryptos: [...state.cryptos, action.payload],
        error: null
      };
    
    case 'REMOVE_CRYPTO':
      return {
        ...state,
        cryptos: state.cryptos.filter(c => c.id !== action.payload),
        error: null
      };
    
    case 'UPDATE_CRYPTO':
      return {
        ...state,
        cryptos: state.cryptos.map(c => 
          c.id === action.payload.id ? action.payload : c
        ),
        error: null
      };
    
    default:
      return state;
  }
}

/**
 * Provider: fornece contexto para toda a aplicação
 */
export function CryptoProvider({ children }) {
  const [state, dispatch] = useReducer(cryptoReducer, initialState);
  
  // Ações encapsuladas
  const setCryptos = useCallback((cryptos) => {
    dispatch({ type: 'SET_CRYPTOS', payload: cryptos });
  }, []);
  
  const setSearchResults = useCallback((results) => {
    dispatch({ type: 'SET_SEARCH_RESULTS', payload: results });
  }, []);
  
  const setSelectedCrypto = useCallback((crypto) => {
    dispatch({ type: 'SET_SELECTED_CRYPTO', payload: crypto });
  }, []);
  
  const setLoading = useCallback((loading) => {
    dispatch({ type: 'SET_LOADING', payload: loading });
  }, []);
  
  const setError = useCallback((error) => {
    dispatch({ type: 'SET_ERROR', payload: error });
  }, []);
  
  const setFilter = useCallback((filter) => {
    dispatch({ type: 'SET_FILTER', payload: filter });
  }, []);
  
  const setSortBy = useCallback((sortBy) => {
    dispatch({ type: 'SET_SORT_BY', payload: sortBy });
  }, []);
  
  const addCrypto = useCallback((crypto) => {
    dispatch({ type: 'ADD_CRYPTO', payload: crypto });
  }, []);
  
  const removeCrypto = useCallback((id) => {
    dispatch({ type: 'REMOVE_CRYPTO', payload: id });
  }, []);
  
  const updateCrypto = useCallback((crypto) => {
    dispatch({ type: 'UPDATE_CRYPTO', payload: crypto });
  }, []);
  
  const value = {
    state,
    setCryptos,
    setSearchResults,
    setSelectedCrypto,
    setLoading,
    setError,
    setFilter,
    setSortBy,
    addCrypto,
    removeCrypto,
    updateCrypto
  };
  
  return (
    <CryptoContext.Provider value={value}>
      {children}
    </CryptoContext.Provider>
  );
}

/**
 * Hook customizado para usar o contexto
 * SOLID - Dependency Injection: simplifica acesso ao contexto
 */
export function useCrypto() {
  const context = useContext(CryptoContext);
  if (!context) {
    throw new Error('useCrypto deve ser usado dentro de CryptoProvider');
  }
  return context;
}
