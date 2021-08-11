import React, { useState, useEffect } from 'react';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import { Form, Input,  notification } from 'antd';
import styled from "styled-components";
import { isValidPhoneNumber } from 'react-phone-number-input';
import MessageDataService from '../../services/message/MessageDataService';
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';
import cookies from 'js-cookie';
import Location from '../location/Location';
import RentalDetailsDataService from '../../services/rentalDetails/RentalDetailsDataService';

import BackgroundImage from '../../images/contact1.jpg';

const PageContainer = styled.div`
  width: 100%;
  min-height: calc( 100vh - 250px );
  display: flex;
  background: url(${BackgroundImage});
  background-position: center canter;
  background-size: cover;

  @media (max-width: 991px) { 
    min-height: calc( 100vh - 306px );
  }
`;

const BackgroundFilter = styled.div`
  width: 100%;
  min-height: 100%;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const ContentContainer = styled.div`
  margin-top: 51px;
  width: 1400px;
  display: flex;
  flex-direction: row;
  color: #fff;
  justify-content: space-between;
  min-height: calc(100vh - 306px);

  @media (max-width: 1400px) { 
    width: 95%;
  }

  @media (max-width: 1070px) { 
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  @media (max-width: 991px) { 
    margin-top: 0;
  }
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowTitle = styled.div`
  font-size: 14px;
  width: 70px;
  text-align: left;
  color: #000;
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .ant-form-item {
    width: 100%;
  }

  @media (max-width: 575px) { 
    flex-direction: column;
  }
`;

const ContactUsFormContainer = styled.div`
  background-color: #fff;
  padding: 40px 30px 10px 30px;
  border-radius: 10px;
  color: #fff;
  width: 500px;
  min-height: 400px;

  .ant-form {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    height: 340px;
  }

  .ant-form-item {
    margin-bottom: 0px;
  }

  .ant-form-item-control {
    margin-bottom: 24px;
  }

  .ant-form-item-with-help {
    .ant-form-item-control {
      margin-bottom: 0px;
    }
  }

  .ant-form-item-control-input-content {
    input::placeholder {
      font-size: 13px;
      color: #bfbfbf;
    }

    input:hover, textarea:hover {
      border: 1px solid gray;
    }

    input:focus, textarea:focus {
      border: 1px solid gray;
      box-shadow: none;
    }
  }

  .btn {
    width: 140px;
    font-size: 17px;
    color: #fff;
    background-color: #f44336;
    border: none;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  .react-tel-input {
    input {
      width: 100%;
    }
    

    input::placeholder {
      font-size: 13px;
      color: #bfbfbf;
    }

    &:hover {
      .form-control {
        border: 1px solid gray;
      }
    }
    .form-control {
      border-radius: 0;
      height: 32px;

      &:focus {
        border: 1px solid gray;
        box-shadow: none;
      }

    }
    .flag-dropdown {
      background: none;
      border: none;
      &:hover {
        cursor: pointer;
        background: none;
        border: none;
        cursor: auto;
      }      
    }

    .selected-flag {
      &:hover, &:focus {
        background: none;
        border: none;
        cursor: auto;
      }
    }
  }

  @media (max-width: 1070px) { 
    width: 100%;
  }

  @media (max-width: 575px) { 
    .ant-form {
      padding-top: 80px;
    }
  
    min-height: 490px;
  }

  @media (max-width: 300px) { 
    width: 100%;
  }
`;

const InformationContainer = styled.div`
  display: flex;
  flex-direction: column;
  color: #fff;
  width: 500px;
  min-width: 300px;
  background-color: rgba(0, 0, 0, 0.8);
  justify-content: center;
  align-items: center;
  padding: 30px;

  @media (max-width: 1070px) { 
    width: 400px;
  }

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

const WarningText = styled.div`
  color: rgba(220, 220, 220);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const ContactFormContainer = styled.div`
  display: flex;
  flex-direction: column;
  color: #fff;
  min-width: 300px;
  margin-bottom: 33px;
  height: 100%;
  justify-content: center;

  @media (max-width: 1070px) { 
    width: 400px;
  }

  @media (max-width: 400px) { 
    width: 100%;
    min-width: 100%;      
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

const ContactUsComponent = (props) => {

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

  const [isPhoneValid, setIsPhoneValid] = useState(true);
  const formRef = React.createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const onFinish = async (values) => {
    MessageDataService.create(values).then(
      () => {
        onReset();
        notification.success({
          message: `${t('message_sent_to_administrator')}`,
        });
      }
    ).catch(
      () => {
        notification.error({
          message: `${t('something_went_wrong')}`,
          description: `${t('message_not_sent')}`,
        });
      }
    )
  };

  return (
    <PageContainer>
      <BackgroundFilter>
        <ContentContainer>
          <ContactFormContainer>
            <FormTitle>{t('contact_us')}</FormTitle>
            <ContactUsFormContainer>
              <Form ref={formRef} name="validate_other" onFinish={onFinish}>
                <Row>
                  <Column>
                    <Marginer direction="vertical" margin={4} />
                    <RowTitle>{t('name')}:</RowTitle>
                  </Column>
                  <Form.Item name="name"
                    rules={[
                      {
                        required: true,
                        message: `${t('please_enter_your_name')}`,
                      },
                      {
                        max: 255,
                        message: `${t('name_is_too_long')}`,
                      },
                      {
                        whitespace: true,
                        message: `${t('name_can_not_be_empty')}`,
                      },
                    ]}
                  >
                    <Input type="text" autoComplete="off" placeholder={t('enter_your_name')} />
                  </Form.Item>
                </Row>
                <Row>
                  <Column>
                    <Marginer direction="vertical" margin={4} />
                    <RowTitle>{t('email')}:</RowTitle>
                  </Column>
                  <Form.Item name="email"
                    rules={[
                      {
                        required: true,
                        message: `${t('please_enter_your_email')}`,
                      },
                      {
                        type: 'email',
                        message: `${t('email_is_not_valid')}`,
                      },
                      {
                        max: 255,
                        message: `${t('email_is_too_long')}`,
                      },
                    ]}
                  >
                    <Input placeholder={t('enter_your_email')}/>
                  </Form.Item>
                </Row>
                <Row>
                  <Column>
                    <Marginer direction="vertical" margin={4} />
                    <RowTitle>{t('phone')}:</RowTitle>
                  </Column>
                  <Form.Item name="phone"
                    rules={[
                      {
                        required: true,
                        message: `${t('please_enter_your_phone')}`,
                      },
                      () => ({
                        validator(_, value) {
                          setIsPhoneValid(false);
                          if (isValidPhoneNumber("+" + value)) {
                            setIsPhoneValid(true);
                            return Promise.resolve();
                          } else if (!value) {
                            setIsPhoneValid(false);
                            return Promise.resolve();
                          }
                          return Promise.reject(new Error(`${t('phone_number_is_not_valid')}`));
                        },
                      }),
                    ]}
                  >
                    <PhoneInput 
                      disableDropdown = {true}
                      inputStyle={!isPhoneValid ? ({border: "1px solid red"}) : ({})} 
                      placeholder={t('enter_your_phone')}
                    />
                  </Form.Item>
                </Row>
                <Row>
                  <Column>
                    <Marginer direction="vertical" margin={4} />
                    <RowTitle>{t('message')}:</RowTitle>
                  </Column>
                  <Form.Item name="message"
                    rules={[
                      {
                        required: true,
                        message: `${t('please_enter_message')}`,
                      },
                      {
                        whitespace: true,
                        message: `${t('message_can_not_be_empty')}`,
                      },
                    ]}
                  >
                    <Input.TextArea autoComplete="off" placeholder={t('enter_message')} />
                  </Form.Item>
                </Row>
                <div>
                  <button className="btn" type="text" type="submit">
                    {t('submit')}
                  </button>
                </div>
              </Form>
            </ContactUsFormContainer>
          </ContactFormContainer>
          <InformationContainer>
            <InformationTitle>{t('our_contacts')}</InformationTitle>
            {isDataEmpty && (<WarningText>{t('contact_info_not_available')}</WarningText>)}
              {!isDataEmpty && (
                <>
                  <p>{t('phone_number')}: <Information>{data.phone}</Information></p>
                  <p>{t('email')}: <Information>{data.email}</Information></p>
                  <p>{t('address')}: <Information>{data.locationName}</Information></p>
                  <Marginer direction="vertical" margin={10} />
                  <Location {...data} />
                </>
            )}
          </InformationContainer>
        </ContentContainer>
      </BackgroundFilter>
    </PageContainer>
  )
}

export default ContactUsComponent
