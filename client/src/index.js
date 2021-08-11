import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import 'bootstrap/dist/css/bootstrap.min.css';
import { Provider } from 'react-redux';
import store from './redux/store';
import i18next from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';
import HttpApi from 'i18next-http-backend';
import { WaveTopBottomLoading } from 'react-loadingg';

i18next.use(initReactI18next)
.use(HttpApi)
.use(LanguageDetector)
.init({
  fallbackLng: 'en',
  supportedLngs: ['en', 'ru', 'be'],
  detection: {
    order: [ 'path', 'cookie', 'htmlTag', 'subdomain'],
    caches: ['cookie']
  },
  debug: true,
  backend: {
    loadPath: '/asserts/locales/{{lng}}/translation.json',
  },
});

const loadingMarkup = (
  <div className="py-4 text-center">
    <WaveTopBottomLoading />
  </div>
)

ReactDOM.render(
  <Suspense fallback={loadingMarkup}>
    <Provider store={store}>
      <App />
    </Provider>
  </Suspense>,
  
  document.getElementById('root')
);
reportWebVitals();
