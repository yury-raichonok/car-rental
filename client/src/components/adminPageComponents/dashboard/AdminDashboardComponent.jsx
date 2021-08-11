import React, { useState, createRef, useEffect } from 'react';
import styled from 'styled-components';
import { FiShoppingCart } from 'react-icons/fi';
import { IoChevronForwardCircleSharp, IoDocumentTextOutline } from 'react-icons/io5';
import { TiMessages } from 'react-icons/ti';
import { AiOutlineUserAdd } from 'react-icons/ai';
import Marginer from '../../marginer/Marginer';
import { Form, Select, notification, Input, Spin } from 'antd';
import { Link } from 'react-router-dom';
import LocationDataService from '../../../services/location/LocationDataService';
import RentalDetailsDataService from '../../../services/rentalDetails/RentalDetailsDataService';
import { useTranslation } from 'react-i18next';
import cookies from 'js-cookie';

const { Option } = Select;

const TitleSection = styled.div`
  display: flex;
  width: 100%;
  font-size: 28px;
  text-align: left;
  padding: 20px;
  justify-content: space-between;
  align-items: center;

  @media (max-width: 400px) { 
    font-size: 20px;
  }

  @media (max-width: 300px) { 
    flex-wrap: wrap;
  }
`;

const EditDetailsContainer = styled.div`
  font-size: 16px;
  color: gray;
  cursor: pointer;

  @media (max-width: 400px) { 
    font-size: 14px;
  }
`;

const RowWithTopAlign = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: top;
  padding: 0 20px;

  .ant-form-item {
    width: 250px;
    min-width: 0px;
  }

  .ant-form-item-explain {
    max-width: 250px;
  }

  .ant-select {
    text-align: left;
  }

  .ant-select:not(.ant-select-disabled):hover .ant-select-selector {
    border: 1px solid gray;
  }

  .ant-select-focused:not(.ant-select-disabled).ant-select:not(.ant-select-customize-input) .ant-select-selector {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-picker:hover, .ant-picker-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input-number:hover {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input-number-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-checkbox-wrapper:hover .ant-checkbox-inner, .ant-checkbox:hover .ant-checkbox-inner, .ant-checkbox-input:focus + .ant-checkbox-inner {
    border-color: #f44336;
  }

  .ant-input:focus, .ant-input-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input:hover {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-form-item {
    margin-bottom: 0px;
  }

  .ant-form-item-control {
    margin-bottom: 24px;
  }

  .ant-form-item-with-help {
    .ant-form-item-control {
      margin-bottom: 0px;
    }
  }

  .ant-checkbox-wrapper {
    font-size: 16px;
  }

  .btn {
    width: 100%;
    font-size: 15px;
    color: #fff;
    background-color: #f44336;
    border: none;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  @media (max-width: 510px) { 
    flex-wrap: wrap;
  }
`;

const RentalDetailsContainer = styled.div`
  width: 100%;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowTitle = styled.div`
  font-size: 16px;
  width: 150px;
  text-align: left;
`;

const InfoContainer = styled.div`
  height: 27px;
  margin-top: 5px;
  margin-left: 12px;
  width: 250px;
  text-align: left;

  @media (max-width: 300px) { 
    width: 100%;
  }
`;

const Row = styled.div`
  display: flex;
  width: 100%;
  justify-content: space-around;

  @media (max-width: 1400px) { 
    flex-wrap: wrap;
  }
`;

const NewOrdersContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 23%;
  min-width: 200px;
  height: 130px;
  background: #17a2b8;
  border-radius: 8px;

  &:hover {
    .card-icon {
      height: 60px;
      width: 60px;
    }

    a {
      text-decoration: none;
      color: #fff;
    }
  }

  @media (max-width: 1200px) { 
    margin: 0 10px 10px 10px;
    min-width: 250px;
  }

  @media (max-width: 350px) { 
    width: 90%;
    min-width: 0;
  }
`;

const NewMessagesContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 23%;
  min-width: 200px;
  height: 130px;
  background: #ffc107;
  border-radius: 8px;

  &:hover {
    .card-icon {
      height: 60px;
      width: 60px;
    }

    a {
      text-decoration: none;
      color: #fff;
    }
  }

  @media (max-width: 1200px) { 
    margin: 0 10px 10px 10px;
    min-width: 250px;
  }

  @media (max-width: 350px) { 
    width: 90%;
    min-width: 0;
  }
`;

const NewUsersContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 23%;
  min-width: 200px;
  height: 130px;
  background: #dc3545;
  border-radius: 8px;

  &:hover {
    .card-icon {
      height: 60px;
      width: 60px;
    }

    a {
      text-decoration: none;
      color: #fff;
    }
  }

  @media (max-width: 1200px) { 
    margin: 0 10px 10px 10px;
    min-width: 250px;
  }

  @media (max-width: 350px) { 
    width: 90%;
    min-width: 0;
  }
`;

const NewRequestsContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  width: 23%;
  min-width: 200px;
  height: 130px;
  background: #28a745;
  border-radius: 8px;

  &:hover {
    .card-icon {
      height: 60px;
      width: 60px;
    }

    a {
      text-decoration: none;
      color: #fff;
    }
  }

  @media (max-width: 1200px) { 
    margin: 0 10px 10px 10px;
    min-width: 250px;
  }

  @media (max-width: 350px) { 
    width: 90%;
    min-width: 0;
  }
`;

const CardStatistic = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  color: #fff;
  align-items: center;
  margin-top: 10px;
  
  svg {
    height: 50px;
    width: 50px;
    color: rgba(0,0,0,.15);
    transition: all 0.5s;
  }
`;

const CardStatisticNumber = styled.div`
  font-size: 32px;
  font-weight: 500;
  text-align: left;
`;

const CardStatisticDescription = styled.div`
  font-size: 18px;
  font-weight: 500;
  text-align: left;
`;

const CardFooter = styled.a`
  background-color: rgba(0,0,0,.1);
  text-align: center;
  text-decoration: none;
  padding: 5px 0;
  border-radius: 0 0 5px 5px;
  align-items: center;

  a {
    color: #fff;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  width: 400px;
  margin-bottom: 20px;
  padding-left: 30px;

  .btn {
    width: 100%;
    font-size: 15px;
    color: #fff;
    background-color: #f44336;
    border: none;
    margin: 0 5px;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  @media (max-width: 508px) { 
    flex-wrap: wrap;
    width: 250px;
    
    .btn {
      margin: 5px;
    } 
  }

  @media (max-width: 370px) { 
    width: 100%;
    padding-left: 0px;
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


const AdminDashboardComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [rentalDetails, setRentalDetails] = useState();
  const [editRentalDetails, setEditRentalDetails] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  const [locations, setLocations] = useState([]);

  const formRef = createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const fetchRentalDetailsData = async () => {
    setIsLoading(true);

    const resp = await RentalDetailsDataService.findRentalDetailsAndStatistic().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_rental_details')}`,
      });
      setIsLoading(false);
    });

    if(resp) {
      setRentalDetails(resp.data);
      setIsLoading(false);
    }
  }

  useEffect(() => {
    setIsLoading(true);
    fetchRentalDetailsData();
    fetchLocations();
    setIsLoading(false);
  }, [currentLanguage]);

  const fetchLocations = async () => {
    setIsLoading(true);

    const resp = await LocationDataService.findAllForSelect().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_locations')}`,
      });
      setIsLoading(false);
    });

    if(resp) {
      setLocations(resp.data);
      setIsLoading(false);
    }
  }

  const onRentalDetailsChangingFinish = async (values) => {
    
    const resp = await RentalDetailsDataService.update(values).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_rental_details')}`,
      });
    });

    if(resp) {
      fetchRentalDetailsData();
      setEditRentalDetails(false);
      notification.success({
        message: `${t('rental_details_successfully_updated')}`,
      });
    }
  }

  const handleEditRentalDetails = () => {
    setEditRentalDetails(true);
  }

  const handleCancelEditRentalDetails = () => {
    setEditRentalDetails(false);
    onReset();
  }

  return (
    <div>
      <TitleSection>
        {t('today')}
      </TitleSection>
      <Row>
        <NewOrdersContainer>
          <CardStatistic>
            <div>
              <CardStatisticNumber>
                {rentalDetails ? rentalDetails.newOrders : "0"}
              </CardStatisticNumber>
              <CardStatisticDescription>
                {t('new_orders')}
              </CardStatisticDescription>
            </div>
            <FiShoppingCart className = "card-icon"/>
          </CardStatistic>
          <CardFooter>
            <Link to="/admin/orders/new">
              {t('more_info')}&ensp;
              <IoChevronForwardCircleSharp />
            </Link>
          </CardFooter>
        </NewOrdersContainer>
        <NewRequestsContainer>
          <CardStatistic>
            <div>
              <CardStatisticNumber>
                {rentalDetails ? rentalDetails.newRequests : "0"}
              </CardStatisticNumber>
              <CardStatisticDescription>
                {t('new_requests')}
              </CardStatisticDescription>
            </div>
            <IoDocumentTextOutline className = "card-icon"/>
          </CardStatistic>
          <CardFooter>
            <Link to="/admin/requests/new">
              {t('more_info')}&ensp;
              <IoChevronForwardCircleSharp />
            </Link>
          </CardFooter>
        </NewRequestsContainer>
        <NewMessagesContainer>
          <CardStatistic>
            <div>
              <CardStatisticNumber>
                {rentalDetails ? rentalDetails.newMessages : "0"}
              </CardStatisticNumber>
              <CardStatisticDescription>
                {t('new_messages')}
              </CardStatisticDescription>
            </div>
            <TiMessages className = "card-icon"/>
          </CardStatistic>
          <CardFooter>
            <Link to="/admin/messages/new">
              {t('more_info')}&ensp;
              <IoChevronForwardCircleSharp />
            </Link>
          </CardFooter>
        </NewMessagesContainer>
        <NewUsersContainer>
          <CardStatistic>
            <div>
              <CardStatisticNumber>
                {rentalDetails ? rentalDetails.newUsers : "0"}
              </CardStatisticNumber>
              <CardStatisticDescription>
                {t('new_users')}
              </CardStatisticDescription>
            </div>
            <AiOutlineUserAdd className = "card-icon"/>
          </CardStatistic>
          <CardFooter>
            <Link to="/admin/users">
              {t('more_info')}&ensp;
              <IoChevronForwardCircleSharp />
            </Link>
          </CardFooter>
        </NewUsersContainer>
      </Row>
      <TitleSection>
        <div>{t('rental_details')}</div>
        <EditDetailsContainer onClick = {handleEditRentalDetails}>{t('edit_details')}</EditDetailsContainer>
      </TitleSection>
      {isLoading ? (
        <div><Spin/>{t('loading')} . . .</div>
      ) : (
        <RentalDetailsContainer>
          <Form ref={formRef} name="validate_other" onFinish={onRentalDetailsChangingFinish}>
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('email')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item 
                  initialValue = {rentalDetails.email}
                  name="email"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_email')}`,
                    },
                    {
                      type: 'email',
                      message: `${t('email_is_not_valid')}`,
                    }
                  ]}
                >
                  <Input placeholder={t('enter_email')}/>
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item 
                  name="email"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_email')}`,
                    },
                    {
                      type: 'email',
                      message: `${t('email_is_not_valid')}`,
                    }
                  ]}
                >
                  <Input placeholder={t('enter_email')}/>
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.email}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('phone')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item 
                  name="phoneNumber"
                  initialValue = {rentalDetails.phoneNumber}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_rental_phone')}`,
                    },
                    {
                      pattern: "^\\+375[0-9]{9}$",
                      message: `${t('invalid_phone_number')}`,
                    },
                  ]}
                >
                  <Input type="text" autoComplete="off" placeholder={t('enter_rental_phone')} />
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item 
                  name="phoneNumber"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_rental_phone')}`,
                    },
                    {
                      pattern: "^\\+375[0-9]{9}$",
                      message: `${t('invalid_phone_number')}`,
                    },
                  ]}
                >
                  <Input type="text" autoComplete="off" placeholder={t('enter_rental_phone')} />
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.phoneNumber}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('location')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item name="location" 
                  initialValue = {rentalDetails.location}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_location')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_location')}>
                    {locations.length !== 0 && locations.map((location) => (
                      <Option key={location.id} value={location.id}>{location.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item name="location" 
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_location')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_location')}>
                    {locations.length !== 0 && locations.map((location) => (
                      <Option key={location.id} value={location.id}>{location.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.location}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('from_day_coef')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item
                  initialValue = {rentalDetails.fromDayToWeekCoefficient}
                  name="fromDayToWeekCoefficient"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_coefficient')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" step="0.01" placeholder={t('from_day_payment_coefficient')} />
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item 
                  name="fromDayToWeekCoefficient"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_coefficient')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" step="0.01" placeholder={t('from_day_payment_coefficient')} />
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.fromDayToWeekCoefficient}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('from_week_coef')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item
                  initialValue = {rentalDetails.fromWeekCoefficient}
                  name="fromWeekCoefficient"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_coefficient')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" step="0.01" placeholder={t('from_week_payment_coefficient')} />
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item
                  name="fromWeekCoefficient"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_coefficient')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" step="0.01" placeholder={t('from_week_payment_coefficient')} />
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.fromWeekCoefficient}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            <RowWithTopAlign>
              <Column>
                <Marginer direction="vertical" margin={3} />
                <RowTitle>{t('bill_validity_period')}:&ensp;</RowTitle>
              </Column>
              {rentalDetails && editRentalDetails && (
                <Form.Item
                  initialValue = {rentalDetails.billValidityPeriod}
                  name="billValidityPeriod"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_bill_validity_period')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" placeholder={t('bill_validity_period')} />
                </Form.Item>
              )}
              {!rentalDetails && !editRentalDetails && (
                <Form.Item
                  name="billValidityPeriod"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_bill_validity_period')}`,
                    },
                  ]}
                >
                  <Input type="number" min="0" autoComplete="off" placeholder={t('bill_validity_period')} />
                </Form.Item>
              )}
              {rentalDetails && !editRentalDetails && (
                <InfoContainer>{rentalDetails.billValidityPeriod}</InfoContainer>
              )}
            </RowWithTopAlign>
            {rentalDetails && !editRentalDetails && (
              <Marginer direction="vertical" margin={24} />
            )}
            {rentalDetails && !editRentalDetails ? (
              <></>
            ) : (
              <ButtonContainer>
                <button className="btn" onClick={handleCancelEditRentalDetails} >{t('cancel')}</button>
                <button className="btn" type="submit">{t('update_rental_details')}</button>
              </ButtonContainer>
            )}
          </Form>
        </RentalDetailsContainer>
      )}
    </div>
  )
}

export default AdminDashboardComponent