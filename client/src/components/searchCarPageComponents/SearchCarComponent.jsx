import React, { useState } from 'react';
import styled from "styled-components";
import BigCarCard from '../cards/carCard/BigCarCard';
import 'antd/dist/antd.css';
import { Drawer, Spin, Pagination, Select, Button } from 'antd';
import FilterComponent from './FilterComponent';
import CarDataService from '../../services/car/CarDataService';
import ModelDataService from '../../services/model/ModelDataService';
import ArrowUp from "@kiwicom/orbit-components/lib/icons/ArrowUp";
import ArrowDown from "@kiwicom/orbit-components/lib/icons/ArrowDown";
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';
import MenuKebab from "@kiwicom/orbit-components/lib/icons/MenuKebab";

const { Option } = Select;

const ComponentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

const SortingContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 15px;

  .ant-select {
    font-size: 15px;
    width: 80px;
  }

  .ant-btn {
    font-size: 15px;
  }
`;

const ChildContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;

  .ant-select {
    width: 100%;
  }
`;

const SearchWrapper = styled.div`
  margin-top: 51px;
  width: 1400px;
  min-height: calc( 100vh - 301px );
  display: flex;

  .drawer-container {
    .ant-drawer-body {
      padding: 0px;
    }
  }

  @media (max-width: 1400px) { 
    width: 100%;
  }

  @media (max-width: 991px) { 
    margin-top: 0;
    min-height: calc( 100vh - 306px );
  }
`;

const FilterContainer = styled.div`
  width: 350px;
  display: flex;

  .ant-collapse-borderless {
    background-color: #fff;
  }

  @media (max-width: 1260px) { 
    display: none;
  }
`;

const DataContainer = styled.div`
  display: flex;
  flex-direction: column;
  height: auto;
  width: calc(100% - 350px);

  .ant-pagination {
    margin-top: 10px;
  }

  .ant-pagination-item-active {
    border-color: gray;
  }

  .ant-pagination-item:hover {
    border-color: #ea5c52;
  }

  .ant-pagination-next{
    align-items: center;
  }

  .ant-pagination-prev button, .ant-pagination-next button {
    &:hover {
      border-color: #ea5c52;
      color: #ea5c52;
    }
  }

  .ant-table-column-sorter-up.active, .ant-table-column-sorter-down.active {
    color: #ea5c52;
  }

  .ant-select-focused:not(.ant-select-disabled).ant-select:not(.ant-select-customize-input) .ant-select-selector {
    border-color: #ea5c52;
    box-shadow: none;
  }

  .ant-select:not(.ant-select-disabled):hover .ant-select-selector {
    border-color: #ea5c52;
  }

  @media (max-width: 1260px) { 
    width: 100%;
    align-items: center;
  }

  @media (max-width: 420px) { 
    width: calc(100% - 50px);
  }
`;

const WarningText = styled.h3`
  color: rgba(100, 100, 100);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const ShowMenuButton = styled.button`
  width: 50px;
  min-height: calc( 100vh - 306px );
  display: none;

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

  @media (max-width: 1260px) { 
    display: block
  }
`;

const SearchCarComponent = (props) => {

  const { t } = useTranslation();

  const [ascSort, setAscSort] = useState(true);
  const [cars, setCars] = useState([]);
  const [drawer, setDrawer] = useState(false); 
  const [isLoading, setLoading] = useState(false);
  const [models, setModels] = useState([]);
  const [state, setState] = useState( (props.location.state) ? ({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "costPerHour",
    locationName: props.location.state.locationName,
    pickUpDateTime: props.location.state.pickUpDateTime,
    returnDateTime: props.location.state.returnDateTime,
    brandName: props.location.state.brand,
    modelName: null,
    carClass: null,
    costFrom: null,
    costTo: null,
    yearFrom: null,
    yearTo: null,
    bodyType: null,
    engineType: null,
    airConditioner: false,
    autoTransmission: false,
    inRental: true,
  }) : ({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "costPerHour",
    locationName: null,
    pickUpDateTime: null,
    returnDateTime: null,
    brandName: null,
    modelName: null,
    carClass: null,
    costFrom: null,
    costTo: null,
    yearFrom: null,
    yearTo: null,
    bodyType: null,
    engineType: null,
    airConditioner: false,
    autoTransmission: false,
    inRental: true,
  })
)

const fetchCars = async () => {
  setLoading(true);
  const resp = await CarDataService.getFilteredCars(state).catch((err) => {
    console.log("Error: ", err);
  });

  if(resp) {
    console.log(resp)
    setCars(resp.data.content);
    setState(prevState => ({
      ...prevState,
      totalElements: `${resp.data.totalElements}`
    }))
  }

  setLoading(false);
}

const fetchModels = async (value) => {
  const resp = await ModelDataService.getModelsByBrandName(value).catch((err) => {
    console.log("Error: ", err);
  });

  if(resp) {
    setModels(resp.data);
  }
}

  const isCarsEmpty = !cars || (cars && cars.length === 0);

  function handlePickDateChange(value) {
    if (null == value) {
      setState(prevState => ({
        ...prevState,
        pickUpDateTime: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        pickUpDateTime: value._d
      }))
    )
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

  function handleCarClassChange(value) {
    if (value === 0) {
      setState(prevState => ({
        ...prevState,
        carClass: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        carClass: value
      }))
    ) 
  }

  function handleModelChange(value) {
    if (value === "0") {
      setState(prevState => ({
        ...prevState,
        modelName: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        modelName: value
      }))
    )
  }

  function handleLocationChange(value) {
    if (value === 0) {
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

  function handleBrandChange(value) {
    console.log(value);
    if (value === "0") {
      setState(prevState => ({
        ...prevState,
        brandName: null,
        modelName: null
      }))
      setModels([]);
    } else (
      setState(prevState => ({
        ...prevState,
        brandName: value,
        modelName: null
      }))
    )
    if (value !== "0") {
      fetchModels(value)
    }
  }

  function handleCostFrom(value) {
    setState(prevState => ({
      ...prevState,
      costFrom: value
    }))
  }

  function handleCostTo(value) {
    setState(prevState => ({
      ...prevState,
      costTo: value
    }))
  }

  function handleYearFrom(value) {
    setState(prevState => ({
      ...prevState,
      yearFrom: value
    }))
  }

  function handleYearTo(value) {
    setState(prevState => ({
      ...prevState,
      yearTo: value
    }))
  }

  function handleBodyType(value) {
    if (value === 10) {
      setState(prevState => ({
        ...prevState,
        bodyType: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        bodyType: value
      }))
    )
  }

  function handleEngineType(value) {
    if (value === 10) {
      setState(prevState => ({
        ...prevState,
        engineType: null
      }))
    } else (
      setState(prevState => ({
        ...prevState,
        engineType: value
      }))
    )
  }

  function handleAirConditioner(value) {
    setState(prevState => ({
      ...prevState,
      airConditioner: !state.airConditioner
    }))
  }

  function handleAutomaticTransmission(value) {
    setState(prevState => ({
      ...prevState,
      autoTransmission: !state.autoTransmission
    }))
  }

  function handlefilter(value) {
    setState(prevState => ({
      ...prevState,
      pageNumber: 0,
      pageSize: 10
    }))
    fetchCars();
  }

  function handleSortBy(value) {
    state.sortBy = value;
    fetchCars();
  }

  function handleSortDirection() {
    setAscSort(!ascSort);
    const sort = !ascSort;
    {sort ? state.sortDirection = "asc" : state.sortDirection = "desc"}    
    fetchCars();
  }

  function onChange(current, pageSize) {
    state.pageNumber = current - 1;
    state.pageSize = pageSize;
    fetchCars();
  }


  const showDrawer = () => {
    setDrawer(true);
  };

  const onClose = () => {
    setDrawer(false);
  };

  const drawerStyle = {
    padding: "0 10px 20px 10px",
  }

  let component = () => ( 
    <ChildContainer>
      {state.modelName ? 
      (
        <Select value={state.modelName} onChange={handleModelChange}>
          <Option key="0" value="0">{t('any_model')}</Option>
          {models?.map((model) => (
            <Option key={model.id} value={model.name}>{model.name}</Option>
          ))}
        </Select>
      ) : (
        <Select placeholder={t('car_model')} onChange={handleModelChange}>
          <Option key="0" value="0">{t('any_model')}</Option>
          {models && models?.map((model) => (
            <Option key={model.id} value={model.name}>{model.name}</Option>
          ))}
        </Select>
      )}
    </ChildContainer>    
  );

  return (
    <div>
      <ComponentContainer>
        <SearchWrapper>
          <ShowMenuButton onClick={showDrawer}>
            <MenuKebab />
          </ShowMenuButton>
          <Drawer
              placement="left"
              closable={false}
              onClose={onClose}
              visible={drawer}
              key="left"
              bodyStyle = {drawerStyle}
              width = "270px"
            >
              <FilterComponent 
                models={models}
                state={state}
                child={component}
                fetchCars={fetchCars}
                fetchModels={fetchModels}
                handleLocationChange={handleLocationChange}
                handlePickDateChange={handlePickDateChange}
                handleReturnDateChange={handleReturnDateChange}
                handleCarClassChange={handleCarClassChange}
                handleModelChange={handleModelChange}
                handleBrandChange={handleBrandChange}
                handleCostFrom={handleCostFrom}
                handleCostTo={handleCostTo}
                handleYearFrom={handleYearFrom}
                handleYearTo={handleYearTo}
                handleBodyType={handleBodyType}
                handleEngineType={handleEngineType}
                handleAirConditioner={handleAirConditioner}
                handleAutomaticTransmission={handleAutomaticTransmission}
                handlefilter={handlefilter}
                {...props} 
              />
            </Drawer>
          <Marginer direction="horizontal" margin={10} />
          <FilterContainer>  
            <FilterComponent 
              models={models}
              state={state}
              child={component}
              fetchCars={fetchCars}
              fetchModels={fetchModels}
              handleLocationChange={handleLocationChange}
              handlePickDateChange={handlePickDateChange}
              handleReturnDateChange={handleReturnDateChange}
              handleCarClassChange={handleCarClassChange}
              handleModelChange={handleModelChange}
              handleBrandChange={handleBrandChange}
              handleCostFrom={handleCostFrom}
              handleCostTo={handleCostTo}
              handleYearFrom={handleYearFrom}
              handleYearTo={handleYearTo}
              handleBodyType={handleBodyType}
              handleEngineType={handleEngineType}
              handleAirConditioner={handleAirConditioner}
              handleAutomaticTransmission={handleAutomaticTransmission}
              handlefilter={handlefilter}
              {...props} 
            />
          </FilterContainer>
          <Marginer direction="horizontal" margin={20} />
          <DataContainer>
            <SortingContainer>
              <div>{t('sort_by')}:</div>
              <Select 
                bordered={false} 
                showArrow={false} 
                defaultValue="costPerHour" 
                onChange={handleSortBy}
              >
                <Option value="costPerHour">{t('search_by_cost')}</Option>
                <Option value="model">{t('search_by_model')}</Option>
                <Option value="carClass">{t('search_by_class')}</Option>
              </Select>
              <Button
                type="text"
                onClick={handleSortDirection}
                icon={ascSort ? <ArrowUp /> : <ArrowDown />}
              ></Button>
            </SortingContainer>
            <Marginer direction="vertical" margin={17} />
            {isCarsEmpty && !isLoading && (<WarningText>{t('no_results')}</WarningText>)}
            { isLoading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
            {!isCarsEmpty && !isLoading && cars.map((car) => (
              <BigCarCard
                key = {car.id}
                id = {car.id}
                brand = {car.brand}
                model = {car.model}
                carClass = {car.carClass}
                yearOfIssue = {car.yearOfIssue}
                bodyType = {car.bodyType}
                transmissionType = {car.automaticTransmission}
                color = {car.color}
                engineType = {car.engineType}
                enginePower = {car.enginePower}
                passengersAmt = {car.passengersAmt}
                baggageAmt = {car.baggageAmt}
                hasConditioner = {car.hasConditioner}
                costPerHour = {car.costPerHour}
                locationName = {car.locationName}
                carImageLink = {car.carImageLink}
                pickUpDateTime = {state.pickUpDateTime}
                returnDateTime = {state.returnDateTime}
              />
            ))}
            <Pagination
              showSizeChanger
              defaultCurrent={1}
              defaultPageSize={state.pageSize}
              onChange={onChange}
              total={state.totalElements}
            />
            <Marginer direction="vertical" margin={20} />
          </DataContainer>
        </SearchWrapper>
      </ComponentContainer>
    </div>
  )
}

export default SearchCarComponent
