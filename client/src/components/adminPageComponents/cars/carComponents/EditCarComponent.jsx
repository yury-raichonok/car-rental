import { useState, useEffect, createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, Select, DatePicker, notification, Switch } from 'antd';
import CarDataService from '../../../../services/car/CarDataService';
import BrandDataService from '../../../../services/brand/BrandDataService';
import ModelDataService from '../../../../services/model/ModelDataService';
import LocationDataService from '../../../../services/location/LocationDataService';
import CarClassDataService from '../../../../services/carClass/CarClassDataService';
import Marginer from '../../../marginer/Marginer';
import moment from 'moment';
import { useTranslation } from 'react-i18next';

const { Option } = Select;

const AddNewCarContainer = styled.div`
  width: 100%;
  display: flex;
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
    max-width: 202px;
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

    .ant-form-item {
      max-width: 100%;
    }
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

const SelectorRow = styled.div`
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

  .ant-row {
    width: 50px;
  }
`;

const ItemTitle = styled.div`
  font-size: 15px;
  margin-left: 10px;
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

const FormColumnContainer = styled.div`
  width: 32%;
  display: flex;
  flex-direction: column;

  .ant-picker {
    width: 100%;
  }

  @media (max-width: 900px) { 
    width: 100%;
  }
`;

const EditCarComponent = (props) => {

  const { t } = useTranslation();

  const [brands, setBrands] = useState([]);
  const [models, setModels] = useState([]);
  const [locations, setLocations] = useState([]);
  const [carClasses, setCarClasses] = useState([]);
  const formRef = createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const fetchData = async () => {

    const brandsResp = await BrandDataService.getAll().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_car_brands')}`,
      });
    });

    const locationResp = await LocationDataService.findAllForSelect().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_locations')}`,
      });
    });

    const carClassResp = await CarClassDataService.getAll().catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_car_classes')}`,
      });
    });

    if(brandsResp && locationResp && carClassResp) {
      switch(brandsResp.status){
        case 204:
          notification.error({
            message: `${t('no_specified_car_brand')}`,
          });
          break;
      }
      switch(locations.status){
        case 204:
          notification.error({
            message: `${t('no_specified_locations')}`,
          });
          break;
      }
      switch(carClassResp.status){
        case 204:
          notification.error({
            message: `${t('no_specified_car_classes')}`,
          });
          break;
      }
      setBrands(brandsResp.data);
      setLocations(locationResp.data);
      setCarClasses(carClassResp.data);
    } else {
      notification.error({
        message: `${t('error_when_fetching_car_data')}`,
      });
    }
  }

  const fetchModels = async (brandId) => {
    const modelResp = await ModelDataService.getModelsByBrandName(brandId).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_car_models')}`,
      });
    });

    if(modelResp) {
      switch(modelResp.status){
        case 204:
          notification.error({
            message: `${t('no_specified_car_models')}`,
          });
          break;
      }
      setModels(modelResp.data);
    } else {
      notification.error({
        message: `${t('error_when_fetching_models_data')}`,
      });
    }
  }

  useEffect(() => {
    fetchData();
    fetchModels(props.data.brand)
  }, []);

  function handleBrandFilter(value) {
    fetchModels(value);
    formRef.current.resetFields(["modelId"]);
  };

  function disabledDate(current) {
    return current && current > moment().endOf('day');
  }

  const onFinish = (values) => {    
    CarDataService.update(props.data.id, values).then(
      res => {
        onReset();
        props.setEditCarModalVisible(false);
        props.fetchCars();
        notification.success({
          message: `${t('car_successfully_updated')}`,
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('car_with_the_same_vin_already_exists')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('error_when_updating_car')}`,
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
            <FormColumnContainer>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('brand')}:</RowTitle>
                </Column>
                <Form.Item name="brand"
                  initialValue = {props.data.brand}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_car_brand')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_car_brand')} onChange={handleBrandFilter}>
                    {brands.length !== 0 && brands.map((brand) => (
                      <Option key={brand.id} value={brand.name}>{brand.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('model')}:</RowTitle>
                </Column>
                <Form.Item name="model"
                initialValue = {props.data.model} 
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_car_model')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_car_model')}>
                    {models.length !== 0 && models.map((model) => (
                      <Option key={model.id} value={model.name}>{model.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('location')}:</RowTitle>
                </Column>
                <Form.Item name="location" 
                  initialValue = {props.data.locationId}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_car_location')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_car_location')}>
                    {locations.length !== 0 && locations.map((location) => (
                      <Option key={location.id} value={location.id}>{location.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('class')}:</RowTitle>
                </Column>
                <Form.Item name="carClass" 
                  initialValue = {props.data.carClassId}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_car_class')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_car_class')}>
                    {carClasses.length !== 0 && carClasses.map((carClass) => (
                      <Option key={carClass.id} value={carClass.id}>{carClass.name}</Option>
                    ))}
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('vin')}:</RowTitle>
                </Column>
                <Form.Item name="vin" 
                  initialValue = {props.data.vin}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_vin')}`,
                    },
                    {
                      whitespace: true,
                      message: `${t('vin_can_not_be_empty')}`,
                    },
                    // {
                    //   pattern: "^[A-HJ-NPR-Za-hj-npr-z\\d]{8}[\\dX][A-HJ-NPR-Za-hj-npr-z\\d]{2}\\d{6}$",
                    //   message: 'Car VIN incorrect!',
                    // },
                  ]}
                >
                  <Input type="text" autoComplete="off" placeholder={t('input_car_vin')}/>
                </Form.Item>
              </Row>
            </FormColumnContainer>
            <FormColumnContainer>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('issued_at')}:</RowTitle>
                </Column>
                <Form.Item name="dateOfIssue" 
                  initialValue = {moment(new Date(props.data.dateOfIssue))}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_date_of_issue')}`,
                    },
                  ]}
                >
                  <DatePicker disabledDate={disabledDate} format="DD.MM.YYYY" />
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('color')}:</RowTitle>
                </Column>
                <Form.Item name="color"
                  initialValue = {props.data.color}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_car_color')}`,
                    },
                    {
                      whitespace: true,
                      message: `${t('color_can_not_be_empty')}`,
                    },
                    {
                      max: 255,
                      message: `${t('color_is_too_long')}`,
                    },
                  ]}
                >
                  <Input autoComplete="off" placeholder={t('input_color')} />
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('body')}:</RowTitle>
                </Column>
                <Form.Item name="bodyType"
                  initialValue = {props.data.bodyType.toUpperCase()}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_body_type')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_body_type')}>
                    <Option value="0">{t('hatchback')}</Option>
                    <Option value="1">{t('sedan')}</Option>
                    <Option value="2">{t('muv_suv')}</Option>
                    <Option value="3">{t('coupe')}</Option>
                    <Option value="4">{t('convertible')}</Option>
                    <Option value="5">{t('wagon')}</Option>
                    <Option value="6">{t('van')}</Option>
                    <Option value="7">{t('jeep')}</Option>
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('engine')}:</RowTitle>
                </Column>
                <Form.Item name="engineType"
                  initialValue = {props.data.engineType.toUpperCase()}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_select_engine_type')}`,
                    },
                  ]}
                >
                  <Select placeholder={t('select_engine_type')}>
                    <Option value="0">{t('diesel')}</Option>
                    <Option value="1">{t('petrol')}</Option>
                    <Option value="2">{t('hybrid')}</Option>
                    <Option value="3">{t('electro')}</Option>
                  </Select>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('pass_amt')}:</RowTitle>
                </Column>
                <Form.Item name="passengersAmt" 
                  initialValue = {props.data.passengersAmt}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_max_pass_amt')}`,
                    },
                  ]}
                >
                  <Input type="number" min="1" autoComplete="off" placeholder={t('passengers_amount')} />
                </Form.Item>
              </Row>
            </FormColumnContainer>
            <FormColumnContainer>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('bag_amt')}:</RowTitle>
                </Column>
                <Form.Item name="baggageAmt" 
                  initialValue = {props.data.baggageAmt}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_max_bag_amt')}`,
                    },
                  ]}
                >
                  <Input type="number" min="1" autoComplete="off" placeholder={t('baggage_amount')} />
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('cost_p_h')}:</RowTitle>
                </Column>
                <Form.Item name="costPerHour" 
                  initialValue = {props.data.costPerHour}
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_cost')}`,
                    },
                  ]}
                >
                  <Input type="number" min="1" autoComplete="off" placeholder={t('cost_per_hour')} />
                </Form.Item>
              </Row>
              <SelectorRow>
                <Form.Item
                  initialValue={props.data.automaticTransmission}
                  name="autoTransmission"
                  valuePropName="checked"
                >
                  {props.data.automaticTransmission ? (
                    <Switch checked={true} /> 
                  ) : (
                    <Switch /> 
                  )}
                </Form.Item>
                <Column>
                  <Marginer direction="vertical" margin={5} />
                  <ItemTitle>{t('automatic_transmission')}</ItemTitle>
                </Column>
              </SelectorRow>
              <SelectorRow>
                <Form.Item
                  initialValue={props.data.hasConditioner}
                  name="hasConditioner"
                >
                  {props.data.hasConditioner ? (
                    <Switch checked={true} /> 
                  ) : (
                    <Switch /> 
                  )}
                </Form.Item>
                <Column>
                  <Marginer direction="vertical" margin={5} />
                  <ItemTitle>{t('has_air_conditioner')}</ItemTitle>
                </Column>
              </SelectorRow>
              <Row>
                <button type="submit" className="btn">
                  {t('submit')}
                </button>
                <button type="button" className="btn" onClick={onReset}>
                  {t('reset')}
                </button>
              </Row>
            </FormColumnContainer>
          </AddNewCarContainer>
        </Form>
    </AddNewCarContainer>
  )
}

export default EditCarComponent
