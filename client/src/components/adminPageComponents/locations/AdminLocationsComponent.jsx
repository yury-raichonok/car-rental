import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { notification, Table, Modal } from 'antd';
import LocationDataService from '../../../services/location/LocationDataService';
import AddLocationComponent from './AddLocationComponent';
import EditLocationComponent from './EditLocationComponent';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import Marginer from '../../marginer/Marginer';
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

const AdminLocationsComponent = () => {

  const { t } = useTranslation();

  const [locationModal, setLocationModal] = useState(false);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [location, setLocation] = useState();
  const [editModalVisible, setEditModalVisible] = useState(false);
  
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

  function EditLocationComponentFunction(props) {
    return <EditLocationComponent data={props.data} fetchLocations={fetchLocations} handleEditInfoCancel={handleEditInfoCancel} />;
  }

  const fetchLocations = async () => {
    setLoading(true);

    const resp = await LocationDataService.findAllPaged(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_locations_list')}`,
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
    fetchLocations();
  }, []);

  const showLocationModal = () => {
    setLocationModal(true);
  };

  const handleLocationCancel = () => {
    setLocationModal(false);
  };

  const handleEditInfo = (e) => {
    setEditModalVisible(true);
    setLocation(e);
  } 

  const handleEditInfoCancel = () => {
    setEditModalVisible(false);
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
    fetchLocations();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchLocations();
  }

  const LocationTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id 
    },
    { 
      title: `${t('address_ru')}`, 
      dataIndex: 'nameRu', 
      key: 'nameRu'
    },
    { 
      title: `${t('address_be')}`, 
      dataIndex: 'nameBe', 
      key: 'nameBe'
    },
    { 
      title: `${t('address_en')}`, 
      dataIndex: 'nameEn', 
      key: 'nameEn',
      sorter: (a, b) => a.nameEn - b.nameEn 
    },
    { 
      title: `${t('coordinate_x')}`, 
      dataIndex: 'coordinateX', 
      key: 'coordinateX' 
    },
    { 
      title: `${t('coordinate_y')}`, 
      dataIndex: 'coordinateY', 
      key: 'coordinateY' 
    },
    { 
      title: `${t('zoom')}`, 
      dataIndex: 'zoom', 
      key: 'zoom' 
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
        </div>,
    },
  ];

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={showLocationModal}>
            {t('add_new_location')}
          </button>
          <Marginer direction="horizontal" margin={15} />
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={LocationTableColumns} 
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
        visible={locationModal}
        title={t('add_new_location')}
        onCancel={handleLocationCancel}
        footer={[]}
      >
        <AddLocationComponent handleLocationCancel={handleLocationCancel} fetchLocations={fetchLocations} />
      </Modal>
      <Modal
        width="600px"
        visible={editModalVisible}
        title={t('edit_location')}
        onCancel={handleEditInfoCancel}
        footer={[]}
      >
        <EditLocationComponentFunction data={location}/>
      </Modal>
    </div>
  )
}

export default AdminLocationsComponent