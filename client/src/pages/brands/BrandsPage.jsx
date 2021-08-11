import React from 'react';
import BrandsComponent from '../../components/brandsPageComponents/BrandsComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

let component = () => ( 
  <BrandsComponent />
);

const BrandsPage = (props) => {
  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default BrandsPage
