import React, { useState, useEffect } from 'react';
import { Table, Modal, notification, Image } from 'antd';
import BrandDataService from '../../../../services/brand/BrandDataService';
import AddBrandComponent from './AddBrandComponent';
import Reload from "@kiwicom/orbit-components/lib/icons/Reload";
import styled from 'styled-components';
import NoImage from '../../../../images/no-image.png';
import EditBrandComponent from './EditBrandComponent';
import EditBrandImageComponent from './EditBrandImageComponent';
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

const AdminBrandsComponent = () => {

  const { t } = useTranslation();

  const [isBrandModalVisible, setBrandModalVisible] = useState(false);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const [brand, setBrand] = useState();
  const [editBrandModalVisible, setEditBrandModalVisible] = useState(false);
  const [editImageModalVisible, setEditImageModalVisible] = useState(false);
  
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

  function EditBrandComponentFunction(props) {
    return <EditBrandComponent data={props.data} fetchBrands={fetchBrands} handleEditBrandCancel={handleEditBrandCancel} />;
  }

  function EditBrandImageComponentFunction(props) {
    return <EditBrandImageComponent data={props.data} fetchBrands={fetchBrands} handleEditImageCancel={handleEditImageCancel}/>
  }

  const fetchBrands = async () => {
    setLoading(true);
    
    const resp = await BrandDataService.findAllPaged(state.pageNumber, state.pageSize, state.sortBy, state.sortDirection).catch((err) => {
      notification.error({
        message: `${t('error_when_fetching_brands_list')}`,
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
    fetchBrands();
  },[])

  const BrandsTableColumns = [
    { 
      title: 'Id', 
      width: 50, 
      dataIndex: 'id', 
      key: 'id', 
      fixed: 'left',
      sorter: (a, b) => a.id - b.id,
    },
    { 
      title: `${t('entity_name')}`, 
      dataIndex: 'name', 
      key: 'name',
      sorter: (a, b) => a.name - b.name,
    },
    { 
      title: `${t('image')}`, 
      dataIndex: 'imageLink', 
      key: 'imageLink', 
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
    fetchBrands();
  };

  const onReload = () => {
    state.pageNumber = 0;
    state.pageSize = 10;
    state.sortDirection = "asc";
    state.sortBy = "id";
    fetchBrands();
  }

  const showBrandModal = () => {
    setBrandModalVisible(true);
  };

  const handleBrandCancel = () => {
    setBrandModalVisible(false);
  };

  const handleEditBrandCancel = () => {
    setEditBrandModalVisible(false);
  };

  const handleEditImageCancel = () => {
    setEditImageModalVisible(false);
  };

  const handleEditInfo = (e) => {
    setEditBrandModalVisible(true);
    setBrand(e);
  } 
  
  const handleChangeImage = (e) => {
    setEditImageModalVisible(true);
    setBrand(e);
  }

  return (
    <div>
      <FunctionsContainer>
        <ButtonsContainer>
          <button className="btn" onClick={showBrandModal}>
            {t('add_new_brand')}
          </button>
          <Marginer direction="horizontal" margin={15} />
          <button className="btn" onClick={onReload}>
            <Reload />
          </button>
        </ButtonsContainer>
      </FunctionsContainer>
      <TableContainer>
        <Table 
          columns={BrandsTableColumns} 
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
        visible={isBrandModalVisible}
        title={t('add_new_car_brand')}
        onCancel={handleBrandCancel}
        footer={[]}
      >
        <AddBrandComponent closeModal={handleBrandCancel} fetchBrands={fetchBrands}/>
      </Modal>
      <Modal
        width="600px"
        visible={editBrandModalVisible}
        title={t('edit_car_brand')}
        onCancel={handleEditBrandCancel}
        footer={[]}
      >
        <EditBrandComponentFunction data={brand}/>
      </Modal>
      <Modal
        width="600px"
        visible={editImageModalVisible}
        title={t('edit_car_image')}
        onCancel={handleEditImageCancel}
        footer={[]}
      >
        <EditBrandImageComponentFunction data={brand}/>
      </Modal>
    </div>  
  )
}

export default AdminBrandsComponent
