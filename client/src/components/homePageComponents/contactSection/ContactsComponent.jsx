import { useState, useEffect } from 'react';
import styled from "styled-components";
import ContactUsForm from './ContactUsFormComponent';
import Location from '../../location/Location';
import BackgroundImg from '../../../images/contact1.jpg';
import cookies from 'js-cookie';
import { useTranslation } from 'react-i18next';
import RentalDetailsDataService from '../../../services/rentalDetails/RentalDetailsDataService';

const ContactUsContainer = styled.div`
  width: 100%;
  min-height: 500px;
  display: flex;
  background-image: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.5)),  url(${BackgroundImg});
  background-position: center center;
  background-size: cover;
  display: flex;
  flex-direction: column;
  box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);
`;

const ContactFormContainer = styled.div`
  display: flex;
  flex-direction: column;
  color: #fff;
  max-width: 50%;
  min-width: 300px;
  margin-bottom: 33px;

  @media (max-width: 400px) { 
    width: 100%;
    min-width: 100%;      
  }
`;

const InformationContainer = styled.div`
  display: flex;
  flex-direction: column;
  color: #fff;
  max-width: 50%;
  min-width: 300px;
  background-color: rgba(0, 0, 0, 0.8);
  justify-content: center;
  align-items: center;
  padding: 30px;

  @media (max-width: 400px) { 
    width: 100%;
    min-width: 100%;
  }
`;

const InformationTitle = styled.h2`
  color: #fff;
  padding: 5px;
  color: #f44336;

  @media (max-width: 750px) { 
    font-size: 25px;
  }
`;

const Information = styled.span`
  color: #f44336;
`;

const ContactWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: space-evenly;

  @media (max-width: 400px) { 
    width: 100%;
  }
`;

const FormTitle = styled.h2`
  display: flex;
  color: #fff;
  padding: 10px;
  background-color: #f44336;
  position: relative;
  bottom: -30px;
  left: 20%;
  width: 60%;
  justify-content: center;
  border-radius: 10px 10px 0 0;
  box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);

  @media (max-width: 750px) { 
    font-size: 25px;
  }
`;

const WarningText = styled.div`
  color: rgba(220, 220, 220);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const languages = [
  {
    code: 'be',
    name: 'BY',
    country_code: 'by',
  },
  {
    code: 'ru',
    name: 'RU',
    country_code: 'ru',
  },
  {
    code: 'en',
    name: 'EN',
    country_code: 'gb',
  }
]

const ContactsComponent = () => {
  
  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);

  const fetchDetails = async () => {
    const res = await RentalDetailsDataService.findRentalContacts().catch((err) => {
      console.log(err.response)
    });

    if (res) {
      setData(res.data);
    }
  };

  useEffect(() => {
    fetchDetails();
  }, [currentLanguage]);

  const isDataEmpty = !data || (data && data.length === 0);

  return (
    <ContactUsContainer>
      <ContactWrapper>                
        <ContactFormContainer>
          <FormTitle>{t('contact_us')}</FormTitle>
          <ContactUsForm />
        </ContactFormContainer>
        <InformationContainer>
          <InformationTitle>{t('our_contacts')}</InformationTitle>
          {isDataEmpty && (<WarningText>{t('contact_info_not_available')}</WarningText>)}
            {!isDataEmpty && (
              <>
                <p>{t('phone_number')}: <Information>{data.phone}</Information></p>
                <p>{t('email')}: <Information>{data.email}</Information></p>
                <p>{t('address')}: <Information>{data.locationName}</Information></p>
                <Location {...data} />
              </>
          )}
        </InformationContainer>
      </ContactWrapper>           
    </ContactUsContainer>
  )
}

export default ContactsComponent