import { useState, useEffect } from 'react';
import { useTranslation } from 'react-i18next';
import styled from 'styled-components';
import { DatePicker, Select } from 'antd';
import moment from 'moment';
import cookies from 'js-cookie';
import { Form } from "antd";
import { Link } from "react-router-dom";
import LocationDataService from '../../../services/location/LocationDataService';

const { Option } = Select;

const FastSearchFormContainer = styled.div`
  margin-top: 51px;
  margin-bottom: 51px;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 15px 30px 30px 30px;
  color: #fff;
  width: 900px;

  .btn {
    width: 200px;
    font-size: 17px;
    color: #fff;
    background-color: #f44336;
    border: none;
  }

  .btn:hover {
    color: #fff;
    background-color: #ea5c52;
    border: none;
  }

  @media (max-width: 991px) { 
    margin-top: 0;
  }

  @media (max-width: 900px) { 
    width: 100%;
  }
`;

const SearchRow = styled.div`
  width: 100%;
  display: flex;

  @media (max-width: 600px) { 
    flex-direction: column;
    margin-bottom: 10px;
    align-items: center;
  }
`;

const SearchRowItem = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: left;
  margin: 5px;

  @media (max-width: 600px) { 
    margin: 0;
    width: 95%;
  }
`;

const SearchRowFormItem = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  text-align: left;
  margin: 5px;

  .ant-picker:hover, .ant-select:not(.ant-select-disabled):hover .ant-select-selector {
    border: 1px solid gray;
  }

  .ant-picker-focused, .ant-select-focused:not(.ant-select-disabled).ant-select:not(.ant-select-customize-input) .ant-select-selector {
    border: 1px solid gray;
    box-shadow: none;
  }

  label {
    color: #fff;
    text-align: center;
  }
`;

const SubTitle = styled.h6`
  margin: 0;
  color: #fff;
  font-size: 20px;
  line-height: 1.7;
  padding-bottom: 10px;
  font-weight: 400;

  @media (max-width: 543px) { 
    font-size: 16px;
  }
`;

const Title = styled.h2`
  margin: 0;
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  line-height: 1.7;

  @media (max-width: 526px) { 
    font-size: 34px;
  }

  @media (max-width: 400px) { 
    font-size: 24px;
  }
`;

const FormLabel = styled.div`
  font-size: 16px;
  color: #fff;
  text-align: center;
  margin-bottom: 5px;
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

const FastSearchFormComponent = (props) => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [form] = Form.useForm();
  const [data, setData] = useState([]);
  const [state, setState] = useState({
    locationName: null,
    pickUpDateTime: null,
    returnDateTime: null
  });

  const fetchLocations = async () => {
    const res = await LocationDataService.findAllForSelect().catch((err) => {
      console.log(err.response)
    });

    if (res) {
      setData(res.data);
    }
  };

  useEffect(() => {
    fetchLocations();
  }, [currentLanguage]);

  function disabledDate(current) {
    return current && current < moment().startOf("hour");
  }

  function disabledReturnDate(current) {
    if (state.pickUpDateTime) {
      return current && current < state.pickUpDateTime;
    } else {
      return current && current < moment().endOf("hour");
    }
  }

  function handleLocationChange(value) {
    console.log(value);
    if (value == 0) {
      setState(prevState => ({
        ...prevState,
        locationName: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        locationName: value
      }))
    )
  }

  function handlePickDateChange(value) {
    if (null == value) {
      setState(prevState => ({
        ...prevState,
        pickUpDateTime: null
      }))
    } else if (value >= state.returnDateTime) {
      setState(prevState => ({
        ...prevState,
        pickUpDateTime: value._d,
        returnDateTime: null
      }))
      form.resetFields(["returnDateTime"]);
    } else {
      setState(prevState => ({
        ...prevState,
        pickUpDateTime: value._d
      }))
    }
  }

  function handleReturnDateChange(value) {
    if (null == value) {
      setState(prevState => ({
        ...prevState,
        returnDateTime: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        returnDateTime: value._d
      }))
    )
  }

  return (
    <FastSearchFormContainer>
      <Title>{t('car_rental_in_belarus')}</Title>
      <SubTitle>{t('use_the_quick_search')}</SubTitle>
      <Form form={form}>
        <SearchRow>
          <SearchRowItem>
            <Form.Item name = "locationName">
              <SearchRowFormItem>
                <FormLabel>{t('pick_up_location')}</FormLabel>
                <Select
                  size="large"
                  showSearch
                  placeholder = {t('pick_location')}
                  optionFilterProp = "children"
                  onChange={handleLocationChange}
                  filterOption = {
                    (input, option) => option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                  }
                >
                  <Option value="0">{t('any_location')}</Option>
                  {data && !(data && data.length === 0) && data?.map((row) => (
                      <Option key={row.id} value={row.name}>{row.name}</Option>
                  ))}
                </Select>
              </SearchRowFormItem>
            </Form.Item>
          </SearchRowItem>
          <SearchRowItem>
            <Form.Item name="pickUpDateTime">
              <SearchRowFormItem>
                <FormLabel>{t('pick_up_date')}</FormLabel>
                <DatePicker 
                  showNow={false}
                  size="large"
                  disabledDate={disabledDate}
                  format="YYYY-MM-DD HH:00"
                  showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
                  onChange={handlePickDateChange}
                  placeholder={t('select_date')}
                />
              </SearchRowFormItem>
            </Form.Item>
          </SearchRowItem>
          <SearchRowItem>
            <Form.Item name="returnDateTime">
              <SearchRowFormItem>
                <FormLabel>{t('return_date')}</FormLabel>
                  <DatePicker
                    showNow={false}
                    size="large"
                    disabledDate={disabledReturnDate}
                    format="YYYY-MM-DD HH:00"
                    showTime={{ defaultValue: moment('12:00', 'HH:mm') }}
                    onChange={handleReturnDateChange}
                    placeholder={t('select_date')}
                  />
              </SearchRowFormItem>
            </Form.Item>
          </SearchRowItem>
        </SearchRow>
        <Link
          className="btn"
          to={{
            pathname: "/search",
            state
          }}
        >
          {t('search')}
        </Link>
      </Form>
    </FastSearchFormContainer>
  )
}

export default FastSearchFormComponent