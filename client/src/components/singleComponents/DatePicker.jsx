import React from 'react';
import 'antd/dist/antd.css';
import { DatePicker } from 'antd';

function onChange(date, dateString) {
  console.log(date, dateString);
}

const Datepicker = (props) => {
    return (
        <>
          <DatePicker 
            onChange={onChange} 
            placeholder={props.signature}
          />
        </>
    )
}

export default Datepicker
