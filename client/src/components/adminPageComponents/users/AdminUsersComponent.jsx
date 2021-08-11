import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import UserDataService from '../../../services/user/UserDataService';
import { notification, Table, Modal, Popconfirm, Tooltip, Input } from 'antd';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import Marginer from '../../marginer/Marginer';
import PassportDataComponent from './PassportDataComponent';
import DrivingLicenseDataComponent from './DrivingLicenseDataComponent';
import PhoneDataService from '../../../services/user/PhoneDataService';
import PassportDataService from '../../../services/user/PassportDataService';
import DrivingLicenseDataService from '../../../services/user/DrivingLicenseDataService';
import { useTranslation } from 'react-i18next';

const { Search } = Input;

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

const TablePhone = styled.div`
  min-height: 32px;
  font-size: 13px;
  border: none;
  cursor: pointer;
  text-align: center;
  padding-top: 5px;
  margin: 5px;
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

const SearchTitle = styled.div`
  @media (max-width: 450px) { 
    display: none;
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

const AdminUsersComponent = (props) => {

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [passportDataVisible, setPassportDataVisible] = useState(false);
  const [passportId, setPassportId] = useState();

  const [drivingLicenseDataVisible, setDrivingLicenseDataVisible] = useState(false);
  const [drivingLicenseId, setDrivingLicenseId] = useState(false);

  const [state, setState] = useState({
    pageNumber: 0,
    pageSize: 10,
    sortDirection: "asc",
    sortBy: "id",
    email: null,
  });

  const [pagination, setPagination] = useState({
    pageNumber: 0,
    pageSize: 10,
    total: 0,
  })

  const fetchUsers = async () => {
    setLoading(true);

    const resp = await UserDataService.findAll(state).catch((err) => {
      notification.error({
        message: 'Error when fetching users list!',
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
    fetchUsers();
  }, []);

  const updateUserStatus = async (id) => {
    setLoading(true);

    const resp = await UserDataService.updateUserStatus(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_user_status')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('user_status_updated')}`,
      });
      fetchUsers();
    }
  }

  const updateUserRoleToAdmin = async (id) => {
    setLoading(true);

    const resp = await UserDataService.updateUserRoleToAdmin(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_user_role')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('user_role_updated_to_administrator')}`,
      });
      fetchUsers();
    }
  }

  const updateUserRoleToUser = async (id) => {
    setLoading(true);

    const resp = await UserDataService.updateUserRoleToUser(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_user_role')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('user_role_updated_to_user')}`,
      });
      fetchUsers();
    }
  }

  const updatePhoneStatus = async (id) => {
    setLoading(true);

    const resp = await PhoneDataService.updatePhoneStatus(id).catch((err) => {
      
      if(err && err.response){
        switch(err.response.status){
          case 405:
            notification.error({
              message: `${t('this_phone_number_is_already_taken')}`,
            });
            break;
          default:
            notification.error({
              message: `${t('error_when_updating_phone_status')}`,
            });
            break;
        }
      } else{
        notification.error({
          message: `${t('something_wrong_please_try_again')}`,
        });
      }
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('phone_status_updated')}`,
      });
      fetchUsers();
    }
  }

  const updatePassportStatus = async (id) => {
    setLoading(true);

    const resp = await PassportDataService.updatePassportStatus(id).catch((err) => {
      if(err && err.response){
        switch(err.response.status){
          case 405:
            notification.error({
              message: `${t('drivers_license_is_pending_to_consider_the_request_go_to_the_requests_tab')}`,
            });
            break;
        }
      } else {
        notification.error({
          message: `${t('error_when_updating_passport_status')}`,
        });
      }
    });

    if(resp) {
      notification.success({
        message: `${t('passport_status_updated')}`,
      });
      fetchUsers();
    }

    setLoading(false);
  }

  const updateDrivingLicenseStatus = async (id) => {
    setLoading(true);

    const resp = await DrivingLicenseDataService.updateDrivingLicenseStatus(id).catch((err) => {
      notification.error({
        message: `${t('error_when_updating_driving_license_status')}`,
      });
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('driving_license_status_updated')}`,
      });
      fetchUsers();
    }
  }

  function PassportDataComponentFunction(props) {
    return <PassportDataComponent data={props.data} handlePassportDataCancel={handlePassportDataCancel} />;
  }

  function DrivingLicenseComponentFunction(props) {
    return <DrivingLicenseDataComponent data={props.data} handleDrivingLicenseDataCancel={handleDrivingLicenseDataCancel} />;
  }

  const handlePassportData = (e) => {
    setPassportId(e);
    setPassportDataVisible(true);
  }

  const handlePassportDataCancel = () => {
    setPassportDataVisible(false);
  }

  const handleDrivingLicense = (e) => {
    setDrivingLicenseId(e);
    setDrivingLicenseDataVisible(true);
  }

  const handleDrivingLicenseDataCancel = () => {
    setDrivingLicenseDataVisible(false);
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
    fetchUsers();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    state.email = null;
    fetchUsers();
  }

  const onSearch = value => {
    state.email = value;
    fetchUsers();
  }

  const UserTableColumns = [
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
      dataIndex: 'email', 
      key: 'email',
      sorter: (a, b) => a.email - b.email,
    },
    { 
      title: `${t('role')}`, 
      dataIndex: 'role', 
      key: 'role'
    },
    { 
      title: `${t('status')}`, 
      dataIndex: 'locked', 
      key: 'locked',
      render: (dataIndex) => <div>{dataIndex ? `${t('locked')}` : `${t('enabled')}`}</div>
    },
    { 
      title: `${t('email_confirmed')}`, 
      dataIndex: 'emailConfirmed', 
      key: 'emailConfirmed',
      render: (dataIndex) => <div>{dataIndex ? `${t('confirmed')}` : `${t('no')}`}</div>
    },
    { 
      title: `${t('passport_confirmed')}`, 
      dataIndex: 'passportStatus', 
      key: 'passportConfirmed',
      render: (dataIndex) => 
        <div>
          {dataIndex == "Confirmed" && `${t('confirmed')}`}
          {dataIndex == "Under consideration" && `${t('under_consideration')}`}
          {dataIndex == "Not confirmed" && `${t('not_confirmed')}`}
          {!dataIndex && `${t('no_data')}`}
        </div>
    },
    { 
      title: `${t('driving_license_confirmed')}`, 
      dataIndex: 'drivingLicenseStatus', 
      key: 'drivingLicenseConfirmed',
      render: (dataIndex) => 
        <div>
          {dataIndex == "Confirmed" && `${t('confirmed')}`}
          {dataIndex == "Under consideration" && `${t('under_consideration')}`}
          {dataIndex == "Not confirmed" && `${t('not_confirmed')}`}
          {!dataIndex && `${t('no_data')}`}
        </div>
    },
    { 
      title: `${t('phones')}`, 
      dataIndex: 'phones', 
      key: 'phones',
      render: (_, record) => 
      <div>
        {record.phones && record.phones.map((phone) => (
          <Popconfirm title={phone.active ? `${t('lock_phone')}` : `${t('unlock_phone')}`} onConfirm={() => updatePhoneStatus(phone.id)}>
            <Tooltip placement="left" title={phone.active ? `${t('phone_active')}` : `${t('phone_locked')}`}>
              <Popconfirm title={phone.active ? `${t('lock_phone')}` : `${t('unlock_phone')}`} onConfirm={() => updatePhoneStatus(phone.id)}>
                <TablePhone key={phone.id} style={phone.active ? {background: "#f6ffed", border: "1px solid #b7eb8f"} : {background: "#fff1f0", border: "1px solid #ffa39e"}}>
                  {phone.phone}
                </TablePhone>
              </Popconfirm>
            </Tooltip>
          </Popconfirm>
        ))}
      </div>
    },
    { 
      title: `${t('passport_id')}`, 
      dataIndex: 'passportId', 
      key: 'passportId',
      render: (dataIndex) => 
      <div>
        {dataIndex ? 
        (
          <TableButton onClick={() => handlePassportData(dataIndex)} >
            {t('passport_id')} - {dataIndex}
          </TableButton>
        ) : (
          `${t('not_specified')}`
        )}
      </div>
    },
    { 
      title: `${t('driving_license_id')}`, 
      dataIndex: 'drivingLicenseId', 
      key: 'drivingLicenseId',
      render: (dataIndex) => 
      <div>
        {dataIndex ? 
        (
          <TableButton onClick={() => handleDrivingLicense(dataIndex)} >
            {t('license_id')} - {dataIndex}
          </TableButton>
        ) : (
          `${t('not_specified')}`
        )}
      </div>
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 150,
      render: (_, record) => 
        <div>
          {!(props.user.email === record.email) && (
            <>
              {record.locked ? (
                <>
                  <Popconfirm title={t('sure_to_unlock_user')} onConfirm={() => updateUserStatus(record.id)}>
                    <TableButton>
                      {t('enable_user')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ) : (
                <>
                  <Popconfirm title={t('sure_to_lock_user')} onConfirm={() => updateUserStatus(record.id)}>
                    <TableButton>
                      {t('lock_user')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              )}
              {record.role === "User" ? (
                <>
                  <Popconfirm title={t('sure_to_set_role_admin')} onConfirm={() => updateUserRoleToAdmin(record.id)}>
                    <TableButton>
                      {t('set_role_admin')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ) : (
                <>
                  <Popconfirm title={t('sure_to_set_role_user')} onConfirm={() => updateUserRoleToUser(record.id)}>
                    <TableButton>
                      {t('set_role_user')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              )}
              {record.passportId && (record.passportStatus == "Confirmed" ? (
                <>
                  <Popconfirm title={t('sure_to_set_passport_status_not_confirmed')} onConfirm={() => updatePassportStatus(record.passportId)}>
                    <TableButton>
                      {t('change_passport_status')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ) : (
                <>
                  <Popconfirm title={t('sure_to_set_passport_status_confirmed')} onConfirm={() => updatePassportStatus(record.passportId)}>
                    <TableButton>
                      {t('confirm_passport')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ))}
              {record.drivingLicenseId && (record.drivingLicenseStatus == "Confirmed" ? (
                <>
                  <Popconfirm title={t('sure_to_set_license_status_not_confirmed')} onConfirm={() => updateDrivingLicenseStatus(record.drivingLicenseId)}>
                    <TableButton>
                      {t('change_license_status')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ) : (
                <>
                  <Popconfirm title={t('sure_to_set_license_status_confirmed')} onConfirm={() => updateDrivingLicenseStatus(record.drivingLicenseId)}>
                    <TableButton>
                      {t('confirm_license')}
                    </TableButton>
                  </Popconfirm>
                  <Marginer direction="vertical" margin={8} />
                </>
              ))}
            </>
          ) }
        </div>,
    },
  ];

  console.log(props.user);

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
        <SearchButtonsContainer>
          <SearchTitle>{t('search_by_email')}:</SearchTitle>
          <Marginer direction="horizontal" margin={10} />
          <Search placeholder={t('input_search_text')} onSearch={onSearch} style={{ width: 200 }} />
        </SearchButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={UserTableColumns} 
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
        visible={passportDataVisible}
        title={t('passport_data')}
        onCancel={handlePassportDataCancel}
        footer={[]}
      >
        <PassportDataComponentFunction data={passportId} />
      </Modal>
      <Modal
        width="600px"
        visible={drivingLicenseDataVisible}
        title={t('driving_license_data')}
        onCancel={handleDrivingLicenseDataCancel}
        footer={[]}
      >
        <DrivingLicenseComponentFunction data={drivingLicenseId} />
      </Modal>
    </div>
  )
}

export default AdminUsersComponent
