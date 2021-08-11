import React, { useState, createRef } from 'react';
import styled from 'styled-components';
import BackgroundImage from '../../images/5.jpg';
import { Redirect } from 'react-router';
import { fetchUserData } from '../../services/authenticationService/AuthenticationService';
import { userLogin } from '../../services/authenticationService/AuthenticationLoginService';
import { Link } from 'react-router-dom';
import { Form, Input, Checkbox, notification, Spin } from 'antd';
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';

const SignInContainer = styled.div`
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
  justify-content: center;
  align-items: center;
`;

const Title = styled.h2`
  margin-bottom: 20px;
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  line-height: 1.7;

  @media (max-width: 575px) { 
    margin-bottom: 5px;
  }

  @media (max-width: 450px) { 
    font-size: 35px;
  }
`;

const SignInFormContainer = styled.div`
  margin-top: 51px;
  width: 600px;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 1.5rem;

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

  @media (max-width: 991px) { 
    margin-top: 0;
  }

  @media (max-width: 700px) { 
    width: 90%;
  }

  .btn {
    width: 200px;
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

  .ant-checkbox-wrapper {
    &:hover {
      .ant-checkbox-inner {
        border-color: #f44336;
        color: #fff;
      }
    }
  }

  .ant-checkbox-wrapper:hover .ant-checkbox-inner, .ant-checkbox:hover .ant-checkbox-inner, .ant-checkbox-input:focus + .ant-checkbox-inner {
    box-shadow: none;
    border-color: gray;
  }

  .ant-checkbox-input {
    
  }

  .ant-checkbox-checked .ant-checkbox-inner {
    background-color: #f44336;
    border-color: #f44336;
  }

  .ant-input-affix-wrapper:not(.ant-input-affix-wrapper-disabled):hover, #validate_other_email:hover {
    border: 1px solid gray;
  }

  .ant-input-affix-wrapper:focus, .ant-input-affix-wrapper-focused, #validate_other_email:focus {
    border: 1px solid gray;
    box-shadow: none;
  }
`;

const TextContainer = styled.p`
  color: #fff;

  a {
    color: #fff;
  }

  a:hover {
    text-decoration: none;
    color: #f44336;
  }
`;

const RememgerMeGroup = styled.div`  
  text-align: start;
  display: flex;
  justify-content: space-between;
  font-size: 16px;

  input {
    text-align: start;
  }

  span {
    font-size: 16px;
  }

  @media (max-width: 450px) { 
    flex-direction: column;

    .forgot-password {
      margin-top: 10px;
      align-items: senter;
    }
  }
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowTitle = styled.div`
  font-size: 17px;
  width: 70px;
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

const LoginComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();
  const [loggedIn, setLoggedIn] = useState(false);
  const [isLoading, setLoading] = useState(false);

  const onFinish = async (values) => {

    setLoading(true);
    
    await userLogin(values).then((response)=>{
      localStorage.setItem('token', response.data.token);
      setLoggedIn(true);
      fetchUserData().then((res) => {
        props.setUser(res.data);
        notification.success({
          message: `${t('congratulations')}`,
          description: `${t('you_are_successfully_logged_in')}`
        });
      }).catch((err) => {
        setLoggedIn(false);
        localStorage.clear();
        if(err && err.response){
          switch(err.response.status){
            case 401:
              console.log("401 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${t('authentication_failed_bad_credentials')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('oops')}`,
                description: `${t('something_wrong_please_try_again')}`,
              });
          }
        } else{
          notification.error({
            message: `${t('oops')}`,
            description: `${t('something_wrong_please_try_again')}`,
          });
        }
      })
    }).catch((err)=>{
      if(err && err.response){
        switch(err.response.status){
          case 401:
            console.log("401 status");
            notification.error({
              message: `${t('oops')}`,
              description: `${t('authentication_failed_wrong_email_or_password')}`,
            });
            break;
          default:
            notification.error({
              message: 'Oops!',
              description: `${t('something_wrong_please_try_again')}`,
            });
        }
      } else{
        notification.error({
          message: `${t('oops')}`,
          description: `${t('something_wrong_please_try_again')}`,
        });
      }
    })
    
    setLoading(false);
  }

  if(loggedIn) {
    return <Redirect to="/" />
  }

  return (
    <SignInContainer>
      <BackgroundFilter>
        <SignInFormContainer>
          <Form ref={formRef} onFinish={onFinish} name="validate_other">
            <Title>{t('login')}</Title>
            <Row>
              <Column>
                <Marginer direction="vertical" margin={4} />
                <RowTitle>Email:</RowTitle>
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
                <Input autoComplete="off" placeholder={t('enter_your_email')}/>
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
                    required: true,
                    message: `${t('please_enter_password')}`,
                  },
                ]}
              >
                <Input.Password type="password" placeholder={t('enter_password')}/>
              </Form.Item>
            </Row>
            <RememgerMeGroup>
              <Form.Item
                name="remember"
                valuePropName="checked"
              >
                <Checkbox>{t('remember_me')}</Checkbox>
              </Form.Item>
              <TextContainer className="forgot-password ">
                {t('forgot')} <Link to="/forgot">{t('password')}?</Link>
              </TextContainer>
            </RememgerMeGroup>
            <button className="btn" type="submit">
              {t('submit')} <Spin spinning={isLoading} />
            </button>
          </Form>
        </SignInFormContainer>
      </BackgroundFilter>
    </SignInContainer>
  )
}

export default LoginComponent