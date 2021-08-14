import styled from "styled-components";
import { Link } from 'react-router-dom';
import 'antd/dist/antd.css';
import { SiBigcartel } from 'react-icons/si';
import Transmission from "@kiwicom/orbit-components/lib/icons/Transmission";
import AirConditioning from "@kiwicom/orbit-components/lib/icons/AirConditioning";
import Passenger from "@kiwicom/orbit-components/lib/icons/Passenger";
import BaggageChecked30 from "@kiwicom/orbit-components/lib/icons/BaggageChecked30";
import Location from "@kiwicom/orbit-components/lib/icons/Location";
import InformationCircle from "@kiwicom/orbit-components/lib/icons/InformationCircle";
import { Tooltip, Popover } from 'antd';
import Button from '../../singleComponents/Button';
import { useTranslation } from 'react-i18next';
import noImage from '../../../images/no-image.png';

const TitleContainer = styled.div`
  display: flex;
  justify-content: space-between;
  margin: 20px 20px 0 20px;
`;

const CarTitle = styled.div`
  height: 30px;
  font-size: 20px;
  font-weight: 600;
  color: #000;
  font-family: 'Poppins'; 
  
  @media (max-width: 450px) {
    font-size: 16px;
  }
`;

const TextContainer = styled.p`
  font-size: 14px;
  color: #000;
  margin: 0 20px 10px 20px;
  text-align: left;
`;

const CardContainer = styled.div`
  display: flex;
  flex-direction: row;
  max-width: 100%;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.6);
  transition: all 200ms ease-in-out;
  margin-bottom: 20px;

  &:hover {
    box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);
  }

  @media (max-width: 1260px) { 
    width: 860px;
  }

  @media (max-width: 951px) {
    width: 90%;
  }

  @media (max-width: 850px) {
    border-left: none;
    flex-direction: column;
    width: 70%;
  }

  @media (max-width: 640px) {
    width: 95%;
  }
`;

const ImageContainer = styled.div`
  width: 250px;
  display: flex;
  flex-direction: column;
  align-items: center;

  @media (max-width: 951px) {
    width: 100%;
  }

  @media (max-width: 850px) {
    height: 250px;
  }

  @media (max-width: 450px) {
    height: 200px;
  }
`;

const CarCharacteristicContainer = styled.div`
  width: calc(100% - 250px);
  display: flex;
  flex-direction: column;

  @media (max-width: 951px) {
    flex-direction: column;
    width: 100%;
  }
`;

const RowCharacteristic = styled.div`
  margin: 5px 15px 5px 0;
  border-radius: 10px;
  align-items: center;
  cursor: pointer;
  font-size: 16px;
`;

const RowContainer = styled.div`
  display: flex;
  margin: 0 20px 10px 20px;  
`;

const HorizontalLine = styled.div`
  width: 100%;
  border-bottom: 1px solid #ccc7c7;
`;

const PriceContainer = styled.div`
  height: 30px;
  font-size: 20px;
  font-weight: 700;
  color: #000;
  margin-bottom: 40px;

  @media (max-width: 951px) {
    margin-bottom: 10px;
  }

  @media (max-width: 450px) {
    font-size: 16px;
    margin-bottom: 10px;
  }
`;

const LocationContainer = styled.div`
  padding: 5px 0;
  border-radius: 10px;
`;

const DealContainer = styled.div`
  width: 250px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-left: 1px solid #ccc7c7;
  justify-content: center;

  @media (max-width: 951px) {
    width: 100%;
    border-left: none;
    padding-bottom: 10px;
    flex-direction: row;
    justify-content: space-around;
  }

  @media (max-width: 450px) {
    flex-direction: column;
  }
`;

const CarInfoContainer = styled.div`
  display: flex;
  flex-direction: column;
  margin: 0;
  padding: 0;

  p {
    text-align: left;
    margin: 0;
    padding: 0;
  }
`;

const DataContainer = styled.div`
  width: calc(100% - 250px);
  display: flex;

  @media (max-width: 951px) {
    flex-direction: column;
    width: 100%;
    border-left: 1px solid #ccc7c7;
  }

  @media (max-width: 850px) {
    border-left: none;
  }
`;

const CarCard = (props) => {

  const { t } = useTranslation();

  const { id,
    brand,
    model,
    carClass,
    yearOfIssue,
    bodyType,
    transmissionType,
    color,
    engineType,
    passengersAmt,
    baggageAmt,
    hasConditioner,
    costPerHour,
    locationName,
    carImageLink,
    pickUpDateTime,
    returnDateTime
  } = props;

  const carInfo = (
    <CarInfoContainer>
      <p>{t('year_of_issue')}: {yearOfIssue}</p>
      <p>
        {t('body_type')}: 
        {bodyType === "Hatchback" && ` ${t('hatchback')}`}
        {bodyType === "Sedan" && ` ${t('sedan')}`}
        {bodyType === "MUV/SUV" && ` ${t('muv_suv')}`}
        {bodyType === "Coupe" && ` ${t('coupe')}`}
        {bodyType === "Convertible" && ` ${t('convertible')}`}
        {bodyType === "Wagon" && ` ${t('wagon')}`}
        {bodyType === "Van" && ` ${t('van')}`}
        {bodyType === "Jeep" && ` ${t('jeep')}`}
      </p>
      <p>
        {t('color')}: 
        {color === "Red" && ` ${t('red')}`}
        {color === "White" && ` ${t('white')}`}
        {color === "Black" && ` ${t('black')}`}
        {color === "Green" && ` ${t('green')}`}
        {color === "Grey" && ` ${t('grey')}`}
        {color === "Yellow" && ` ${t('yellow')}`}
        {color === "Blue" && ` ${t('blue')}`}
        {color === "Silver" && ` ${t('silver')}`}
        {color === "Pink" && ` ${t('pink')}`}
        {color === "Orange" && ` ${t('orange')}`}
        {color === "Brown" && ` ${t('brown')}`}
        {color === "Violet" && ` ${t('violet')}`}
        {color === "Gold" && ` ${t('gold')}`}
        {color === "Sand" && ` ${t('sand')}`}
        {color !== "Red" && 
          color !== "White" && 
          color !== "Black" && 
          color !== "Green" && 
          color !== "Grey" && 
          color !== "Yellow" && 
          color !== "Blue" && 
          color !== "Silver" && 
          color !== "Pink" && 
          color !== "Orange" && 
          color !== "Brown" && 
          color !== "Violet" && 
          color !== "Gold" && 
          color !== "Sand" && ` ${color}`}
      </p>
      <p>
        {t('engine_type')}: 
        {engineType === "Diesel" && ` ${t('diesel')}`}
        {engineType === "Petrol" && ` ${t('petrol')}`}
        {engineType === "Hybrid" && ` ${t('hybrid')}`}
        {engineType === "Electro" && ` ${t('electro')}`}
      </p>
    </CarInfoContainer>
  );

  const divStyle = {
    background: 'url(' + carImageLink + ')',
    backgroundPosition: 'center center',
    backgroundSize: 'cover',
  };

  const divStyleNoImage = {
    background: 'url(' + noImage + ')',
    backgroundPosition: 'center center',
    backgroundSize: '50%',
  };

  return (
    <CardContainer>
      { carImageLink ? (
        <ImageContainer style={divStyle} />
      ) : (
        <ImageContainer style={divStyleNoImage} />
      )}
      <DataContainer>
        <CarCharacteristicContainer>
          <TitleContainer>
            <CarTitle>{brand} {model}</CarTitle>
            <Popover placement="leftTop" content={carInfo} title={t('car_info')}>
              <div><InformationCircle /></div>
            </Popover>       
          </TitleContainer>        
          <TextContainer>{t('or_similar')}{carClass}</TextContainer>
          <RowContainer>
            <Tooltip title={carClass} color="#000000c2">
              <RowCharacteristic><SiBigcartel /> {t('car_class')}</RowCharacteristic>
            </Tooltip>
            <Tooltip title={`${passengersAmt}` + ` ${t('adult_passengers')}`} color="#000000c2">
              <RowCharacteristic><Passenger /> {passengersAmt}</RowCharacteristic>
            </Tooltip>
            <Tooltip title={`${baggageAmt}` + ` ${t('large_bag')}`} color="#000000c2">
              <RowCharacteristic><BaggageChecked30 /> {baggageAmt}</RowCharacteristic>
            </Tooltip>
            {transmissionType ? 
              <Tooltip title={t('automatic_transmission')} color="#000000c2">
                <RowCharacteristic><Transmission /> A</RowCharacteristic>
              </Tooltip>
            :
              <Tooltip title={t('manual_transmission')} color="#000000c2">
                <RowCharacteristic><Transmission /> M</RowCharacteristic>
              </Tooltip>
            }
            {hasConditioner && 
              <Tooltip title={t('air_conditioner')} color="#000000c2">
                <RowCharacteristic><AirConditioning /> AC</RowCharacteristic>
              </Tooltip>
            }
          </RowContainer>
          <RowContainer>
            <HorizontalLine/>
          </RowContainer>
          <RowContainer>          
            <LocationContainer><Location />&nbsp;{locationName}</LocationContainer>
          </RowContainer>
        </CarCharacteristicContainer>
        <DealContainer>
          <PriceContainer>BYN {costPerHour} /{t('hour')}</PriceContainer>
          <Link 
            className="link"
            to={{
              pathname: `/search/cars/${id}`,
              pickUpDateTime,
              returnDateTime
            }} 
          >
            <Button width="150" >{t('view_deal')}</Button>
          </Link>
        </DealContainer>
      </DataContainer>
    </CardContainer>
  )
}

export default CarCard
