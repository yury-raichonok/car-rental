import React from 'react';
import SearchCarComponent from '../../components/searchCarPageComponents/SearchCarComponent';
import PageComponent from '../../components/pageComponents/PageComponent';


const SearchCar = (props) => {

  let component = () => ( 
    <div>
      <SearchCarComponent {...props}/>
    </div>
  );


  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default SearchCar
