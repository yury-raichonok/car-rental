import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import NotificationDataService from '../../../services/notification/NotificationDataService';
import { notification, Table, Popconfirm } from 'antd';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import { useTranslation } from 'react-i18next';
import Marginer from '../../marginer/Marginer';

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

const ProfileNotificationsComponent = () => {

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

  const fetchNotifications = async () => {
    setLoading(true);

    const resp = await NotificationDataService.findAllNew(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
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
    fetchNotifications();
  }, []);

  const updateNotificationAsRead = async (id) => {
    setLoading(true);

    const resp = await NotificationDataService.updateNotificationAsRead(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_notification')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      fetchNotifications();
      notification.success({
        message: `${t('notification_status_updated')}`,
      });
    }
  }

  const deleteNotification = async (id) => {
    setLoading(true);

    const resp = await NotificationDataService.delete(id).catch((err) => {
      notification.error({
        message: `${t('error_when_deleting_notification')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      fetchNotifications();
      notification.success({
        message: `${t('notification_deleted')}`,
      });
    }
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
    fetchNotifications();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchNotifications();
  }

  const OrderTableColumns = [
    { 
      title: `${t('message')}`, 
      dataIndex: 'message', 
      key: 'message'
    },
    { 
      title: `${t('notification_type')}`, 
      dataIndex: 'notificationType', 
      key: 'notificationType',
      width: 200,
      render: (_, record) => 
        <div>
          {record.notificationType == "Info" && "Info"}
          {record.notificationType == "Accept" && "Accept"}
          {record.notificationType == "Deny" && "Deny"}
        </div>
    },
    { 
      title: `${t('sent_date')}`, 
      dataIndex: 'sentDate', 
      key: 'sentDate',
      width: 200
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 120,
      render: (_, record) => 
        <div>
          <Popconfirm title={t('sure_to_set_notification_readed')} onConfirm={() => updateNotificationAsRead(record.id)}>
            <TableButton>
              {t('set_as_readed')}
            </TableButton>
          </Popconfirm>
          <Marginer direction="vertical" margin={8} />
          <Popconfirm title={t('sure_to_delete_notification')} onConfirm={() => deleteNotification(record.id)}>
            <TableButton>
              {t('delete_notification')}
            </TableButton>
          </Popconfirm>
        </div>
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

export default ProfileNotificationsComponent
