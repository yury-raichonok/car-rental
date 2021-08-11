import { useState, createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, notification, Spin } from 'antd';
import Marginer from '../../marginer/Marginer';
import OrderDataService from '../../../services/order/OrderDataService';
import { useTranslation } from 'react-i18next';

const ContentContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

  .ant-form {
    width: 100%;
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

  @media (max-width: 900px) { 
    flex-direction: column;
  }
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .btn {
    margin: 0px 5px;
  }

  .ant-form-item {
    width: 100%;
  }

  .ant-switch-checked {
    background-color: #ea5c52;
  }
`;

const RowTitle = styled.div`
  font-size: 16px;
  width: 120px;
  text-align: left;
`;

const RowData = styled.div`
  font-size: 16px;
  width: 400px;
  text-align: left;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const CompleteOrderComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();

  const [penalty, setPenalty] = useState(false);
  const [loading, setLoading] = useState(false);

  const onCompleteOrder = async () => {
    setLoading(true);

    const res = await OrderDataService.completeOrder(props.data.id).catch(
      err => {
        props.handleCompleteOrderClose();
        props.fetchCurrentOrders();
        notification.error({
          message: `${t('error_when_completing_order')}`,
        });
        setLoading(false);
      }
    )

    if (res) {
      props.handleCompleteOrderClose();
      props.fetchCurrentOrders();
      notification.success({
        message: `${t('order_completed')}`,
        description: `${t('notification_with_message_has_been_sent_to_the_user')}`
      });
      setLoading(false);
    }
  }

  const onCompleteOrderWithPenalty = async (values) => {
    setLoading(true);

    const res = await OrderDataService.completeOrderWithPenalty(props.data.id, values).catch(
      err => {
        props.handleCompleteOrderClose();
        props.fetchCurrentOrders();
        notification.error({
          message: `${t('error_when_completing_order_with_penalty')}`,
        });
        setLoading(false);
      }
    )

    if (res) {
      props.handleCompleteOrderClose();
      props.fetchCurrentOrders();
      notification.success({
        message: `${t('order_completed')}`,
        description: `${t('notification_with_message_and_repair_bill_has_been_sent_to_the_user')}`
      });
      setLoading(false);
    }
  }

  const handleCompleteOrderWithPenalty = () => {
    setPenalty(!penalty);
  }

  return (
    <ContentContainer>
      <Spin spinning={loading}>
        <Row>
          <RowTitle>{t('user')}:</RowTitle>
          <RowData>{props.data.userEmail}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('car')}:</RowTitle>
          <RowData>{props.data.carBrandModel}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('car_vin')}:</RowTitle>
          <RowData>{props.data.carVin}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('car_location_for_order')}:</RowTitle>
          <RowData>{props.data.locationName}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('pick_up_date_for_order')}:</RowTitle>
          <RowData>{props.data.pickUpDate}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('return_date')}:</RowTitle>
          <RowData>{props.data.returnDate}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('total_cost')}:</RowTitle>
          <RowData>{props.data.totalCost}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        <Row>
          <RowTitle>{t('sent_date_for_order')}:</RowTitle>
          <RowData>{props.data.sentDate}</RowData>
        </Row>
        <Marginer direction="vertical" margin={8} />
        {penalty ? (
          <Form ref={formRef} name="validate_other" onFinish={onCompleteOrderWithPenalty}>
            <Row>
              <Column>
                <Marginer direction="vertical" margin={4} />
                <RowTitle>{t('message')}:</RowTitle>
              </Column>
              <Form.Item name="message" 
                rules={[
                  {
                    required: true,
                    message: `${t('please_input_message')}`,
                  },
                  {
                    whitespace: true,
                    message: `${t('message_can_not_be_empty')}`,
                  },
                  {
                    max: 255,
                    message: `${t('message_is_too_long')}`,
                  }
                ]}
              >
                <Input autoComplete="off" placeholder = {t('input_message_capital')}/>
              </Form.Item>
            </Row>
            <Row>
              <Column>
                <Marginer direction="vertical" margin={4} />
                <RowTitle>{t('total_cost')}:</RowTitle>
              </Column>
              <Form.Item name="totalCost" 
                rules={[
                  {
                    required: true,
                    message: `${t('please_input_total_cost')}`,
                  },
                ]}
              >
                <Input type="number" min="1" autoComplete="off" step="0.01" placeholder={t('penalty')} />
              </Form.Item>
            </Row>
            <Row>
              <button type="submit" className="btn" onClick={handleCompleteOrderWithPenalty}>
                {t('cancel')}
              </button>
              <button type="submit" className="btn">
                {t('complete_order_with_penalty')}
              </button>
            </Row>
          </Form>
        ) : (
          <>
            <Row>
              <button type="button" className="btn red" onClick={handleCompleteOrderWithPenalty}>
                {t('complete_order_with_penalty')}
              </button>
              <button type="button" className="btn green" onClick={onCompleteOrder}>
                {t('complete_order')}
              </button>
            </Row>
          </>
        )}
      </Spin>
    </ContentContainer>
  )
}

export default CompleteOrderComponent
