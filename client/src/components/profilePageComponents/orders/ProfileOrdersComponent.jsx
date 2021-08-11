import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import OrderDataService from '../../../services/order/OrderDataService';
import { notification, Table } from 'antd';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import { useTranslation } from 'react-i18next';

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
  width: 120px;
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

const ProfileOrdersComponent = () => {

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

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

  const FileDownload = require('js-file-download');

  const exportOrder = async (id) => {
    setLoading(true);

    const resp = await OrderDataService.exportOrderToPDF(id).catch((err) => {
      notification.error({
        message: `${t('error_when_exporting_order_data')}`,
      });
      setLoading(false);
    });

    if(resp) {
      FileDownload(resp.data, `${id + ".pdf"}`);
      setLoading(false);
    }
  }

  const fetchOrders = async () => {
    setLoading(true);

    const resp = await OrderDataService.findNewUserOrders(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_orders_list')}`,
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
    fetchOrders();
  }, []);

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
    fetchOrders();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchOrders();
  }

  const OrderTableColumns = [
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
      sorter: (a, b) => a.totalCost - b.totalCost,
      render: (dataIndex) => <div>{dataIndex} BYN</div>
    },
    { 
      title: `${t('sent_date')}`, 
      dataIndex: 'sentDate', 
      key: 'sentDate',
      sorter: (a, b) => a.sentDate - b.sentDate
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 150,
      render: (_, record) => 
        <div>
          <TableButton onClick={() => exportOrder(record.id)} >
            {t('save_order_details')}
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
          columns={OrderTableColumns} 
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

export default ProfileOrdersComponent
