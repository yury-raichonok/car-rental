import React, { useState, createRef } from 'react';
import styled from 'styled-components';
import BackgroundImage from '../../images/5.jpg';
import { Link } from 'react-router-dom';
import UserDataService from '../../services/user/UserDataService';
import { Form, Input, notification, Spin } from 'antd';
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
    margin-bottom: 10px;
  }

  @media (max-width: 450px) { 
    font-size: 35px;
    line-height: 1.2;
  }

  @media (max-width: 400px) { 
    font-size: 28px;
  }

  @media (max-width: 350px) { 
    font-size: 25px;
  }

  @media (max-width: 300px) { 
    font-size: 22px;
  }
`;

const ForgotPasswordFormContainer = styled.div`
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
    margin-bottom: 10px;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
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
  text-align: right;
  font-size: 16px;

  a {
    color: #fff;
  }

  a:hover {
    text-decoration: none;
    color: #f44336;
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

const ForgotPasswordComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();
  const [isLoading, setLoading] = useState(false);

  const onFinish = (values) => {

    setLoading(true);

    UserDataService.forgotPassword(values).then(
      (res) => {
        notification.success({
          message: `${t('well_done')}`,
          description: `${t('link_to_change_your_password_has_been_sent_to_your_email')}`
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 401:
              console.log("401 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${t('wrong_email')}`,
              });
              break;
            case 404:
              console.log("404 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${t('email_was_not_sent_to_the_mail')}`,
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
            message: 'Oops!',
            description: `${t('something_wrong_please_try_again')}`,
          });
        }
      }
    )
  }

  return (
    <SignInContainer>
      <BackgroundFilter>
        <ForgotPasswordFormContainer>
          <Form ref={formRef} onFinish={onFinish} name="validate_other">
            <Title>{t('forgot_password')}</Title>
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
            <TextContainer className="forgot-password ">
              {t('you_can')} <Link to="/register">{t('register')}</Link> {t('or')} <Link to="/login">{t('sign_in')}</Link>
            </TextContainer>
            <button className="btn"  type="submit">
              {t('submit')} {isLoading && <Spin />}
            </button> 
          </Form>
        </ForgotPasswordFormContainer>
      </BackgroundFilter>
    </SignInContainer>
  )
}

export default ForgotPasswordComponent