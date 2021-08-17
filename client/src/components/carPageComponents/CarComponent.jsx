import { useState, useEffect } from 'react';
import { Redirect } from 'react-router-dom';
import styled from 'styled-components';
import CarDataService from '../../services/car/CarDataService';
import { Image, Tabs,  Spin } from 'antd';
import { Table } from 'react-bootstrap';
import { YMaps, Map, Placemark } from "react-yandex-maps";
import Location from "@kiwicom/orbit-components/lib/icons/Location";
import CarRental from "@kiwicom/orbit-components/lib/icons/CarRental";
import OrderComponent from './OrderComponent';
import Settings from "@kiwicom/orbit-components/lib/icons/Settings";
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';
import cookies from 'js-cookie';
import noImage from '../../images/no-image.png';

const { TabPane } = Tabs;

const ComponentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;

  @media (max-width: 800px) { 
    flex-direction: column;
  }
`;

const InfofmationContainer = styled.div`
  margin-top: 71px;
  margin-bottom: 20px;
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

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;

  @media (max-width: 1000px) { 
    flex-direction: column;
  }
`;

const MinRow = styled.div`
  width: 50%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;

  .inner-row {
    width: 100%;
  }

  @media (max-width: 1000px) {
    width: 100%;
  }
`;

const ImagesContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;

  .ant-image {
    img {
      width: 100%;
      max-height: 400px;
      object-fit: scale-down;
    }
    
  }
`;

const OptionsContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  padding: 0 20px;
`;

const OrderContainer = styled.div`
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  padding: 0 20px;
`;

const OptionsTableBody = styled.tbody`
  tr {
    td {
      padding: 10px;
    }
  }
`;

const LeftColumnContainer = styled.td`
  text-align: left;
`;

const RightColumnContainer = styled.td`
  text-align: right;
`;

const WarningText = styled.div`
  margin-top: 81px;
  color: #000;
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const ComponentWrapper = styled.div`
  width: 100%;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.6);
  padding: 10px;

  .ant-tabs-tab {
    width: 120px;
    display: flex;
    justify-content: center;

    span {
      display: flex;
      align-items: center;
    }
  }

  .ant-tabs-tab:hover {
    color: #f44336;
  }

  .ant-tabs-tab-btn:focus{
    color: #f44336;
  }

  .ant-tabs-tab.ant-tabs-tab-active .ant-tabs-tab-btn {
    color: #f44336;
  }

  .ant-tabs-content-holder {
    min-height: 360px;
  }

  .ant-tabs-ink-bar {
    background: #f44336;
  }
`;

const ImageComponentWrapper = styled.div`
  width: 100%;
  border-radius: 10px;
  padding: 10px;
  align-items: center;
`;

const AlertContainer = styled.div`
  width: 100%;

  span {
    color: red;
    font-weigth: 500;
  }
`;

const TopContainer = styled.div`
  width: 100%;
  font-weight: 500;
  font-size: 18px;
`;

const PricingContainer = styled.div`
  width: 100%;
  height: 214px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
`;

const Title = styled.div`
  margin-bottom: 10px;
  font-size: 32px;
  font-weight: 700;
  color: #000;

  @media (max-width: 400px) { 
    font-size: 28px;
  }
`;

const MapContainer = styled.div`
  height: 100%;
  display: flex;
  flex-direction: column;

  .location-title {
    justify-content: flex-start;

    @media (max-width: 1000px) { 
      flex-direction: row;
    }
  }
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

const CarComponent = (props) => {

  const { t } = useTranslation();

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const [data, setData] = useState();
  const [isLoading, setLoading] = useState(false);
  const [isRedirect, setIsRedirect] = useState(false);
  const [mapData, setMapData] = useState({
    center: [],
    zoom: 15,
  })

  const fetchData = async () => {
    setLoading(true);
    const resp = await CarDataService.getCarById(props.match.params.id).catch((error) => {
      console.log("Error: ", error);
      if (error && error.response) {
        switch (error.response.status) {
          case 500:
            setIsRedirect(true)
            break;
        
          default:
            break;
        }
      }
    });
  
    if(resp) {
      setData(resp.data);
      setMapData({
        center: [resp.data.locationCoordinateX, resp.data.locationCoordinateY],
        zoom: 15
      })
    }

    setLoading(false);
  }

  useEffect(() => {
    fetchData();
  }, [currentLanguage]);

  if(isRedirect) {
    return <Redirect to="/error" />
  }

  return (
    <ComponentContainer>
      {!isLoading && !data && (<WarningText>{t('no_car_information')}</WarningText>)}
      {isLoading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
      {data && !isLoading &&
      <InfofmationContainer>
        <Row>
          <Title>{data.brand} {data.model} {data.yearOfIssue}</Title> 
        </Row>
        <Marginer direction="vertical" margin={10} />
        <Row>
          <MinRow>
            <ImageComponentWrapper>
              <ImagesContainer >
                {data.carImageLink ? (
                  <Image src={data.carImageLink} alt={data.brand}/>
                ) : (
                  <Image src={noImage} alt={data.brand}/>
                )}
              </ImagesContainer>
            </ImageComponentWrapper>
          </MinRow>
          <Marginer direction="horizontal" margin={20} />
          <MinRow>
            <ComponentWrapper>
              <Tabs>
                <TabPane 
                  tab={
                    <span>
                      <Settings />&nbsp;
                      {t('car_options')}
                    </span>
                  } 
                  key="1"
                >
                  <OptionsContainer>
                    <Table borderless responsive>
                      <OptionsTableBody>
                        <tr>
                          <LeftColumnContainer>{t('car_class')}</LeftColumnContainer>
                          <RightColumnContainer>{data.carClass}</RightColumnContainer>
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('color')}</LeftColumnContainer>
                          <RightColumnContainer>
                            {data.color === "Red" && `${t('red')}`}
                            {data.color === "White" && `${t('white')}`}
                            {data.color === "Black" && `${t('black')}`}
                            {data.color === "Green" && `${t('green')}`}
                            {data.color === "Grey" && `${t('grey')}`}
                            {data.color === "Yellow" && `${t('yellow')}`}
                            {data.color === "Blue" && `${t('blue')}`}
                            {data.color === "Silver" && `${t('silver')}`}
                            {data.color === "Pink" && `${t('pink')}`}
                            {data.color === "Orange" && `${t('orange')}`}
                            {data.color === "Brown" && `${t('brown')}`}
                            {data.color === "Violet" && `${t('violet')}`}
                            {data.color === "Gold" && `${t('gold')}`}
                            {data.color === "Sand" && `${t('sand')}`}
                            {data.color !== "Red" && 
                              data.color !== "White" && 
                              data.color !== "Black" && 
                              data.color !== "Green" && 
                              data.color !== "Grey" && 
                              data.color !== "Yellow" && 
                              data.color !== "Blue" && 
                              data.color !== "Silver" && 
                              data.color !== "Pink" && 
                              data.color !== "Orange" && 
                              data.color !== "Brown" && 
                              data.color !== "Violet" && 
                              data.color !== "Gold" && 
                              data.color !== "Sand" && `${data.color}`}
                          </RightColumnContainer>
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('body_type')}</LeftColumnContainer>
                          <RightColumnContainer>
                            {data.bodyType === "Hatchback" && ` ${t('hatchback')}`}
                            {data.bodyType === "Sedan" && ` ${t('sedan')}`}
                            {data.bodyType === "MUV" && ` ${t('muv_suv')}`}
                            {data.bodyType === "Coupe" && ` ${t('coupe')}`}
                            {data.bodyType === "Convertible" && ` ${t('convertible')}`}
                            {data.bodyType === "Wagon" && ` ${t('wagon')}`}
                            {data.bodyType === "Van" && ` ${t('van')}`}
                            {data.bodyType === "Jeep" && ` ${t('jeep')}`}
                          </RightColumnContainer>
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('engine_type')}</LeftColumnContainer>
                          <RightColumnContainer>
                            {data.engineType === "Diesel" && ` ${t('diesel')}`}
                            {data.engineType === "Petrol" && ` ${t('petrol')}`}
                            {data.engineType === "Hybrid" && ` ${t('hybrid')}`}
                            {data.engineType === "Electro" && ` ${t('electro')}`}
                          </RightColumnContainer>
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('transmission')}</LeftColumnContainer>
                          {data.automaticTransmission ? <RightColumnContainer>{t('automatic')}</RightColumnContainer> : <RightColumnContainer>{t('manual')}</RightColumnContainer> }
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('air_conditioner')}</LeftColumnContainer>
                          {data.hasConditioner ? <RightColumnContainer>{t('has_air_conditioner')}</RightColumnContainer> : <RightColumnContainer>{t('without_air_conditioner')}</RightColumnContainer> }
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('passengers_amount')}</LeftColumnContainer>
                          <RightColumnContainer>{data.passengersAmt}</RightColumnContainer>
                        </tr>
                        <tr>
                          <LeftColumnContainer>{t('baggage_amount')}</LeftColumnContainer>
                          <RightColumnContainer>{data.baggageAmt}</RightColumnContainer>
                        </tr>
                      </OptionsTableBody>
                    </Table>
                  </OptionsContainer>
                </TabPane>
                { props.user && props.user.role != null && (
                  <TabPane 
                    tab={
                      <span>
                        <CarRental />&nbsp;
                        {t('rent_car')}
                      </span>
                    } 
                    key="2"
                  >
                    <OrderContainer>
                      <OrderComponent data={data} pickUpDateTime={props.location.pickUpDateTime} returnDateTime={props.location.returnDateTime} />
                    </OrderContainer>
                  </TabPane>
                )}
              </Tabs>
            </ComponentWrapper>
          </MinRow>
        </Row>
        <Marginer direction="vertical" margin={20} />
        <Row>
          <MinRow>
            <ComponentWrapper>
              <PricingContainer>
                <TopContainer>
                  {t('rental_details')}
                </TopContainer>
                <Table responsive>
                  <tbody>
                    <tr>
                      <td>{t('cost_per_hour')}</td>
                      <td>{data.costPerHour} BYN</td>
                    </tr>
                  </tbody>
                </Table>
                <AlertContainer>
                  <span>{t('attention')}!</span> {t('car_insurance_is_not_included_in_the_rental_price')}
                  {t('in_case_of_damage_exceeding_the_rental_period_traffic_fines_and_other_violations_you_will_be_charged_a_fine')}
                </AlertContainer>
              </PricingContainer>
            </ComponentWrapper>
          </MinRow>
          <Marginer direction="horizontal" margin={20} />
          <Marginer direction="vertical" margin={20} />
          <MinRow>
            <ComponentWrapper>
              <MapContainer>
                <Row className="location-title">
                  <Location />&nbsp;{data.locationName}
                </Row>
                <Marginer direction="vertical" margin={10} />
                <Row>
                  <YMaps>
                    <Map width="100%" height="180px" defaultState={mapData}> 
                      <Placemark geometry={mapData.center} />
                    </Map>
                  </YMaps>
                </Row>
              </MapContainer>
            </ComponentWrapper>
          </MinRow>
        </Row>
      </InfofmationContainer>
      }
    </ComponentContainer>   
  )
}

export default CarComponent
