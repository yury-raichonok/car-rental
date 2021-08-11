import React from 'react'
import 'antd/dist/antd.css';
import { Pagination } from 'antd';

function onShowSizeChange(current, pageSize) {
  console.log(current, pageSize);
}

const PaginationComponent = (props) => {

  const { total } = props;

  return (
    <div>
      <Pagination 
        showSizeChanger
        onShowSizeChange={onShowSizeChange}
        defaultCurrent={1}
        total={total} 
      />
    </div>
  )
}

export default PaginationComponent
