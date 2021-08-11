import { createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, notification } from 'antd';
import LocationDataService from '../../../services/location/LocationDataService';
import Marginer from '../../marginer/Marginer';
import { useTranslation } from 'react-i18next';

const AddNewCarContainer = styled.div`
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
  font-size: 15px;
  width: 190px;
  text-align: left;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const EditLocationComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const onFinish = (values) => {
    LocationDataService.update(props.data.id, values).then(
      res => {
        onReset();
        props.handleEditInfoCancel();
        props.fetchLocations();
        notification.success({
          message: `${t('location_successfully_updated')}`,
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('location_with_the_same_name_already_exists')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('error_when_updating_location')}`,
              });
          }
        }
      }
    )
  };

  return (
    <AddNewCarContainer>
      <Form ref={formRef} name="validate_other" onFinish={onFinish}>
        <AddNewCarContainer>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('address_ru')}:</RowTitle>
            </Column>
            <Form.Item name="nameRu" 
              initialValue = {props.data.nameRu}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_location_name')}`,
                },
                {
                  whitespace: true,
                  message: `${t('location_name_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('location_name_is_too_long')}`,
                },
              ]}
            >
              <Input type="text" autoComplete="off" placeholder={t('input_location_name')} />
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('address_be')}:</RowTitle>
            </Column>
            <Form.Item name="nameBe" 
              initialValue = {props.data.nameBe}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_location_name')}`,
                },
                {
                  whitespace: true,
                  message: `${t('location_name_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('location_name_is_too_long')}`,
                },
              ]}
            >
              <Input type="text" autoComplete="off" placeholder={t('input_location_name')} />
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('address_en')}:</RowTitle>
            </Column>
            <Form.Item name="nameEn" 
              initialValue = {props.data.nameEn}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_location_name')}`,
                },
                {
                  whitespace: true,
                  message: `${t('location_name_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('location_name_is_too_long')}`,
                },
              ]}
            >
              <Input type="text" autoComplete="off" placeholder={t('input_location_name')} />
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('coord_x')}:</RowTitle>
            </Column>
            <Form.Item name="coordinateX" 
              initialValue = {props.data.coordinateX}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_coordinate_x')}`,
                },
              ]}
            >
              <Input type="number" min="0" step="0.000001" autoComplete="off" placeholder={t('input_coordinate_x')} />
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('coord_y')}:</RowTitle>
            </Column>
            <Form.Item name="coordinateY" 
              initialValue = {props.data.coordinateY}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_coordinate_y')}`,
                },
              ]}
            >
              <Input type="number" min="0" step="0.000001" autoComplete="off" placeholder={t('input_coordinate_y')} />
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('zoom')}:</RowTitle>
            </Column>
            <Form.Item name="zoom" 
              initialValue = {props.data.zoom}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_zoom')}`,
                },
              ]}
            >
              <Input type="number" min="0" autoComplete="off" placeholder={t('input_zoom')} />
            </Form.Item>
          </Row>
          <Row>
            <button type="submit" className="btn">
              {t('submit')}
            </button>
            <button type="button" className="btn" onClick={onReset}>
              {t('reset')}
            </button>
          </Row>
        </AddNewCarContainer>
      </Form>
    </AddNewCarContainer>
  )
}

export default EditLocationComponent
