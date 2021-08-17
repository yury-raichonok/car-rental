import React, { useState, createRef } from 'react';
import { Link, Redirect } from 'react-router-dom';
import styled from 'styled-components';
import UserDataService from '../../services/user/UserDataService';
import BackgroundImage from '../../images/5.jpg';
import { Form, Input, notification, Spin } from 'antd';
import { CheckOutlined } from '@ant-design/icons';
import Marginer from '../marginer/Marginer';
import { useTranslation } from 'react-i18next';

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowTitle = styled.div`
  font-size: 17px;
  width: 130px;
  text-align: left;
  color: #fff;
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .ant-form-item {
    width: 100%;
  }

  @media (max-width: 500px) { 
    flex-direction: column;
  }
`;

const SignUpContainer = styled.div`
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

const Title = styled.h2`
  margin: 0;
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  line-height: 1.7;

  @media (max-width: 450px) { 
    font-size: 30px;
  }

  @media (max-width: 300px) { 
    font-size: 25px;
  }
`;

const SignUpFormContainer = styled.div`
  max-width: 800px;
  width: 90%;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 1.2rem;
  margin-top: 51px;

  .ant-form-item-label {
    label {
      color: #fff;
      font-size: 16px;
    }
  }

  label {
    color: #fff;
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

  .btn {
    width: 200px;
    font-size: 17px;
    color: #fff;
    background-color: #f44336;
    border: none;
    margin-bottom: 15px;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  .ant-input-affix-wrapper:not(.ant-input-affix-wrapper-disabled):hover, #validate_other_email:hover {
    border: 1px solid gray;
  }

  .ant-input-affix-wrapper:focus, .ant-input-affix-wrapper-focused, #validate_other_email:focus {
    border: 1px solid gray;
    box-shadow: none;
  }

  @media (max-width: 991px) { 
    margin-top: 0;
  }
`;

const TextContainer = styled.p`
  margin: 10px 20px 20px 0;
  color: #fff;
  font-size: 16px;

  a {
    color: #fff;
  }

  a:hover {
    text-decoration: none;
    color: #f44336;
  }
`;

const FormContainer = styled.div`
  padding: 0 20px 0 20px;
`;

const AdvantagesListContainer = styled.div`  
  text-align: start;
  list-style: none;
  font-size: 16px;
  color: #fff;
  padding-left: 20px;
  
  div {
    text-align: start;
    padding: 5px;
    display: flex;
    flex-direction: row;
    align-items: center;

    span {
      color: green;
      margin-right: 10px;
    }
  }
`;

const RegisterComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();
  const [registered, setRegistered] = useState(false);
  const [isLoading, setLoading] = useState(false);

  const onFinish = async (values) => {

    setLoading(true);
    
    await UserDataService.registrateUser(values).then(
      res => {
        setRegistered(true);
        console.log(res);
        notification.success({
          message: `${t('congratulations')}`,
          description: `${t('you_are_successfully_registered_a_link_to_confirm_your_email_has_been_sent_to_your_email')}`
        });
      }  
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 401:
              console.log("401 status");
              notification.error({
                message: `${t('registration_failed_bad_credentials')}`,
              });
              break;
            case 405:
              console.log("405 status");
              notification.error({
                message: `${err.response.data}`,
              });
              break;
            default:
              notification.error({
                message: `${t('something_wrong_please_try_again')}`,
              });
          }
        } else{
          notification.error({
            message: `${t('something_wrong_please_try_again')}`,
          });
        }
      }
    )

    setLoading(false);
  }
    
  if(registered) {
    return <Redirect to="/login" />
  }

  return (
    <SignUpContainer>
      <BackgroundFilter>
        <SignUpFormContainer>
          <Form ref={formRef} onFinish={onFinish} name="validate_other">
            <Title>{t('create_account')}</Title>
            <AdvantagesListContainer>
              <div><CheckOutlined /> {t('view_your_booking_history_and_manage_your_personal_information_for_our_services')}</div>
              <div><CheckOutlined /> {t('faster_booking')}</div>
              <div><CheckOutlined /> {t('gain_access_to_our_services')}</div>
            </AdvantagesListContainer>
            <Marginer direction="vertical" margin={20} />
            <FormContainer>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('email')}:</RowTitle>
                </Column>
                <Form.Item name="email"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_enter_email')}`,
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
                  <RowTitle>{t('register_password')}:</RowTitle>
                </Column>
                <Form.Item 
                  name="password"
                  rules={[
                    {
                      pattern: "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                      message: `${t('password_must_be_from_8_characters_min_one_uppercase_letter_one_lowercase_letter_and_one_number')}`,
                    },
                    {
                      required: true,
                      message: `${t('please_enter_password')}`,
                    },
                  ]}
                >
                  <Input.Password type="password" placeholder={t('enter_password')}/>
                </Form.Item>
              </Row>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('confirm_password')}:</RowTitle>
                </Column>
                <Form.Item 
                  name="confirmPassword"
                  rules={[
                    {
                      required: true,
                      message: `${t('please_confirm_your_password')}`,
                    },
                    ({ getFieldValue }) => ({
                      validator(_, value) {
                        if (!value || getFieldValue('password') === value) {
                          return Promise.resolve();
                        }
            
                        return Promise.reject(new Error(`${t('the_two_passwords_do_not_match')}`));
                      },
                    }),
                  ]}
                >
                  <Input.Password type="password" placeholder={t('confirm_password')}/>
                </Form.Item>
              </Row>
              
            </FormContainer>
            <TextContainer className="forgot-password text-right">
              {t('already_registered')} <Link to="/login">{t('sign_in')}?</Link>
            </TextContainer>
            <button className="btn"  type="submit">
                {t('submit')} <Spin spinning={isLoading}/>
              </button>
          </Form>
        </SignUpFormContainer>
      </BackgroundFilter>
    </SignUpContainer>
  )  
}

export default RegisterComponent
