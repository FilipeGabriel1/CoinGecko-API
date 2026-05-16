import { CryptoProvider } from './context/CryptoContext'
import Home from './pages/Home'

/**
 * Componente principal da aplicação
 * Responsabilidade: Ser raiz da aplicação com providers
 * Padrão: Provider Pattern
 */
function App() {
  return (
    <CryptoProvider>
      <Home />
    </CryptoProvider>
  )
}

export default App
