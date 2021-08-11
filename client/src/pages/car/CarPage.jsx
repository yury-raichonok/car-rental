import React from 'react';
import CarComponent from '../../components/carPageComponents/CarComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

const CarPage = (props) => {

  let component = () => ( 
    <CarComponent {...props} setUser={props.setUser} />
  );

  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default CarPage
