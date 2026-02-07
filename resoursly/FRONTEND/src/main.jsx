import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { Provider } from 'react-redux'
import ProfileCard from './components/ProfileCard.jsx'
import store from './store/store.js'
import BudgetFinancial from './components/project/BudgetFinancial.jsx'
import UserProfileCard from './components/UserProfileCard.jsx'
import TabView from './components/TabView.jsx'
createRoot(document.getElementById('root')).render(
  <>
    
    <Provider store={store}>
    <App />
    </Provider>
    {/* <ProfileCard/> */}
    
  </>
)
