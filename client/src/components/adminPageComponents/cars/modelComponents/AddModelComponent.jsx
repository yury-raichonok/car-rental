import { useState, useEffect, createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, Select, notification } from 'antd';
import BrandDataService from '../../../../services/brand/BrandDataService';
import ModelDataService from '../../../../services/model/ModelDataService';
import Marginer from '../../../marginer/Marginer';
import { useTranslation } from 'react-i18next';

const { Option } = Select;

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
  width: 70px;
  text-align: left;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const AddModelComponent = (props) => {

  const { t } = useTranslation();
  
  const [brands, setBrands] = useState([]);
  const formRef = createRef();

  const fetchBrands = async () => {
    const brandsResp = await BrandDataService.getAll().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_car_brands')}`,
      });
    });

    if(brandsResp) {
      switch(brandsResp.status){
        case 204:
          notification.error({
            message: `${t('no_specified_car_brands')}`,
            description: `${t('to_add_new_car_first_add_new_car_brand')}`,
          });
          break;
      }
      setBrands(brandsResp.data);
    }
  }

  useEffect(() => {
    fetchBrands();
  }, []);

  const onReset = () => {
    formRef.current.resetFields();
  };

  const onFinish = (values) => {
    ModelDataService.create(values).then(
      res => {
        onReset();
        props.handleModelCancel();
        props.fetchModels();
        notification.success({
          message: `${t('new_car_model_added_successfully')}`,
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('model_with_the_same_name_already_exists')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('error_when_adding_a_new_model')}`,
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
              <RowTitle>{t('brand')}:</RowTitle>
            </Column>
            <Form.Item name="brandId" 
              rules={[
                {
                  required: true,
                  message: `${t('please_select_car_brand')}`,
                },
              ]}
            >
              <Select placeholder={t('select_car_brand')} >
                {brands.length !== 0 && brands.map((brand) => (
                  <Option key={brand.id} value={brand.id}>{brand.name}</Option>
                ))}
              </Select>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('entity_name')}:</RowTitle>
            </Column>
            <Form.Item name="name" 
              rules={[
                {
                  required: true,
                  message: `${t('please_input_model_name')}`,
                },
                {
                  whitespace: true,
                  message: `${t('model_name_can_not_be_empty')}`,
                },
                {
                  max: 30,
                  message: `${t('model_name_is_to_long')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder = {t('input_model_name')} />
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

export default AddModelComponent
