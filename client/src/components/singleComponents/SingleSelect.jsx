import React from 'react'
import { Select } from 'antd';

const { Option } = Select;

const SingleSelect = (props) => {

  const { array, signature } = props;  
  
  return (
    <Select
      showSearch
      style={{ width: "100%" }}
      placeholder={signature}
      optionFilterProp="children"
      filterOption={(input, option) =>
        option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
      }
    >
      {array.length !== 0 && array.map((value) => (
        <Option key={value.id} value={value.id}>{value.name}</Option>
      ))}
    </Select>
  )
}

export default SingleSelect
