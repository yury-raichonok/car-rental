import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import RequestDataService from '../../../services/request/RequestDataService';
import { notification, Table, Tag } from 'antd';
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

const AdminRequestsComponent = () => {

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

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

  const fetchRequests = async () => {
    setLoading(true);

    const resp = await RequestDataService.findAll(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_requests_list')}`,
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
    fetchRequests();
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
    fetchRequests();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchRequests();
  }

  const RequestTableColumns = [
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
      sorter: (a, b) => a.userEmai - b.userEmai
    },
    { 
      title: `${t('request_type')}`, 
      dataIndex: 'requestType', 
      key: 'requestType',
      render: (_, record) => 
        <>
          { record.requestType === "ORDER_REQUEST" &&
            <Tag color={"red"} >
              {t('order_request')}
            </Tag>
          }
          { record.requestType === "DRIVING_LICENSE_CONFIRMATION_REQUEST" &&
            <Tag color={"green"} >
              {t('driving_license_confirmation_request')}
            </Tag>
          }
          { record.requestType === "PASSPORT_CONFIRMATION_REQUEST" &&
            <Tag color={"blue"} >
              {t('passport_confirmation_request')}
            </Tag>
          }
        </>,
    },
    { 
      title: `${t('sent_date')}`, 
      dataIndex: 'sentDate', 
      key: 'sentDate',
      sorter: (a, b) => a.sentDate - b.sentDate
    },
    { 
      title: `${t('comments')}`, 
      dataIndex: 'comments', 
      key: 'comments',
      sorter: (a, b) => a.sentDate - b.sentDate,
      render: (dataIndex) => <div>{dataIndex ? `${dataIndex}` : `${t('no_comments')}`}</div>
    },
    { 
      title: `${t('is_considered')}`, 
      dataIndex: 'considered', 
      key: 'considered',
      sorter: (a, b) => a.considered - b.considered,
      render: (dataIndex) => <div>{dataIndex ? `${t('considered')}` : `${t('new')}`}</div>
    },
    { 
      title: `${t('consideration_date')}`, 
      dataIndex: 'considerationDate', 
      key: 'considerationDate',
      sorter: (a, b) => a.considered - b.considered,
      render: (dataIndex) => <div>{dataIndex ? `${dataIndex}` : `${t('new')}`}</div>
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
          columns={RequestTableColumns} 
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

export default AdminRequestsComponent
