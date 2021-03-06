import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Table, notification, Modal, Spin } from 'antd';
import PaymentBillDataService from '../../../services/bill/PaymentBillDataService';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import { useTranslation } from 'react-i18next';
import PaymentComponent from './PaymentComponent';
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

const TableButton = styled.button`
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

const ProfilePaymentBillsComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [paymentLoading, setPaymentLoading] = useState(false);
  const [paymentVisible, setPaymentVisible] = useState(false);

  const [bill, setBill] = useState({});

  const [state, setState] = useState({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "id",
  });

  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 10,
    total: 0,
  })

  const fetchBills = async () => {
    setLoading(true);

    const resp = await PaymentBillDataService.findNewUserBills(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_payment_bills_list')}`,
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
    fetchBills();
  }, [currentLanguage]);

  const payBill = async (id) => {
    setPaymentLoading(true);

    const resp = await PaymentBillDataService.payBill(id).catch((err) => {
      notification.error({
        message: `${t('error_when_paying_bill')}`,
      });
      setPaymentLoading(false);
    });

    if(resp) {
      setPaymentLoading(false);
      fetchBills();
      notification.success({
        message: `${t('bill_successfully_payed')}`,
      });
    }
  }

  const PaymentBillsTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id 
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
      title: `${t('expiration_time')}`, 
      dataIndex: 'expirationTime', 
      key: 'expirationTime' 
    },
    { 
      title: `${t('total_cost')}`, 
      dataIndex: 'totalCost', 
      key: 'totalCost',
      sorter: (a, b) => a.totalCost - b.totalCost 
    },
    { 
      title: `${t('order_id')}`, 
      dataIndex: 'orderId', 
      key: 'orderId',
      render: (dataIndex) => <div>{t('order_id')} - {dataIndex}</div>
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 120,
      render: (_, record) => 
      <TableButton onClick={() => handlePayBill(record)} >
        {t('pay_bill')}
      </TableButton>
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
    fetchBills();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchBills();
  }

  const handlePayBillCancel = () => {
    setPaymentVisible(false);
  }

  const handlePayBill = (e) => {
    setBill(e);
    setPaymentVisible(true);
  }

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
          columns={PaymentBillsTableColumns} 
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
        visible={paymentVisible}
        title={t('pay_bill')}
        onCancel={handlePayBillCancel}
        footer={[]}
      >
        <Spin spinning={paymentLoading}>
          <PaymentComponent data={bill} payBill={payBill} handlePayBillCancel={handlePayBillCancel} fetchBills={fetchBills} />
        </Spin>
      </Modal>
    </div>
  )
}

export default ProfilePaymentBillsComponent

