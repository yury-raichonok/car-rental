import { Link } from 'react-router-dom';
import styled from 'styled-components';
import BrandCard from '../../components/cards/brandCard/BrandCard';
import useSWR from 'swr';
import { useTranslation } from 'react-i18next';

const ComponentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

const BrandsContainer = styled.div`
  margin-top: 71px;
  width: 1400px;
  display: flex;
  flex-direction: column;
  justify-content: center;

  @media (max-width: 1417px) { 
    width: 100%;
  }

  @media (max-width: 991px) { 
    margin-top: 20px;
  }
`;

const Title = styled.h1`
  font-size: 32px;
  font-weight: 700;
  color: #000;

  @media (max-width: 400px) { 
    font-size: 28px;
  }
`;

const BrandsWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: center;

  .link {
    margin: 1em;
    margin-bottom: 1.3em;
  }

  .link:hover {
    text-decoration: none;
  }
`;

const WarningText = styled.h3`
  color: rgba(100, 100, 100);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const BrandsComponent = () => {

  const { t } = useTranslation();

  const { data } = useSWR("http://localhost:8080/brands");

  const isDataEmpty = !data || (data && data.length === 0);

  return (
    <ComponentContainer>
      <BrandsContainer>
        <Title>{t('browse_rental_cars_by_brand')}</Title>
        <BrandsWrapper>        
          {isDataEmpty && (<WarningText>{t('no_brands_are_published_yet')}</WarningText>)}
          {!isDataEmpty && data?.map(row => (
             <Link
              key={row.id}
              className="link"
              to={{
                pathname: "/search",
                state: { brand: `${row.name}` }
              }}
           >
             <BrandCard key={row.id} {...row} />
           </Link>
          ))}
        </BrandsWrapper>
      </BrandsContainer>
    </ComponentContainer>    
  )
}

export default BrandsComponent
