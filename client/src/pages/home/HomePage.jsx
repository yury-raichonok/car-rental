import React from 'react';
import ProfitableOffers from '../../components/homePageComponents/profitableOffers/ProfitableOffers';
import FaqListComponent from '../../components/homePageComponents/faqSection/FaqListComponent';
import ContactsComponent from '../../components/homePageComponents/contactSection/ContactsComponent';
import Marginer from '../../components/marginer/Marginer';
import TopSectionComponent from '../../components/homePageComponents/topSection/TopSectionComponent';
import PageComponent from '../../components/pageComponents/PageComponent';

let component = (props) => ( 
    <div>
      <TopSectionComponent />
      <Marginer direction="vertical" margin={100}  />
      <ProfitableOffers />
      <Marginer direction="vertical" margin={100}  />
      <FaqListComponent />
      <Marginer direction="vertical" margin={100}  />
      <ContactsComponent />
      <Marginer direction="vertical" margin={100}  />
    </div>
  );

const HomePage = (props) => {

  return (
    <div>
      <PageComponent setUser={props.setUser} user={props.user} child={component}/>
    </div>
  )
}

export default HomePage
