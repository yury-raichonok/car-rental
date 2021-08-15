import React, { useState } from 'react';
import { Redirect } from 'react-router';
import ChatBot from 'react-simple-chatbot';
import styled, { ThemeProvider } from 'styled-components';
import { useTranslation } from 'react-i18next';

const BotContainer = styled.div`
  h2 {
    color: #fff;
  }

  .rsc-ts-bubble {
    text-align: left;
  }
`;

const config ={
  height: "500px",
  floating: true,
};

const style = {
  backgroundColor: "#f44336",
  width: "45px",
  height: "45px",
  right: "37px",
  bottom: "45px",
};

const theme = {
  background: '#f5f8fb',
  headerBgColor: '#f44336',
  headerFontColor: '#fff',
  headerFontSize: '18px',
  botBubbleColor: '#fff',
  botFontColor: '#000',
  userBubbleColor: '#fff'
};



const ChatBotComponent = (props) => {

  const { t } = useTranslation();

  const [brands, setBrands] = useState(false);
  const [search, setSearch] = useState(false);
  const [contact, setContact] = useState(false);

  function redirectToBrandsPage() {
    setBrands(true);
  }

  function redirectToSearchPage() {
    setSearch(true);
  }
  
  function redirectToContactUsPage() {
    setContact(true);
  }

  if(brands) {
    return <Redirect to="/brands" />
  }

  if(search) {
    return <Redirect to="/search" />
  }

  if(contact) {
    return <Redirect to="/contact" />
  }

  return (
    <BotContainer>
      <ThemeProvider theme={theme}>
        <ChatBot 
          headerTitle={t('car_rental_support')}
          botDelay="2000"
          steps={[
            {
              id: '1',
              message: `${t('hi_how_can_i_help_you')}`,
              trigger: '2',
            },
            {
              id: '2',
              options: [
                { value: 2, label: `${t('i_want_book_a_car')}`, trigger: '3' },
                { value: 9, label: `${t('what_cars_are_available_for_rent')}`, trigger: '8' },
                { value: 10, label: `${t('i_want_to_see_the_available_car_options')}`, trigger: '9' },
                { value: 3, label: `${t('i_want_to_contact_the_administrator')}`, trigger: 'contact-response' },
              ],
            },
            {
              id: '3',
              message: `${t('to_book_a_car')}`,
              trigger: '4'
            },
            {
              id: '4',
              options: [
                { value: 4, label: `${t('how_to_register')}`, trigger: '5' },
                { value: 5, label: `${t('how_to_confirm_personal_data')}`, trigger: '6' },
                { value: 6, label: `${t('how_to_specify_a_contact_phone_number')}`, trigger: '7' },
                { value: 7, label: `${t('i_want_to_contact_the_administrator')}`, trigger: 'contact-response' },
                { value: 8, label: `${t('thank_you')}`, trigger: 'thanks-else' },
              ],
            },
            {
              id: '5',
              message: `${t('to_register')}`,
              trigger: 'else'
            },
            {
              id: '6',
              message: `${t('to_confirm_data')}`,
              trigger: 'else'
            },
            {
              id: '7',
              message: `${t('to_specify_phone')}`,
              trigger: 'else'
            },
            {
              id: '8',
              message: `${t('redirect_to_brands')}`,
              trigger: 'brands'
            },
            {
              id: '9',
              message: `${t('check_out_the_complete_list_of_our_vehicles')}`,
              trigger: '10'
            },
            {
              id: '10',
              message: `${t('posibility_to_filter')}`,
              trigger: 'search'
            },
            {
              id: 'contact-response',
              message: `${t('you_can_use_feetback')}`,
              trigger: 'contact-response-2'
            },
            {
              id: 'contact-response-2',
              message: `${t('redirect_to_contact')}`,
              trigger: 'contact'
            },
            {
              id: 'contact',
              delay: 4000,
              message: 'Redirect',
              trigger: redirectToContactUsPage,
            },
            {
              id: 'brands',
              delay: 4000,
              message: 'Redirect',
              trigger: redirectToBrandsPage,
            },
            {
              id: 'search',
              delay: 4000,
              message: 'Redirect',
              trigger: redirectToSearchPage,
            },
            {
              id: 'else',
              message: `${t('want_something_else')}`,
              trigger: 'else-options',
            },
            {
              id: 'thanks-else',
              message: `${t('you_are_welcome')}`,
              trigger: 'else-options',
            },
            {
              id: 'else-options',
              options: [
                {value:'y', label:`${t('yes')}`, trigger:'yes-response'},
                {value:'n', label:`${t('no')}`, trigger:'no-response'},
                {value:7, label:`${t('i_want_to_contact_the_administrator')}`, trigger:'contact-response'},
              ],
            },
            {
              id:'yes-response', 
              message:`${t('how_can_i_help_you')}`, 
              trigger: '2',
            },
            {
              id:'no-response', 
              message:`${t('it_was_nice_to_chat')}`, 
              end:true,
            },
          ]}
          floatingStyle={style}
          placeholder={t('type_message')}
          {...config}
        />
      </ThemeProvider>
    </BotContainer>
  )
}

export default ChatBotComponent
