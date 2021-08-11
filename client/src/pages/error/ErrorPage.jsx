import React, { useState } from 'react';
import {  Result, Button  } from 'antd';
import { Redirect } from 'react-router-dom';

const ErrorPage = () => {

  const [isRedirect, setIsRedirect] = useState(false);

  if(isRedirect) {
    return <Redirect to="/" />
  }

  return (
    <div>
      <Result
          status="500"
          title="500"
          subTitle="Sorry, something went wrong."
          extra={<Button type="primary" onClick={() => setIsRedirect(true)}>Back Home</Button>}
        />
    </div>
  )
}

export default ErrorPage
