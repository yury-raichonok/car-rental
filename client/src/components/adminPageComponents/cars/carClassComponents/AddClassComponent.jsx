import { createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, notification } from 'antd';
import CarClassDataService from '../../../../services/carClass/CarClassDataService';
import Marginer from '../../../marginer/Marginer';
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

const AddClassComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const onFinish = (values) => {
    CarClassDataService.create(values).then(
      res => {
        onReset();
        props.handleClassCancel();
        props.fetchClasses();
        notification.success({
          message: `${t('new_car_class_added_successfully')}`,
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('car_class_with_the_same_name_already_exists')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('error_when_adding_a_new_car_class')}`,
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
              <RowTitle>{t('entity_name_ru')}:</RowTitle>
            </Column>
            <Form.Item name="nameRu" 
              rules={[
                {
                  required: true,
                  message: `${t('please_input_car_class_name_on_russian')}`,
                },
                {
                  whitespace: true,
                  message: `${t('car_class_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('car_class_is_too_long')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_car_class_name_on_russian')}/>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('entity_name_be')}:</RowTitle>
            </Column>
            <Form.Item name="nameBe" 
              rules={[
                {
                  required: true,
                  message: `${t('please_input_car_class_name_on_belarussian')}`,
                },
                {
                  whitespace: true,
                  message: `${t('car_class_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('car_class_is_too_long')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_car_class_name_on_belarussian')}/>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('entity_name_en')}:</RowTitle>
            </Column>
            <Form.Item name="nameEn" 
              rules={[
                {
                  required: true,
                  message: `${t('please_input_car_class_name_on_english')}`,
                },
                {
                  whitespace: true,
                  message: `${t('car_class_can_not_be_empty')}`,
                },
                {
                  max: 255,
                  message: `${t('car_class_is_too_long')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_car_class_name_on_english')}/>
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

export default AddClassComponent
