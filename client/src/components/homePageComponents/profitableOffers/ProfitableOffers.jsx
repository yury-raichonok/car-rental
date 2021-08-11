import { useState, useEffect } from 'react';
import CarCard from '../../cards/carCard/CarCard';
import styled from 'styled-components';
import cookies from 'js-cookie';
import { useTranslation } from 'react-i18next';
import CarDataService from '../../../services/car/CarDataService';
import { Spin } from 'antd';

const ComponentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

const OffersContainer = styled.div`
  width: 1400px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
    
  @media (max-width: 1417px) { 
    width: 100%;
  }
`;

const Title = styled.h1`
  font-size: 32px;
  font-weight: 700;
  color: #000;
  margin-bottom: 50px;
`;

const OffersWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
`;

const WarningText = styled.h3`
  color: rgba(100, 100, 100);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const languages = [
  {
    code: 'be',
    name: 'BY',
    country_code: 'by',
  },
  {
    code: 'ru',
    name: 'RU',
    country_code: 'ru',
  },
  {
    code: 'en',
    name: 'EN',
    country_code: 'gb',
  }
]

const ProfitableOffers = () => {  

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const isDataEmpty = !data || (data && data.length === 0);

  const fetchOffers = async () => {
    setLoading(true);
    const res = await CarDataService.getProfitableOffers().catch((err) => {
      console.log(err.response)
    });

    if (res) {
      setData(res.data);
    }

    setLoading(false);
  };

  useEffect(() => {
    fetchOffers();
  }, [currentLanguage]);

  return (
    <ComponentContainer>
      <OffersContainer>
        <Title>{t('profitable_offers')}</Title>
        <OffersWrapper>
          {isDataEmpty && !loading && (<WarningText>{t('no_offers_published')}</WarningText>)}
          { loading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
          {!isDataEmpty && !loading && data.map((cars) => (
            <CarCard key={cars.id} {...cars} />
          ))}
        </OffersWrapper>
      </OffersContainer>
    </ComponentContainer>
    
  )
}

export default ProfitableOffers
