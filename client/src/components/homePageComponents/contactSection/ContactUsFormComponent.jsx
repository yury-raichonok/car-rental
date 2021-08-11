import React, { useState } from 'react';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import { Form, Input,  notification } from 'antd';
import styled from "styled-components";
import { isValidPhoneNumber } from 'react-phone-number-input';
import MessageDataService from '../../../services/message/MessageDataService';
import { useTranslation } from 'react-i18next';
import Marginer from '../../marginer/Marginer';

const ContactUsFormContainer = styled.div`
  background-color: #fff;
  padding: 40px 30px 10px 30px;
  border-radius: 10px;
  color: #fff;
  max-width: 100%;
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

const ContactUsForm = () => {

  const { t } = useTranslation();

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
  )
}

export default ContactUsForm
