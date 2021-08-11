import React, { useState, createRef } from 'react';
import styled from 'styled-components';
import UserDataService from '../../services/user/UserDataService';
import BackgroundImage from '../../images/5.jpg';
import { Redirect } from 'react-router';
import { Form, Input, notification, Spin } from 'antd';
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';

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
  margin: 0;
  font-size: 42px;
  font-weight: 700;
  color: #fff;
  line-height: 1.7;

  @media (max-width: 450px) { 
    font-size: 35px;
    line-height: 1.2;
    margin-bottom: 10px;
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

const PasswordRestorationFormContainer = styled.form`
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

  .btn {
    width: 200px;
    font-size: 17px;
    color: #fff;
    background-color: #f44336;
    border: none;
    margin-bottom: 15px;
    margin-top: 15px;

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

  @media (max-width: 700px) { 
    width: 90%;
  }
`;

const ResetPasswordComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();
  const [reset, setReset] = useState(false);
  const [isLoading, setLoading] = useState(false);

  const data = {
    token: props.match.params.id,
    password: null,
    confirmPassword: null,
  }

  const handlePasswordChange = (value) => {
    data.password = value;
  }

  const handleConfirmPasswordChange = (value) => {
    data.confirmPassword = value;
  }

  const onFinish = () => {
    
    setLoading(true);

    UserDataService.resetPassword(data).then(
      () => {
        setReset(true)
        notification.success({
          message: `${t('congratulations')}`,
          description: `${t('your_password_successfully_changed')}`
        });
      }
    ).catch(
      err => {
        if(err && err.response){
          
          switch(err.response.status){
            case 400:
              console.log("400 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${err.response.data}`,
              });
              break;
            case 401:
              console.log("401 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${t('wrong_credentials')}`,
              });
              break;
            case 405:
              console.log("405 status");
              notification.error({
                message: `${t('oops')}`,
                description: `${t('token_expired_or_already_used')}`,
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
      }
    )

    setLoading(false);
  }

  if(reset) {
    return <Redirect to="/login" />
  }

  return (
    <SignInContainer>
      <BackgroundFilter>
        <PasswordRestorationFormContainer>
          <Form ref={formRef} onFinish={onFinish} name="validate_other">
            <Title>{t('reset_password')}</Title>
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
                <Input.Password type="password" placeholder={t('enter_password')} onChange={handlePasswordChange}/>
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
                <Input.Password type="password" placeholder={t('confirm_password')}  onChange={handleConfirmPasswordChange}/>
              </Form.Item>
            </Row>
            <button className="btn" type="submit">
              {t('submit')} {isLoading && <Spin />}
            </button> 
          </Form>
        </PasswordRestorationFormContainer> 
      </BackgroundFilter>
    </SignInContainer>
  )
}

export default ResetPasswordComponent