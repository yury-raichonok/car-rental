import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import OrderDataService from '../../../services/order/OrderDataService';
import { notification, Table, Modal, Popconfirm } from 'antd';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import Marginer from '../../marginer/Marginer';
import CancelOrderComponent from './CancelOderComponent';
import { useTranslation } from 'react-i18next';
import cookies from 'js-cookie';

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

const AdminFutureOrdersComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [order, setOrder] = useState();
  const [cancelOrderModalVisible, setCancelOrderModalVisible] = useState(false);

  const [state, setState] = useState({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "id",
    brandName: null,
  });

  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 10,
    total: 0,
  })

  const fetchFutureOrders = async () => {
    setLoading(true);

    const resp = await OrderDataService.findAllFuture(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_future_orders_list')}`,
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
    fetchFutureOrders();
  }, [currentLanguage]);
  
  const handleStartOrder = async (data) => {
    setLoading(true);

    const resp = await OrderDataService.startRentalPeriod(data.id).catch((err) => {
      notification.error({
        message: `${t('error_when_starting_order_period')}`,
      });
      setLoading(false);
    });

    if(resp) {
      fetchFutureOrders();
      notification.success({
        message: `${t('rental_period_started')}`,
      });
      setLoading(false);
    }
  }

  function CancelOrderComponentFunction(props) {
    return <CancelOrderComponent data={props.data} fetchFutureOrders={fetchFutureOrders} handleCancelOrderCancel={handleCancelOrderCancel} />;
  }

  const handleCancelOrder = (e) => {
    setOrder(e);
    setCancelOrderModalVisible(true);
  }

  const handleCancelOrderCancel = () => {
    setCancelOrderModalVisible(false);
  }

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
    fetchFutureOrders();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchFutureOrders();
  }

  const CurrentOrderTableColumns = [
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
      key: 'userEmail',
      sorter: (a, b) => a.userEmail - b.userEmail
    },
    { 
      title: `${t('car')}`, 
      dataIndex: 'carBrandModel', 
      key: 'carBrandModel'
    },
    { 
      title: `${t('car_vin')}`, 
      dataIndex: 'carVin', 
      key: 'carVin',
      sorter: (a, b) => a.carVin - b.carVin
    },
    { 
      title: `${t('location')}`, 
      dataIndex: 'locationName', 
      key: 'locationName',
      sorter: (a, b) => a.locationName - b.locationName
    },
    { 
      title: `${t('pick_up_date')}`, 
      dataIndex: 'pickUpDate', 
      key: 'pickUpDate',
      sorter: (a, b) => a.pickUpDate - b.pickUpDate
    },
    { 
      title: `${t('return_date')}`, 
      dataIndex: 'returnDate', 
      key: 'returnDate',
      sorter: (a, b) => a.returnDate - b.returnDate
    },
    { 
      title: `${t('total_cost')}`, 
      dataIndex: 'totalCost', 
      key: 'totalCost',
      sorter: (a, b) => a.totalCost - b.totalCost
    },
    { 
      title: `${t('sent_date')}`, 
      dataIndex: 'sentDate', 
      key: 'sentDate',
      sorter: (a, b) => a.sentDate - b.sentDate
    },
    { 
      title: `${t('payment_status')}`, 
      dataIndex: 'paymentStatus', 
      key: 'paymentStatus',
      sorter: (a, b) => a.paymentStatus - b.paymentStatus,
      render: (dataIndex) => <div>{dataIndex ? `${t('paid')}` : `${t('no_paid')}`}</div>
    },
    { 
      title: `${t('payment_date')}`, 
      dataIndex: 'paymentDate', 
      key: 'paymentDate',
      sorter: (a, b) => a.paymentStatus - b.paymentStatus,
      render: (dataIndex) => <div>{dataIndex ? `${dataIndex}` : `${t('no_paid')}`}</div>
    },
    { 
      title: `${t('payment_bill_id')}`, 
      dataIndex: 'paymentBillId', 
      key: 'paymentBillId',
      render: (dataIndex) => <div>{dataIndex ? `${dataIndex}` : `${t('has_no_bill')}`}</div>
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 120,
      render: (_, record) => 
        <div>
          <Popconfirm title={t('sure_to_start_rental_period')} onConfirm={() => handleStartOrder(record)}>
            <TableButton>
              {t('start_rental')}
            </TableButton>
          </Popconfirm>
          <Marginer direction="vertical" margin={8} />
          <TableButton onClick={() => handleCancelOrder(record)} >
            {t('cancel_order')}
          </TableButton>
        </div>,
    },
  ];

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={CurrentOrderTableColumns} 
          dataSource={data}
          rowKey={record => record.id} 
          pagination={pagination}
          loading={loading}
          onChange={handleTableChange}
          scroll={{ x: 1600 }}
        />
      </TableContainer>
      <Modal
        width="600px"
        visible={cancelOrderModalVisible}
        title={t('cancel_order')}
        onCancel={handleCancelOrderCancel}
        footer={[]}
      >
        <CancelOrderComponentFunction data={order} />
      </Modal>
    </div>
  )
}

export default AdminFutureOrdersComponent
