import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Form, Spin, DatePicker, notification } from 'antd';
import moment from 'moment';
import OrderDataService from '../../services/order/OrderDataService';
import { Redirect } from 'react-router-dom';
import Email from "@kiwicom/orbit-components/lib/icons/Email";
import Invoice from "@kiwicom/orbit-components/lib/icons/Invoice";
import Wallet from "@kiwicom/orbit-components/lib/icons/Wallet";
import Marginer from '../marginer/Marginer';
import { useTranslation } from 'react-i18next';

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowTitle = styled.div`
  font-size: 15px;
  width: 160px;
  text-align: left;
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .ant-form-item {
    width: 100%;
  }

  @media (max-width: 500px) { 
    flex-direction: column;
  }
`;

const FormContainer =  styled.div`
  width: 100%;
  display: flex;

  form {
    width: 100%;
    
    .ant-picker {
      width: 100%;
    }
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

  .ant-checkbox-checked .ant-checkbox-inner {
    background-color: #f44336;
    border-color: #f44336;
  }

  .ant-checkbox-checked::after {
    border-color: #f44336;
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
`;

const TotalCostContainer = styled.div`
  width: 100%;
  display: flex;
  margin-bottom: 20px;
  min-height: 40px;

  @media (max-width: 500px) { 
    flex-direction: column;
  }
`;

const TotalCostValueContainer = styled.div`
  font-size: 15px;
  text-align: left;
`;

const ButtonsContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;

  .calculateCost {
    width: 100%;
    font-size: 15px;
    color: #000;
    background-color: #4842420f;
    border: none;
    border-radius: 0px;
    margin-bottom: 20px;

    &:hover {
      background-color: #f44336;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  .sentOrder {
    width: 100%;
    font-size: 15px;
    color: #000;
    background-color: #4842420f;
    border: none;
    border-radius: 0px;
    margin-bottom: 20px;

    &:hover {
      background-color: #1d8e1d;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  .contactUs {
    width: 100%;
    font-size: 15px;
    color: #000;
    background-color: #4842420f;
    border: none;
    border-radius: 0px;

    &:hover {
      background-color: #bd7a16;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }
`;

const WarningText = styled.div`
  font-size: 15px;
  text-align: left;
`;

const OrderComponent = (props) => {

  const { t } = useTranslation();

  const [form] = Form.useForm();
  const [isOrderSended, setIsOrderSended] = useState(false);
  const [isLoading, setLoading] = useState(false);
  const [totalCost, setTotalcost] = useState();
  const [order, setOrder] = useState((props.pickUpDateTime && props.returnDateTime) ? ({
    pickUpDate: props.pickUpDateTime,
    returnDate: props.returnDateTime,
    costPerHour: props.data.costPerHour,
    carId: props.data.id
  }) : ({
    pickUpDate: null,
    returnDate: null,
    costPerHour: props.data.costPerHour,
    carId: props.data.id
  }))
  const [data, setData] = useState((props.pickUpDateTime && props.returnDateTime) ? ({
      pickUpDate: props.pickUpDateTime,
      returnDate: props.returnDateTime,
      costPerHour: props.data.costPerHour,
  }) : ({
      pickUpDate: null,
      returnDate: null,
      costPerHour: props.data.costPerHour,
  }));
  
  const isCostEmpty = !totalCost || (totalCost && totalCost.length === 0);

  const onFinish = (values) => {
    OrderDataService.createOrder(order).then(
      () => {
        setIsOrderSended(true);
        notification.success({
          message: `${t('congratulations')}`,
          description:
            `${t('your_order_has_been_successfully_placed_after_checking_all_the_details_and_the_specified_data_you_will_be_sent_an_invoice_for_payment')}`,
        });

      }  
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 400:
              notification.error({
                message: `${t('oops')}`,
                description: `${t('your_documents_are_not_confirmed')}`,
              });
              break;
            case 401:
              notification.error({
                message: `${t('oops')}`,
                description: `${t('you_need_to_log_in_to_place_an_order')}`,
              });
              break;
            case 405:
              notification.error({
                message: `${t('this_car_has_already_been_booked_for_the_ates_indicated')}`
              });
              break;
            case 406:
              notification.error({
                message: `${t('the_rental_period_is_incorrect')}`
              });
              break;  
            default:
              notification.error({
                message: `${t('oops')}`,
                description: `${t('something_wrong_please_try_again')}`,
              });
          }
        } else{
          notification.error({
            message: `${t('oops')}`,
            description: `${t('something_wrong_please_try_again')}`,
          });
        }
      }
    )
  };

  const calculateTotalCost = async () => {
    setLoading(true);
    const resp = await OrderDataService.calculateTotalCost(data).catch((err) => {
      if(err && err.response){
        switch(err.response.status){
          case 406:
            notification.error({
              message: `${t('the_rental_period_is_incorrect')}`
            });
            break;  
          default:
            notification.error({
              message: `${t('oops')}`,
              description: `${t('something_wrong_please_try_again')}`,
            });
        }
      } else{
        notification.error({
          message: `${t('oops')}`,
          description: `${t('something_wrong_please_try_again')}`,
        });
      }
    })

    if(resp) {
      setTotalcost(resp.data.totalCost)
    }    

    setLoading(false);
  }

  useEffect(() => {
    if (props.pickUpDateTime && props.returnDateTime) {
      calculateTotalCost();
    }
  }, []);

  const handlePickUpDateChange = (e) => {
    data.pickUpDate = e;
    order.pickUpDate = e;
    if (e >= data.returnDate) {
      form.resetFields(['returnDate']);
    }
  }

  const handleReturnDateChange = (e) => {
    setData(prevState => ({
      ...prevState,
      returnDate: e
    }))
    setOrder(prevState => ({
      ...prevState,
      returnDate: e
    }))
  }

  function disabledDate(current) {
    return current && current < moment().startOf("hour");
  }

  function disabledReturnDate(current) {
    if (data.pickUpDate) {
      return current && current < data.pickUpDate;
    } else {
      return current && current < moment().endOf("hour");
    }
  }

  if (isOrderSended) {
    return <Redirect to="/" />
  }

  return (
    <FormContainer>
      <Form form={form} name="validate_other" onFinish={onFinish}>
      <Marginer direction="vertical" margin={10} />
        <Row>
          <Column>
            <Marginer direction="vertical" margin={4} />
            <RowTitle>{t('pick_up_date')}:</RowTitle>
          </Column>
          {props.pickUpDateTime ? (
            <Form.Item name = "pickUpDate"
              initialValue = {moment(new Date(props.pickUpDateTime))}
              rules={[
                {
                  required: true,
                  message: `${t('please_select_pick-up_date')}`,
                },
              ]}
            >
              <DatePicker 
                onChange={handlePickUpDateChange} 
                showNow={false}
                disabledDate={disabledDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
              />
              
            </Form.Item>
          ) : (
            <Form.Item name = "pickUpDate"
              rules={[
                {
                  required: true,
                  message: `${t('please_select_pick-up_date')}`,
                },
              ]}
            >
              <DatePicker 
                placeholder={t('pick_up_date')} 
                onChange={handlePickUpDateChange} 
                showNow={false}
                disabledDate={disabledDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
              />
            </Form.Item>
          )}
        </Row>
        <Row>
          <Column>
            <Marginer direction="vertical" margin={4} />
            <RowTitle>{t('return_date')}:</RowTitle>
          </Column>
          {props.returnDateTime ? (
            <Form.Item name="returnDate"
              initialValue = {moment(new Date(props.returnDateTime))}
              rules={[
                {
                  required: true,
                  message: `${t('please_select_return_date')}`,
                },
              ]}
            >
              <DatePicker 
                onChange={handleReturnDateChange} 
                showNow={false}
                disabledDate={disabledReturnDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
                />
            </Form.Item>
          ) : (
            <Form.Item name="returnDate"
              rules={[
                {
                  required: true,
                  message: `${t('please_select_return_date')}`,
                },
              ]}
            >
              <DatePicker 
                placeholder={t('return_date')}
                onChange={handleReturnDateChange} 
                showNow={false}
                disabledDate={disabledReturnDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
              />
            </Form.Item>
          )}
        </Row>
        
        <TotalCostContainer>
          <RowTitle>
            {t('total_cost')}:
          </RowTitle>
          {isCostEmpty && !isLoading && (<TotalCostValueContainer className="value-container">{t('not_enough_data_to_calculate_the_total_cost')}</TotalCostValueContainer>)}
            { isLoading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
            {!isCostEmpty && !isLoading && 
              <TotalCostValueContainer>
                {totalCost} BYN
              </TotalCostValueContainer>
            }
        </TotalCostContainer>
        <ButtonsContainer>
          <button type="button" className="btn calculateCost" onClick={calculateTotalCost}><Wallet />&nbsp;&nbsp;&nbsp;{t('calculate_cost')}</button>
          <button className="btn sentOrder" type="submit"><Invoice />&nbsp;&nbsp;&nbsp;{t('send_order')}</button>
          <button type="button" className="btn contactUs" onClick={()=> window.open("/contact", "_blank")}><Email />&nbsp;&nbsp;&nbsp;{t('contact_us_low_c')}</button>
        </ButtonsContainer>
      </Form>
    </FormContainer>
  )
}

export default OrderComponent
