import { useState, useEffect } from 'react';
import { Table, Modal, notification } from 'antd';
import CarClassDataService from '../../../../services/carClass/CarClassDataService';
import AddClassComponent from './AddClassComponent';
import EditClassComponent from './EditClassComponent';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import styled from 'styled-components';
import Marginer from '../../../marginer/Marginer';
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

const AdminCarsComponent = () => {

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [carClass, setCarClass] = useState();
  const [isClassModalVisible, setClassModalVisible] = useState(false);
  const [showEditModal, setShowEditModal] = useState(false);

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

  const fetchClasses = async () => {
    setLoading(true);

    const resp = await CarClassDataService.findAllPaged(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_car_classes_list')}`,
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

  function EditClassComponentFunction(props) {
    return <EditClassComponent data={props.data} handleEditInfoCancel={handleEditInfoCancel} fetchClasses={fetchClasses} />
  }

  useEffect(() => {
    fetchClasses();
  }, []);

  const showClassModal = () => {
    setClassModalVisible(true);
  };

  const handleClassCancel = () => {
    setClassModalVisible(false);
  };

  const handleEditInfo = (e) => {
    setShowEditModal(true);
    setCarClass(e);
  }

  const handleEditInfoCancel = (e) => {
    setShowEditModal(false);
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
    fetchClasses();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchClasses();
  }

  const ClassTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id
    },
    { 
      title: `${t('entity_name_ru')}`, 
      dataIndex: 'nameRu', 
      key: 'nameRu'
    },
    { 
      title: `${t('entity_name_be')}`, 
      dataIndex: 'nameBe', 
      key: 'nameBe'
    },
    { 
      title: `${t('entity_name_en')}`, 
      dataIndex: 'nameEn', 
      key: 'nameEn',
      sorter: (a, b) => a.nameEn - b.nameEn
    },
    {
      title: `${t('action')}`,
      key: 'operation',
      fixed: 'right',
      width: 120,
      render: (_, record) => {
        return (
          <TableButton onClick={() => handleEditInfo(record)} >
            {t('edit_info')}
          </TableButton>   
        )
      },
    },
  ];

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={showClassModal}>
            {t('add_new_class')}
          </button>
          <Marginer direction="horizontal" margin={15} />
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={ClassTableColumns} 
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
        visible={isClassModalVisible}
        title={t('add_new_car_class')}
        onCancel={handleClassCancel}
        footer={[]}
      >
        <AddClassComponent handleClassCancel={handleClassCancel} fetchClasses={fetchClasses} />
      </Modal>
      <Modal
        width="600px"
        visible={showEditModal}
        title={t('edit_car_class')}
        onCancel={handleEditInfoCancel}
        footer={[]}
      >
        <EditClassComponentFunction data={carClass} />
      </Modal>
    </div>  
  )
}

export default AdminCarsComponent