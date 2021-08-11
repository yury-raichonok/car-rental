import styled from "styled-components";
import React from 'react';
import Button from '../../singleComponents/Button';
import Marginer from '../../../components/marginer/Marginer';
import Transmission from "@kiwicom/orbit-components/lib/icons/Transmission";
import AirConditioning from "@kiwicom/orbit-components/lib/icons/AirConditioning";
import Passenger from "@kiwicom/orbit-components/lib/icons/Passenger";
import BaggageChecked30 from "@kiwicom/orbit-components/lib/icons/BaggageChecked30";
import { SiBigcartel } from 'react-icons/si';
import 'antd/dist/antd.css';
import { Tooltip } from 'antd';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import noImage from '../../../images/no-image.png';

const CarBrandTitle = styled.div`
  height: 30px;
  font-size: 18px;
  font-weight: 600;
  color: #000;
  font-family: 'Poppins';
`;

const CardContainer = styled.div`
  display: flex;
  flex-direction: row;
  width: 400px;
  height: 200px;
  background-color: #fff;
  margin: 1em;
  margin-bottom: 1.3em;
  padding: 20px 12px;
  max-width: 800px;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.6);
  transition: all 200ms ease-in-out;

  &:hover {
    box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);
  }

  @media (max-width: 400px) { 
    width: 350px;
    height: 180px;
    margin: 0 0 2em 0;
  }

  @media (max-width: 350px) { 
    width: 300px;
  }

  @media (max-width: 300px) { 
    width: 250px;
  }
`;

const CarThumbnail = styled.div`
  width: 100%;
  height: 120px;
  justify-content: center;
  align-items: center;

  @media (max-width: 400px) { 
    height: 110px;
  }
`;

const ThumbnailContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 120px;

  @media (max-width: 400px) { 
    height: 110px;
  }
`;

const RightContainer = styled.div`
  width: 200px;
  justify-content: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 200px;
  font-size: 17px;
  color: rgba(0, 0, 0, 0.85);

  @media (max-width: 400px) { 
    font-size: 15px;
  }
`;

const LeftContainer = styled.div`
  width: 200px;
  display: flex;
  flex-direction: column;
  align-items: center;

  @media (max-width: 350px) { 
    margin-right: 8px;
  }
`;

const PriceContainer = styled.div`
  display: flex;    
  margin-bottom: 5px;
`;

const PriceText = styled.div`
    
`;

const CarOptionsContainer = styled.div`
  display: flex;
  margin-bottom: 8px;
`;

const CarCard = (props) => {

  const { t } = useTranslation();

  const { id, 
    brand,
    model,
    carClass,
    amountOfSeats, 
    amountOfBaggage, 
    autoTransmission, 
    airConditioner, 
    carImageLink, 
    costPerHour 
  } = props;

  const divStyle = {
    background: 'url(' + carImageLink + ')',
    backgroundPosition: 'center center',
    backgroundSize: 'cover',
  };

  const divStyleNoImage = {
    background: 'url(' + noImage + ')',
    backgroundPosition: 'center center',
    backgroundSize: '70%',
  };

  console.log(divStyle);

  return (
    <CardContainer>
      <LeftContainer>
        <CarBrandTitle>{brand} {model}</CarBrandTitle>
        <ThumbnailContainer>
          {carImageLink ? (
            <CarThumbnail style={divStyle}/>
          ) : (
            <CarThumbnail style={divStyleNoImage}/>
          )}
        </ThumbnailContainer>
      </LeftContainer>
      <RightContainer>
        <CarOptionsContainer>
          {autoTransmission ? 
            <Tooltip title={t('automatic_transmission')} color="#000000c2">
              <div><Transmission /> A</div>
            </Tooltip>
          :
            <Tooltip title={t('manual_transmission')} color="#000000c2">
              <div><Transmission /> M</div>
            </Tooltip>
          }
          <Marginer direction="horizontal" margin={10} />
          {airConditioner && 
            <Tooltip title={t('air_conditioner')} color="#000000c2">
              <div><AirConditioning /> AC</div>
            </Tooltip>
          }                   
        </CarOptionsContainer>
        <CarOptionsContainer>
          <Tooltip title={carClass} color="#000000c2">
            <div><SiBigcartel /> {t('class')}</div>
          </Tooltip>
          <Marginer direction="horizontal" margin={10} />
          <Tooltip title={`${amountOfSeats}` + ` ${t('adult_passengers')}`} color="#000000c2">
            <div><Passenger /> {amountOfSeats}</div>
          </Tooltip>
          <Marginer direction="horizontal" margin={10} />
          <Tooltip title={`${amountOfBaggage}` + ` ${t('large_bag')}`} color="#000000c2">
            <div><BaggageChecked30 /> {amountOfBaggage}</div>
          </Tooltip>
        </CarOptionsContainer>
        <PriceContainer>
          <PriceText>{"BYN " + costPerHour + ` /${t('hour')}`}</PriceText>
        </PriceContainer>
        <Link 
          className="link"
          to={{
            pathname: `/search/cars/${id}`
          }} 
        >
          <Button width="150" >{t('view_deal')}</Button>
        </Link>
      </RightContainer>
    </CardContainer>
  )
}

export default CarCard