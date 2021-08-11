import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Input, Select, Table, notification } from 'antd';
import RepairBillDataService from '../../../services/bill/RepairBillDataService';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
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
    height: 32px;
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

const AdminBillsComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [searchParameter, setSearchParameter] = useState("email");

  const [state, setState] = useState({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "id",
    email: null,
    carVin: null,
  });

  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 10,
    total: 0,
  })

  const fetchRepairBills = async () => {
    setLoading(true);

    const resp = await RepairBillDataService.findAll(state).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_repair_bills_list')}`,
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

  useEffect(() => {
    fetchRepairBills();
  }, [currentLanguage]);

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
    fetchRepairBills();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    state.email = null;
    state.carVin = null;
    fetchRepairBills();
  }

  const onSearch = value => {
    state.email = null;
    state.carVin = null;
    switch (searchParameter) {
      case "email":
        state.email = value;
        break;
      case "carVin":
        state.carVin = value;
        break;          
      default:
        break;
    }
    fetchRepairBills();
  }

  const handleSearchBy = value => {
    setSearchParameter(value)
  }

  const RepairBillsTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id 
    },
    { 
      title: `${t('user_email')}`, 
      dataIndex: 'userEmail', 
      key: 'userEmail'
    },
    { 
      title: `${t('car')}`, 
      dataIndex: 'carBrandModel', 
      key: 'carBrandModel'
    },
    { 
      title: `${t('car_vin')}`, 
      dataIndex: 'carVin', 
      key: 'carVin'
    },
    { 
      title: `${t('location')}`, 
      dataIndex: 'locationName', 
      key: 'locationName'
    },
    { 
      title: `${t('sent_date')}`, 
      dataIndex: 'sentDate', 
      key: 'sentDate',
      sorter: (a, b) => a.sentDate - b.sentDate 
    },
    { 
      title: `${t('message')}`, 
      dataIndex: 'message', 
      key: 'message' 
    },
    { 
      title: `${t('total_cost')}`, 
      dataIndex: 'totalCost', 
      key: 'totalCost',
      sorter: (a, b) => a.totalCost - b.totalCost 
    },
    { 
      title: `${t('payment_date')}`, 
      dataIndex: 'paymentDate', 
      key: 'paymentDate',
      sorter: (a, b) => a.paymentDate - b.paymentDate,
      render: (dataIndex) => <div>{dataIndex ? `${dataIndex}` : `${t('no_payment')}`}</div> 
    },
    { 
      title: `${t('order_id')}`, 
      dataIndex: 'orderId', 
      key: 'orderId',
      render: (dataIndex) => <div>{t('order_id')} - {dataIndex}</div>
    }
  ];

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
        <SearchButtonsContainer>
          <SearchTitle>{t('search_by')}:</SearchTitle>
          <Select 
            bordered={false} 
            showArrow={false} 
            defaultValue="email" 
            placeholder={t('user_email')} 
            onChange={handleSearchBy}
          >
            <Option value="email" default>{t('search_by_user_email')}</Option>
            <Option value="carVin">vin</Option>
          </Select>
          <Search placeholder={t('input_search_text')} onSearch={onSearch} style={{ width: 200 }} />
        </SearchButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={RepairBillsTableColumns} 
          dataSource={data}
          rowKey={record => record.id} 
          pagination={pagination}
          loading={loading}
          onChange={handleTableChange}
          scroll={{ x: 1600 }}
        />
      </TableContainer>
    </div>
  )
}

export default AdminBillsComponent
