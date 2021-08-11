import React, { useState, useEffect } from 'react';
import { Table, Modal, Input, Select, notification, Image, Popconfirm } from 'antd';
import CarDataService from '../../../../services/car/CarDataService';
import AddCarComponent from './AddCarComponent';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import styled from 'styled-components';
import NoImage from '../../../../images/no-image.png';
import EditCarComponent from './EditCarComponent';
import EditCarImageComponent from './EditCarImageComponent';
import Marginer from '../../../marginer/Marginer';
import { useTranslation } from 'react-i18next';
import cookies from 'js-cookie';

const { Search } = Input;
const { Option } = Select;

const FunctionsContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  padding: 15px;

  .ant-input-group {
    input:hover, button:hover {
      border: 1px solid gray;
      box-shadow: none;
    }

    input:focus, button:focus {
      border: 1px solid gray;
      box-shadow: none;
    }
  }

  .ant-input-group-addon {
    &:hover {
      border: 1px solid gray;
      box-shadow: none;
    }

    &:focus {
      border: 1px solid gray;
      box-shadow: none;
    }
  }

  .btn {
    min-height: 32px;
    font-size: 14px;
    color: #000;
    background-color: #4842420f;
    border: none;
    border-radius: 0px;

    svg {
      height: 20px;
      padding-bottom: 2px;
    }

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  @media (max-width: 660px) { 
    flex-direction: column;
    align-items: flex-end;
    padding: 0;
  }
`;

const TableContainer = styled.div`

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
`;

const TableButton = styled.button`
  min-height: 32px;
  width: 100px;
  font-size: 13px;
  color: #000;
  background-color: #4842420f;
  border: none;

  &:hover {
    background-color: #ea5c52;
    color: #fff;
  }

  &:focus {
    box-shadow: none;
  }
`;

const SearchTitle = styled.div`
  @media (max-width: 450px) { 
    display: none;
  }
`;

const ButtonsContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;

  .ant-select {
    width: 100px;
  }

  @media (max-width: 660px) { 
    padding: 10px;
  }
`;

const SearchButtonsContainer = styled.div`
  display: flex;
  flex-direction: row;
  align-items: center;

  .ant-select {
    width: 100px;
  }

  @media (max-width: 660px) { 
    padding: 10px;
  }

  @media (max-width: 300px) { 
    flex-direction: column;
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

const AdminCarsComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [isCarModalVisible, setCarModalVisible] = useState(false);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [car, setCar] = useState();
  const [editCarModalVisible, setEditCarModalVisible] = useState(false);
  const [editImageModalVisible, setEditImageModalVisible] = useState(false);

  const [searchParameter, setSearchParameter] = useState("brandName");
  const [state] = useState({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "id",
    brandName: null,
    modelName: null,
    carClassName: null,
    locationName: null,
    vin: null,
    inRental: true,
  });

  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 10,
    total: 0,
  })

  const fetchCars = async () => {
    setLoading(true);

    const resp = await CarDataService.searchCarsByAdmin(state).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_cars_list')}`,
      });
      setLoading(false);
    });
  
    if(resp) {
      setLoading(false);
      setData(resp.data.content);
      setPagination({
        pageNumber: resp.data.pageable.pageNumber,
        pageSize: resp.data.pageable.pageSize,
        total: resp.data.totalElements,
      })
    }
  }

  const updateStatus = async (id) => {

    const resp = await CarDataService.updateStatus(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_car_status')}`,
      });
    });
  
    if(resp) {
      fetchCars();
      notification.success({
        message: `${t('status_of_car_successfully_changed')}`,
      });
    }
  }

  useEffect(() => {
    fetchCars();
  },[currentLanguage])

  function EditCarComponentFunction(props) {
    return <EditCarComponent data={props.data} setEditCarModalVisible={setEditCarModalVisible} fetchCars={fetchCars} />;
  }

  function EditCarImageComponentFunction(props) {
    return <EditCarImageComponent data={props.data} setEditImageModalVisible={setEditImageModalVisible} fetchCars={fetchCars} />
  }

  const CarsTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id,
    },
    { 
      title: `${t('brand')}`, 
      dataIndex: 'brand', 
      key: 'brand'
    },
    { 
      title: `${t('model')}`, 
      dataIndex: 'model', 
      key: 'model',
      sorter: (a, b) => a.model - b.model
    },
    { 
      title: `${t('vin')}`, 
      dataIndex: 'vin', 
      key: 'vin' 
    },
    { 
      title: `${t('location')}`, 
      dataIndex: 'locationName', 
      key: 'location'
    },
    { 
      title: `${t('class')}`, 
      dataIndex: 'carClass', 
      key: 'carClass',
      sorter: (a, b) => a.carClass - b.carClass
    },
    { 
      title: `${t('date_of_issue')}`,
      dataIndex: 'dateOfIssue', 
      key: 'dateOfIssue',
      sorter: (a, b) => a.dateOfIssue - b.dateOfIssue,
    },
    { 
      title: `${t('color')}`, 
      dataIndex: 'color', 
      key: 'color',
      render: (dataIndex) => 
        <div>
          {dataIndex === "Red" && `${t('red')}`}
          {dataIndex === "White" && `${t('white')}`}
          {dataIndex === "Black" && `${t('black')}`}
          {dataIndex === "Green" && `${t('green')}`}
          {dataIndex === "Grey" && `${t('grey')}`}
          {dataIndex === "Yellow" && `${t('yellow')}`}
          {dataIndex === "Blue" && `${t('blue')}`}
          {dataIndex === "Silver" && `${t('silver')}`}
          {dataIndex === "Pink" && `${t('pink')}`}
          {dataIndex === "Orange" && `${t('orange')}`}
          {dataIndex === "Brown" && `${t('brown')}`}
          {dataIndex === "Violet" && `${t('violet')}`}
          {dataIndex === "Gold" && `${t('gold')}`}
          {dataIndex === "Sand" && `${t('sand')}`}
          {dataIndex !== "Red" && 
            dataIndex !== "White" && 
            dataIndex !== "Black" && 
            dataIndex !== "Green" && 
            dataIndex !== "Grey" && 
            dataIndex !== "Yellow" && 
            dataIndex !== "Blue" && 
            dataIndex !== "Silver" && 
            dataIndex !== "Pink" && 
            dataIndex !== "Orange" && 
            dataIndex !== "Brown" && 
            dataIndex !== "Violet" && 
            dataIndex !== "Gold" && 
            dataIndex !== "Sand" && `${dataIndex}`}
        </div>, 
    },
    { 
      title: `${t('body_type')}`, 
      dataIndex: 'bodyType', 
      key: 'bodyType',
      sorter: (a, b) => a.bodyType - b.bodyType,
      render: (dataIndex) => 
        <div>
          {dataIndex === "Hatchback" && ` ${t('hatchback')}`}
          {dataIndex === "Sedan" && ` ${t('sedan')}`}
          {dataIndex === "MUV/SUV" && ` ${t('muv_suv')}`}
          {dataIndex === "Coupe" && ` ${t('coupe')}`}
          {dataIndex === "Convertible" && ` ${t('convertible')}`}
          {dataIndex === "Wagon" && ` ${t('wagon')}`}
          {dataIndex === "Van" && ` ${t('van')}`}
          {dataIndex === "Jeep" && ` ${t('jeep')}`}
        </div>, 
    },
    { 
      title: `${t('auto_transmission')}`, 
      dataIndex: 'automaticTransmission', 
      key: 'automaticTransmission',
      render: (dataIndex) => 
        <div>
          {dataIndex ? 
            `${t('yes')}`
            :
            `${t('no')}`
          }      
        </div>, 
    },
    { 
      title: `${t('engine_type')}`, 
      dataIndex: 'engineType', 
      key: 'engineType',
      sorter: (a, b) => a.engineType - b.engineType,
      render: (dataIndex) =>
        <div>
          {dataIndex === "Diesel" && ` ${t('diesel')}`}
          {dataIndex === "Petrol" && ` ${t('petrol')}`}
          {dataIndex === "Hybrid" && ` ${t('hybrid')}`}
          {dataIndex === "Electro" && ` ${t('electro')}`}   
        </div>
    },
    { 
      title: `${t('pas_amt')}`, 
      dataIndex: 'passengersAmt', 
      key: 'passengersAmt',
      sorter: (a, b) => a.passengersAmt - b.passengersAmt
    },
    { 
      title: `${t('bag_amt')}`, 
      dataIndex: 'baggageAmt', 
      key: 'baggageAmt',
      sorter: (a, b) => a.baggageAmt - b.baggageAmt
    },
    { 
      title: `${t('air_conditioner')}`, 
      dataIndex: 'hasConditioner', 
      key: 'hasConditioner',
      sorter: (a, b) => a.hasConditioner - b.hasConditioner,
      render: (dataIndex) => 
        <div>
          {dataIndex ? 
            `${t('yes')}`
            :
            `${t('no')}`
          }      
        </div>, 
    },
    { 
      title: `${t('cost')}`, 
      dataIndex: 'costPerHour', 
      key: 'cost',
      sorter: (a, b) => a.cost - b.cost
    },
    { 
      title: `${t('in_rental')}`, 
      dataIndex: 'inRental', 
      key: 'inRental',
      sorter: (a, b) => a.inRental - b.inRental,
      render: (_, record) => 
        <div>
          {record.inRental ? 
            <Popconfirm title={t('sure_to_remove_car_from_rental')} onConfirm={() => updateStatus(record.id)}>
              <a>{t('yes')}</a>
            </Popconfirm>
            :
            <Popconfirm title={t('sure_to_return_car_to_rental')} onConfirm={() => updateStatus(record.id)}>
              <a>{t('no')}</a>
            </Popconfirm>
          }      
        </div>, 
    },
    { 
      title: `${t('image')}`, 
      dataIndex: 'carImageLink', 
      key: 'id', 
      render: (dataIndex) => 
        <div>
          {dataIndex === null ? 
            <Image width={"3rem"} src={NoImage} />
            :
            <Image width={"3rem"} src={dataIndex} />
          }      
        </div>, 
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 120,
      render: (_, record) => 
        <div>
          <TableButton onClick={() => handleEditInfo(record)} >
            {t('edit_info')}
          </TableButton>
          <Marginer direction="vertical" margin={8} />
          <TableButton onClick={() => handleChangeImage(record)} >
            {t('edit_image')}
          </TableButton>
        </div>,
    },
  ];

  function handleTableChange(pagination, filter, sorter) {
    state.sortBy = sorter.field;
    if (sorter.order == "descend") {
      state.sortDirection = "desc";
    } else if (sorter.order == null) {
      state.sortDirection = "asc";
      state.sortBy = "id";
    } else {
      state.sortDirection = "asc";
    }
    state.pageNumber = pagination.current - 1;
    fetchCars();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    state.brandName = null;
    state.modelName = null;
    state.carClassName = null;
    state.locationName = null;
    state.vin = null;
    state.inRental = true;
    fetchCars();
  }

  const showCarModal = () => {
    setCarModalVisible(true);
  };

  const handleCarCancel = () => {
    setCarModalVisible(false);
  };

  const handleEditCarCancel = () => {
    setEditCarModalVisible(false);
  };

  const handleEditImageCancel = () => {
    setEditImageModalVisible(false);
  };

  const handleEditInfo = (e) => {
    setEditCarModalVisible(true);
    setCar(e);
  } 
  
  const handleChangeImage = (e) => {
    setEditImageModalVisible(true);
    setCar(e);
  } 

  const handleSortBy = value => {
    setSearchParameter(value)
  }

  const onSearch = value => {
    state.brandName = null;
    state.modelName = null;
    state.carClassName = null;
    state.locationName = null;
    state.vin = null;
    switch (searchParameter) {
      case "brandName":
        state.brandName = value;
        break;
      case "modelName":
        state.modelName = value;
        break;

      case "carClassName":
        state.carClassName = value;
        break;

      case "vin":
        state.vin = value;
        break;

      case "locationName":
        state.locationName = value;
        break;
                        
      default:
        break;
    }
    fetchCars();
  }

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={showCarModal}>
            {t('add_new_car')}
          </button>
          <Marginer direction="horizontal" margin={15} />
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
        <SearchButtonsContainer>
          <SearchTitle>{t('search_by')}:</SearchTitle>
          <Select 
            bordered={false} 
            showArrow={false} 
            defaultValue="brandName"
            onChange={handleSortBy}
          >
            <Option value="brandName" default>{t('search_by_brand')}</Option>
            <Option value="modelName">{t('search_by_model')}</Option>
            <Option value="carClassName">{t('search_by_class')}</Option>
            <Option value="vin">vin</Option>
            <Option value="locationName">{t('search_by_location')}</Option>
          </Select>
          <Search placeholder={t('input_search_text')} onSearch={onSearch} style={{ width: 200 }} />
        </SearchButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={CarsTableColumns} 
          dataSource={data} 
          rowKey={record => record.id} 
          pagination={pagination}
          loading={loading}
          onChange={handleTableChange}
          scroll={{ x: 1600 }}
        />
      </TableContainer>
      <Modal
        width = "900px"
        visible={isCarModalVisible}
        title={t('add_new_car')}
        onCancel={handleCarCancel}
        footer={[]}
      >
        <AddCarComponent handleCarCancel={handleCarCancel} fetchCars={fetchCars} />
      </Modal>
      <Modal
        width = "900px"
        visible={editCarModalVisible}
        title={t('edit_car_information')}
        onCancel={handleEditCarCancel}
        footer={[]}
      >
        <EditCarComponentFunction data={car} setEditCarModalVisible={setEditCarModalVisible} fetchCars={fetchCars} />
      </Modal>
      <Modal
        width = "600px"
        visible={editImageModalVisible}
        title={t('edit_car_image')}
        onCancel={handleEditImageCancel}
        footer={[]}
      >
        <EditCarImageComponentFunction data={car} setEditImageModalVisible={setEditImageModalVisible} fetchCars={fetchCars} />
      </Modal>
    </div>  
  )
}

export default AdminCarsComponent