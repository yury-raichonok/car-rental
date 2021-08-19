import React, { useState, useEffect } from 'react';
import styled from "styled-components";
import { Select, Collapse, Checkbox, InputNumber , DatePicker } from 'antd';
import BrandDataService from '../../services/brand/BrandDataService';
import CarClassDataService from '../../services/carClass/CarClassDataService';
import LocationDataService from '../../services/location/LocationDataService';
import moment from 'moment';
import { Form } from "antd";
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';
import cookies from 'js-cookie';

const { Option } = Select;
const { Panel } = Collapse;

const FilterContainer = styled.div`
  margin-top: 22px;
  width: 100%;
  display: flex;
  flex-direction: column;

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

  @media (max-width: 1260px) { 
    margin-top: 10px;
  }
`;

const FilterRow = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
`;

const FilterColumn = styled.div`
  width: 100%;
  display: flex;
  text-align: left;
  flex-direction: column;
  justify-content: space-around;
  margin-bottom: 5px;

  .ant-collapse .ant-collapse-item .ant-collapse-content .ant-collapse-content-box {
    margin: 0;
    padding: 0;
  }

  .ant-form-item {
    margin: 0;
  }

  .ant-picker {
    width: 100%;
  }
`;

const FilterLable = styled.span`
  width: 100%;
  display: flex;
  justify-content: center;
  font-size: 15px;
  margin-bottom: 3px;
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

const FilterComponent = (props) => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [form] = Form.useForm();
  const [brands, setBrands] = useState([]);
  const [carClasses, setCarClass] = useState([]);
  const [locations, setLocations] = useState([]);

  const fetchData = async () => {
    const brandResp = await BrandDataService.getAllWithRentalOffers().catch((err) => {
      console.log("Error: ", err);
    });
    const locationResp = await LocationDataService.findAllForSelect().catch((err) => {
      console.log("Error: ", err);
    });
    const carClassResp = await CarClassDataService.getAll().catch((err) => {
      console.log("Error: ", err);
    });

    if(brandResp && locationResp && carClassResp) {
      setBrands(brandResp.data);
      setLocations(locationResp.data);
      setCarClass(carClassResp.data);
    }
  }

  function disabledDate(current) {
    return current && current < moment().startOf("hour");
  }

  function disabledReturnDate(current) {
    if (props.state.pickUpDateTime) {
      return current && current < props.state.pickUpDateTime;
    } else {
      return current && current < moment().endOf("hour");
    }
  }

  function handlePickDateChange(value) {
    props.handlePickDateChange(value);
    if (value >= props.state.returnDateTime) {
      form.resetFields(["returnDateTime"]);
    }
  }

  useEffect(() => {
    fetchData();
    props.fetchCars();
    {
      props.location && props.location.state && props.location.state.brand && props.fetchModels(props.location.state.brand)
    }
  }, [currentLanguage]);

  return (
    <FilterContainer>
      <Form form={form}>
        <FilterColumn>
          <FilterLable>{t('pick_up_place')}</FilterLable>
          {props.location && props.location.state && props.location.state.locationName ? 
          (
            <Select defaultValue={props.location.state.locationName} onChange={props.handleLocationChange}>
              <Option key="0" value="0">{t('any_location')}</Option>
              {locations && locations?.map((location) => (
                <Option key={location.id} value={location.id}>{location.name}</Option>
              ))}
            </Select>
          ) : (
            <Select placeholder={t('pick_location')} onChange={props.handleLocationChange}>
              <Option key="0" value="0">{t('any_location')}</Option>
              {locations && locations?.map((location) => (
                <Option key={location.id} value={location.id}>{location.name}</Option>
              ))}
            </Select>
          )}
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('pick_up_date')}</FilterLable>
          {props.location && props.location.state && props.location.state.pickUpDateTime ? 
          (
            <DatePicker 
              showNow={false}
              defaultValue={moment(new Date(props.location.state.pickUpDateTime))} 
              onChange={handlePickDateChange}
              disabledDate={disabledDate}
              format="YYYY-MM-DD HH:00"
              showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
            />
          ) : (
            <DatePicker 
              showNow={false}
              placeholder={t('pick_up_date')} 
              onChange={handlePickDateChange} 
              disabledDate={disabledDate}
              format="YYYY-MM-DD HH:00"
              showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
            />
          )}
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('return_date')}</FilterLable>
          {props.location && props.location.state && props.location.state.returnDateTime ? 
          (
            <Form.Item name="returnDateTime">
              <DatePicker 
                showNow={false}
                defaultValue={moment(new Date(props.location.state.returnDateTime))} 
                onChange={props.handleReturnDateChange} 
                disabledDate={disabledReturnDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
              />  
            </Form.Item>
            
          ) : (
            <Form.Item name="returnDateTime">
              <DatePicker 
                showNow={false}
                placeholder={t('return_date')}
                onChange={props.handleReturnDateChange} 
                disabledDate={disabledReturnDate}
                format="YYYY-MM-DD HH:00"
                showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
              />
            </Form.Item>
            
          )}
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('class')}</FilterLable>
          <Select placeholder={t('car_class')} onChange={props.handleCarClassChange}>
            <Option key="0" value="0">{t('any_class')}</Option>
            {carClasses && carClasses?.map((carClass) => (
              <Option key={carClass.id} value={carClass.id}>{carClass.name}</Option>
            ))}
          </Select>
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('brand')}</FilterLable>
          {props.location && props.location.state && props.location.state.brand ? 
          (
            <Select defaultValue={props.location.state.brand} onChange={props.handleBrandChange}>
              <Option key="0" value="0">{t('any_brand')}</Option>
              {brands?.map((brand) => (
                <Option key={brand.id} value={brand.name}>{brand.name}</Option>
              ))}
            </Select>
          ) : (
            <Select placeholder={t('car_brand')} onChange={props.handleBrandChange}>
              <Option key="0" value="0">{t('any_brand')}</Option>
              {brands && brands?.map((brand) => (
                <Option key={brand.id} value={brand.name}>{brand.name}</Option>
              ))}
            </Select>
          )}
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('model')}</FilterLable>
          {props.child && props.child() }
        </FilterColumn>
        <FilterColumn>
          <FilterLable>{t('cost_per_hour')}</FilterLable>
          <FilterRow>
            <InputNumber
              placeholder={t('From')}
              style={{
                width: '100%',
              }}
              min="0"
              max={props.state.costTo}
              onChange={props.handleCostFrom}
              stringMode
            />
            <Marginer direction="horizontal" margin={20} />
            <InputNumber
              placeholder={t('to')}
              style={{
                width: '100%',
              }}
              min={props.state.costFrom}
              onChange={props.handleCostTo}
              stringMode
            />
          </FilterRow>
        </FilterColumn>
        <FilterColumn>
          <Collapse ghost>
            <Panel header={t('additional_options')} key="1">
              <FilterColumn>
                <FilterLable>{t('year_of_issue')}</FilterLable>
                <FilterRow>
                  <InputNumber
                    placeholder={t('From')}
                    style={{
                      width: '100%',
                    }}
                    min="1980"
                    max={props.state.yearTo}
                    onChange={props.handleYearFrom}
                    stringMode
                  />
                  <Marginer direction="horizontal" margin={20} />
                  <InputNumber
                    placeholder={t('to')}
                    style={{
                      width: '100%',
                    }}
                    min={props.state.yearFrom}
                    max={new Date().getFullYear()}
                    onChange={props.handleYearTo}
                    stringMode
                  />
                </FilterRow>
              </FilterColumn>
              <FilterColumn>
                <FilterLable>{t('body_type')}</FilterLable>
                <Select placeholder={t('body_type')} onChange={props.handleBodyType}>
                  <Option value="10">{t('any_type')}</Option>
                  <Option value="0">{t('hatchback')}</Option>
                  <Option value="1">{t('sedan')}</Option>
                  <Option value="2">{t('muv_suv')}</Option>
                  <Option value="3">{t('coupe')}</Option>
                  <Option value="4">{t('convertible')}</Option>
                  <Option value="5">{t('wagon')}</Option>
                  <Option value="6">{t('van')}</Option>
                  <Option value="7">{t('jeep')}</Option>
                </Select>
              </FilterColumn>
              <FilterColumn>
                <FilterLable>{t('engine_type')}</FilterLable>
                <Select placeholder={t('engine_type')} onChange={props.handleEngineType}>
                  <Option value="10">{t('any_type')}</Option>
                  <Option value="0">{t('diesel')}</Option>
                  <Option value="1">{t('petrol')}</Option>
                  <Option value="2">{t('hybrid')}</Option>
                  <Option value="3">{t('electro')}</Option>
                </Select>
              </FilterColumn>
              <Marginer direction="vertical" margin={8} />
              <FilterColumn>
                <Checkbox onChange={props.handleAirConditioner}>{t('air_conditioner')}</Checkbox>
              </FilterColumn>
              <Marginer direction="vertical" margin={8} />
              <FilterColumn>
                <Checkbox onChange={props.handleAutomaticTransmission}>{t('automatic_transmission')}</Checkbox>
              </FilterColumn>
            </Panel>
          </Collapse>
        </FilterColumn>
        <Marginer direction="vertical" margin={5} />
        <button className="btn" onClick={props.handlefilter}>{t('filter')}</button>
      </Form>
    </FilterContainer>
  )
}

export default FilterComponent
