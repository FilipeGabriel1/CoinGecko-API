import axios from 'axios';

/**
 * API Service - Padrão: Singleton (instância única)
 * Responsabilidade: Comunicar com backend via HTTP
 * SOLID - Dependency Inversion: Abstração de requisições HTTP
 */

const API_BASE_URL = 'http://localhost:8080/api/v1/cryptocurrencies';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
});

/**
 * Busca uma criptomoeda por ID
 * @param {string} id - ID da criptomoeda
 * @returns {Promise<Object>} Dados da criptomoeda
 */
export const getCryptoById = async (id) => {
  try {
    const response = await apiClient.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Erro ao buscar cripto ${id}:`, error);
    throw error;
  }
};

/**
 * Busca uma criptomoeda por símbolo
 * @param {string} symbol - Símbolo da criptomoeda
 * @returns {Promise<Object>} Dados da criptomoeda
 */
export const getCryptoBySymbol = async (symbol) => {
  try {
    const response = await apiClient.get(`/symbol/${symbol}`);
    return response.data;
  } catch (error) {
    console.error(`Erro ao buscar cripto ${symbol}:`, error);
    throw error;
  }
};

/**
 * Lista todas as criptomoedas
 * @returns {Promise<Array>} Lista de criptomoedas
 */
export const getAllCryptos = async () => {
  try {
    const response = await apiClient.get('/');
    return response.data;
  } catch (error) {
    console.error('Erro ao listar criptomoedas:', error);
    throw error;
  }
};

/**
 * Busca criptomoedas por símbolo (parcial)
 * @param {string} symbol - Símbolo para buscar
 * @returns {Promise<Array>} Lista de criptomoedas pré-filtrada
 */
export const searchCryptos = async (symbol) => {
  try {
    const response = await apiClient.get('/search', {
      params: { symbol }
    });
    return response.data;
  } catch (error) {
    console.error(`Erro ao buscar criptos com ${symbol}:`, error);
    throw error;
  }
};

/**
 * Obtém as top 10 criptomoedas
 * @returns {Promise<Array>} Top 10 criptomoedas
 */
export const getTopCryptos = async () => {
  try {
    const response = await apiClient.get('/top');
    return response.data;
  } catch (error) {
    console.error('Erro ao obter top criptos:', error);
    throw error;
  }
};

/**
 * Cria uma nova criptomoeda
 * @param {Object} crypto - Dados da criptomoeda
 * @returns {Promise<Object>} Criptomoeda criada
 */
export const createCrypto = async (crypto) => {
  try {
    const response = await apiClient.post('/', crypto);
    return response.data;
  } catch (error) {
    console.error('Erro ao criar cripto:', error);
    throw error;
  }
};

/**
 * Deleta uma criptomoeda
 * @param {string} id - ID da criptomoeda
 * @returns {Promise<void>}
 */
export const deleteCrypto = async (id) => {
  try {
    await apiClient.delete(`/${id}`);
  } catch (error) {
    console.error(`Erro ao deletar cripto ${id}:`, error);
    throw error;
  }
};

/**
 * Atualiza dados da criptomoeda de CoinGecko
 * @param {string} id - ID da criptomoeda
 * @returns {Promise<void>}
 */
export const updateFromCoinGecko = async (id) => {
  try {
    await apiClient.put(`/${id}/update`);
  } catch (error) {
    console.error(`Erro ao atualizar cripto ${id}:`, error);
    throw error;
  }
};

export default apiClient;
