import React, { useEffect, useState, createRef } from 'react';
import styled from 'styled-components';
import { Statistic, Form, Tooltip, Spin, DatePicker, notification, Input, Switch, Modal, Popconfirm } from 'antd';
import Marginer from '../../marginer/Marginer';
import { isValidPhoneNumber } from 'react-phone-number-input';
import PhoneInput from 'react-phone-input-2';
import UserDataService from '../../../services/user/UserDataService';
import PassportDataService from '../../../services/user/PassportDataService';
import DrivingLicenseDataService from '../../../services/user/DrivingLicenseDataService';
import RequestDataService from '../../../services/request/RequestDataService';
import PhoneDataService from '../../../services/user/PhoneDataService';
import moment from 'moment';
import { 
  CheckCircleOutlined,
  ExclamationCircleOutlined,
  CloseCircleOutlined,
} from '@ant-design/icons';
import { useTranslation } from 'react-i18next';
import ConfirmPassportComponent from './ConfirmPassportComponent';
import ConfirmDrivingLicenseComponent from './ConfirmDrivingLicenseComponent';

const { Countdown } = Statistic;

const ContainerTitle = styled.div`
  display: flex;
  width: 100%;
  font-size: 28px;
  text-align: left;
  padding: 20px;
  justify-content: space-between;
  align-items: center;

  @media (max-width: 650px) { 
    font-size: 25px;
  }

  @media (max-width: 590px) { 
    font-size: 20px;
  }

  @media (max-width: 400px) { 
    font-size: 20px;
  }

  @media (max-width: 300px) { 
    flex-wrap: wrap;
  }
`;

const ProfileContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  padding: 0px 20px 0 20px;

  .ant-input-affix-wrapper:not(.ant-input-affix-wrapper-disabled):hover, #validate_other_email:hover {
    border: 1px solid gray;
  }

  .ant-input-affix-wrapper:focus, .ant-input-affix-wrapper-focused, #validate_other_email:focus {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-form {
    width: 100%;
  }

  .ant-select:not(.ant-select-disabled):hover .ant-select-selector {
    border: 1px solid gray;
  }

  .ant-select-focused:not(.ant-select-disabled).ant-select:not(.ant-select-customize-input) .ant-select-selector {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-picker:hover, .ant-picker-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input-number:hover {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input-number-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-checkbox-checked .ant-checkbox-inner {
    background-color: #f44336;
    border-color: #f44336;
  }

  .ant-checkbox-checked::after {
    border-color: #f44336;
  }

  .ant-checkbox-wrapper:hover .ant-checkbox-inner, .ant-checkbox:hover .ant-checkbox-inner, .ant-checkbox-input:focus + .ant-checkbox-inner {
    border-color: #f44336;
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

  .ant-checkbox-wrapper {
    font-size: 16px;
  }

  .ant-picker {
    width: 250px;
  }

  .ant-upload {
    button {
      width: 400px;
    }
  }

  .ant-upload-list {
    width: 400px;
  }

  .btn {
    margin-left: 10px;
    height: 32px;
    font-size: 15px;
    color: #000;
    background-color: #4842420f;;
    border: none;

    &:hover {
      background-color: #ea5c52;
      color: #fff;
    }

    &:focus {
      box-shadow: none;
    }
  }

  @media (max-width: 500px) {
    .btn {
      height: auto;
      margin-top: 10px;
    }
    
  }
`;

const ButtonContainer = styled.div`
  width: 340px;
  padding: 10px 0px;

  button {
    width: 100%;
  }

  @media (max-width: 450px) {
    width: 100%;
  }
`;

const AccountInformationContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
`;

const PassportInformationContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;

  .ant-input:focus, .ant-input-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input:hover {
    border: 1px solid gray;
    box-shadow: none;
  }
`;

const PassportDetailsContainer = styled.div`
  width: 100%;
`;

const DrivingLicenseInformationContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;

  .ant-input:focus, .ant-input-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input:hover {
    border: 1px solid gray;
    box-shadow: none;
  }
`;

const DrivingLicenseDetailsContainer = styled.div`
  width: 100%;
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: center;
  min-height: 32px;

  .anticon-close-circle {
    color: red;
    cursor: pointer;
  }

  .anticon-exclamation-circle {
    color: #ffb100;
    cursor: pointer;
  }

  .anticon-check-circle {
    color: green;
    cursor: pointer;
  }

  .anticon-plus {
    color: green;
  }

  @media (max-width: 750px) { 
    flex-wrap: wrap;
  }
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const RowWithTopAlign = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  align-items: top;

  .ant-statistic-content {
    font-size: 20px;
  }

  .ant-form-item {
    width: 250px;
  }


  .ant-form-item-explain {
    max-width: 250px;
  }

  .ant-input-password {
    width: 250px;
  }

  .ant-switch-checked {
    background-color: #ea5c52;
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

  @media (max-width: 750px) { 
    flex-wrap: wrap;
  }
`;

const RowTitle = styled.div`
  font-size: 16px;
  width: 150px;
  text-align: left;
`;

const PhonesContainer = styled.div`
  display: flex;
  flex-direction: column;
  text-align: left;
`;

const PhoneNumberContainer = styled.div`
  display: flex;
  text-align: left;
  margin-bottom: 5px;

  &:hover {
    cursor: pointer;
  }
  
`;

const InfoRowTitle = styled.div`
  font-size: 16px;
  text-align: left;
  margin-bottom: 15px;
`;

const InfoContainer = styled.div`
  height: 27px;
  margin-top: 5px;
  margin-left: 12px;
`;

const WarningText = styled.div`
  text-align: center;
  font-size: 16px;
  height: 32px;
`;

const ProfileComponent = (props) => {

  const { t } = useTranslation();

  const [data, setData] = useState();
  const [isLoading, setLoading] = useState(false);
  const [isPassportLoading, setPassportLoading] = useState(false);
  const [isLicenseLoading, setLicenseLoading] = useState(false);
  const [isPasswordChangeLoading, setPasswordChangeLoading] = useState(false);
  const [isEmailChangeLoading, setEmailChangeLoading] = useState(false);

  const [sendEmail, setSendEmail] = useState(false);
  const [isPhoneValid, setIsPhoneValid] = useState(true);
  const [editEmail, setEditEmail] = useState(false);
  const [addPhoneNumber, setAddPhoneNumber] = useState(false);
  const [editPassword, setEditPassword] = useState(false);
  const formRef = createRef();

  const [isValidationReqSended, setValidationReqSended] = useState(false);

  const [passport, setPassport] = useState();
  const [editPassport, setEditPassport] = useState(false);
  const [showPassportDetails, setShowPassportDetails] = useState(false);
  const [showConfirmPassportInfo, setShowConfirmPassportInfo] = useState(false);

  const [drivingLicense, setDrivingLicense] = useState();
  const [editDrivingLicense, setEditDrivingLicense] = useState(false);
  const [showDrivingLicenseDetails, setShowDrivingLicenseDetails] = useState(false);
  const [showConfirmDrivingLicenseInfo, setShowConfirmDrivingLicenseInfo] = useState(false);

  const [requestLoading, setRequestLoading] = useState(false);

  const [token, setToken] = useState({
    token: null,
    phoneNumber: null
  })

  const [passportConfirmationRequest, setPassportConfirmationRequest] = useState({
    rentalRequestType: "PASSPORT_CONFIRMATION_REQUEST",
  });

  const [drivingLicenseConfirmationRequest, setDrivingLicenseConfirmationRequest]= useState({
    rentalRequestType: "DRIVING_LICENSE_CONFIRMATION_REQUEST",
  })

  function onEditePassportChange(checked) {
    setEditPassport(checked);
  }

  function onEditeDrivingLicenseChange(checked) {
    setEditDrivingLicense(checked);
  }

  function handleSwitchPassportInformation() {
    setShowPassportDetails(!showPassportDetails);
    if (!showPassportDetails) {
      fetchPassportData();
    } else {
      setEditPassport(false);
    }
  }

  function handleSwitchDrivingLicenseInformation() {
    setShowDrivingLicenseDetails(!showDrivingLicenseDetails);
    if (!showDrivingLicenseDetails) {
      fetchDrivingLicenseData();
    } else {
      setEditDrivingLicense(false);
    }
  }

  function handleConfirmPassportInfo() {
    setShowConfirmPassportInfo(true);
  }

  function handleConfirmPassportInfoCancel() {
    setShowConfirmPassportInfo(false);
  }

  function handleConfirmDrivingLicenseInfo() {
    setShowConfirmDrivingLicenseInfo(true);
  }

  function handleConfirmDrivingLicenseInfoCancel() {
    setShowConfirmDrivingLicenseInfo(false);
  }

  const fetchUserProfileData = async () => {
    setLoading(true);
    const resp = await UserDataService.getUserProfile().catch((err) => {
      console.log("Error: ", err);
    });

    if (resp) {
      setData(resp.data);
      console.log(props.user);
      console.log(resp.data);
    }

    setLoading(false);
  }

  const fetchPassportData = async () => {
    setPassportLoading(true);

    const res = await PassportDataService.getUserPassportData().catch(
      err => {
        console.log(err);
      }
    )

    if (res) {
      setPassport(res.data);
    }

    setPassportLoading(false);
  }

  const fetchDrivingLicenseData = async () => {
    setLicenseLoading(true);
    
    const res = await DrivingLicenseDataService.getUserDrivingLicenseData().catch(
      err => {
        notification.error({
          message: `${t('something_wrong_please_try_again')}`,
        });
      }
    )

    if (res) {
      setDrivingLicense(res.data);
    }

    setLicenseLoading(false);
  }

  const onConfirmPhoneNumber = () => {
    

    PhoneDataService.create(token).then(
      res => {
        notification.success({
          message: `${t('phone_saved')}`
        });
        setValidationReqSended(false);
        setValidationReqSended(false);
        fetchUserProfileData();
      }  
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              console.log("406 status");
              notification.error({
                message: `${t('token_expired_try_again')}`,
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
  }

  const onPhoneNumberFinish = () => {

    PhoneDataService.sendConfirmationSms(token).then(
      res => {
        setValidationReqSended(true);
        notification.success({
          message: `${t('message_sended')}`,
          description: `${t('to_confirm_number_enter_number_from_sms')}`
        });
      }  
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('this_phone_number_is_already_taken')}`,
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
  };

  const onEditDrivingLicense = async (values) => {

    const res = await DrivingLicenseDataService.edit(values).catch(
      err => {
        notification.error({
          message: `${t('something_wrong_please_try_again')}`,
        });
      }
    )

    if (res) {
      fetchDrivingLicenseData();
      setEditDrivingLicense(false);
      fetchUserProfileData();
      notification.success({
        message: `${t('driving_license_information_successfully_added')}`,
        description: `${t('send_scans_of_driving_license_to_confirm_information')}`
      });
    }

  };

  const onEditPassport = async (values) => {

    const res = await PassportDataService.edit(values).catch(
      err => {
        notification.error({
          message: `${t('something_wrong_please_try_again')}`,
        });
      }
    )

    if (res) {
      fetchPassportData();
      setEditPassport(false);
      fetchUserProfileData();
      notification.success({
        message: `${t('passport_information_successfully_added')}`,
        description: `${t('send_scans_of_passport_to_confirm_information')}`
      });
    }
  };

  const onPasswordChangingFinish = async (values) => {
    setPasswordChangeLoading(true);

    const res = await UserDataService.updateUser(data.id, values).catch(
      err => {
        notification.error({
          message: `${t('something_went_wrong')}`,
          description: `${t('password_changing_failed')}`
        });
      }
    )

    if (res) {
      fetchUserProfileData();
      setEditPassword(false);
      notification.success({
        message: `${t('password_successfully_changed')}`
      });
    }

    setPasswordChangeLoading(false);
  }

  const onEmailChangingFinish = async (values) => {
    setEmailChangeLoading(true);

    const res = await UserDataService.updateUser(data.id, values).catch(
      err => {
        if (err && err.response) {
          switch(err.response.status){
            case 405:
              notification.error({
                message: `${t('user_with_such_email_already_exists')}`,
              });
              break;
          }
        } else {
          notification.error({
            message: `${t('something_went_wrong')}`,
            description: `${t('email_changing_failed')}`
          });
        }
        
      }
    )

    if (res) {
      fetchUserProfileData();
      setEditEmail(false);
      notification.success({
        message: `${t('email_successfully_changed')}`
      });
    }

    setEmailChangeLoading(false);
  }

  const sendPassportConfirmationRequest = async () => {
    setRequestLoading(true);

    const res = await RequestDataService.create(passportConfirmationRequest).catch(
      err => {
        if (err && err.response) {
          notification.error({
            message: `${t('something_went_wrong')}`,
            description: `${t('passport_confirmation_request_not_sent')}`
          });
        }
      }
    )

    if (res) {
      fetchUserProfileData();
      setShowConfirmPassportInfo(false);
      notification.success({
        message: `${t('passport_confirmation_request_successfully_sent')}`
      });
    }

    setRequestLoading(false);
  }

  const sendDrivingLicenseConfirmationRequest = async () => {
    setRequestLoading(true);

    const res = await RequestDataService.create(drivingLicenseConfirmationRequest).catch(
      err => {
        if (err && err.response) {
          notification.error({
            message: `${t('something_went_wrong')}`,
            description: `${t('driving_license_confirmation_request_not_sent')}`
          });
        }
      }
    )

    if (res) {
      fetchUserProfileData();
      setShowConfirmDrivingLicenseInfo(false);
      notification.success({
        message: `${t('driving_license_confirmation_request_successfully_sent')}`
      });
    }

    setRequestLoading(false);
  }

  const updatePhoneStatus = async (id) => {
    setLoading(true);

    const resp = await PhoneDataService.updatePhoneStatus(id).catch((err) => {
      
      if(err && err.response){
        switch(err.response.status){
          case 405:
            notification.error({
              message: `${t('this_phone_number_is_already_taken')}`,
            });
            break;
          default:
            notification.error({
              message: `${t('error_when_updating_phone_status')}`,
            });
            break;
        }
      } else{
        notification.error({
          message: `${t('something_wrong_please_try_again')}`,
        });
      }
      setLoading(false);
    });

    if(resp) {
      setLoading(false);
      notification.success({
        message: `${t('phone_status_updated')}`,
      });
      fetchUserProfileData();
    }
  }

  useEffect(() => {
    fetchUserProfileData();
  }, []);

  const handleCancelEditEmail = () => {
    setEditEmail(false)
  }  

  const handleEditEmail = () => {
    setEditEmail(true)
  }  

  const handleCancelEditPassword = () => {
    setEditPassword(false)
  }  

  const handleEditPassword = () => {
    setEditPassword(true)
  }  

  const handleCancelAddPhoneNumber = () => {
    setAddPhoneNumber(false)
  }  

  const handleAddPhoneNumber = () => {
    setAddPhoneNumber(true)
  }  

  function onCounterChange(val) {
    if (val < 1 * 1000) {
      setValidationReqSended(false);
      token.token = null;
      token.phoneNumber = null;
    }
  }

  function onConfirmPhoneCancel() {
    setValidationReqSended(false);
    token.token = null;
    token.phoneNumber = null;
  }

  function disabledDate(current) {
    return current && current > moment().endOf("day");
  }

  function disabledDatePast(current) {
    return current && current < moment().endOf("day");
  }

  const handleConfirmEmail = async () => {
    setSendEmail(true);
    const resp = await UserDataService.confirmEmail().catch((err) => {
      console.log("Error: ", err);
    });

    if(resp) {
      notification.success({
        message: `${t('email_confirmation')}`,
        description: `${t('an_email_confirmation_link_has_been_sent_to_your_email')}`
      });
    }

    setSendEmail(false);
  }

  const emailChangeValidateMessages = {
    required: `${t('email_is_required')}`,
    types: { email: `${t('email_is_not_valid')}` },
    max: `${t('email_is_too_long')}`,
  };

  const passwordChangeValidateMessages = {
    required: `${t('password_is_required')}`,
    pattern: {
      mismatch: `${t('password_must_be_from_8_characters_min_one_uppercase_letter_one_lowercase_letter_and_one_number')}`,
    },
  };

  const isDataEmpty = !data || (data && data.length === 0);

  return (
    <ProfileContainer>
      <AccountInformationContainer>
        <ContainerTitle>
          {t('account_information')}
        </ContainerTitle>
        { isLoading ? (
          <div>
            {t('loading')} . . . <Spin/>
            <Marginer direction="vertical" margin={20} />
          </div>
        ) : (
          <>
            <Row>
              {isDataEmpty && !isLoading && (<WarningText>{t('data_not_available')}</WarningText>)}
              {!isDataEmpty && !isLoading && 
                <Row>
                  {editEmail ? (
                    <Form ref={formRef} name="validate_other" validateMessages={emailChangeValidateMessages} onFinish={onEmailChangingFinish}>
                      <RowWithTopAlign>
                        <Column>
                          <Marginer direction="vertical" margin={3} />
                          <RowTitle>{t('email')}:&ensp;</RowTitle>
                        </Column>
                        <Form.Item 
                          name="email"
                          rules={[
                            {
                              max: 255,
                              required: true,
                              type: 'email',
                            },
                          ]}
                        >
                          <Input autoComplete="off" placeholder={t('enter_new_email')}/>
                        </Form.Item>
                        <div>
                          <button className="btn" type="submit">{t('submit')}</button>
                          <button className="btn" onClick={handleCancelEditEmail}>{t('cancel')}</button>
                        </div>
                      </RowWithTopAlign>
                    </Form>
                  ) : (
                    <>
                      <RowTitle>{t('email')}:&ensp;</RowTitle>
                      <div>{data.email}&ensp;</div>
                      {data.emailConfirmed ? (
                        <Tooltip title={t('email_confirmed')} color="#000000c2">
                          <CheckCircleOutlined />
                        </Tooltip>
                      ) : (
                        <Tooltip title={t('email_not_confirmed')} color="#000000c2">
                          <CloseCircleOutlined />
                        </Tooltip>
                      )}
                      {!isDataEmpty && !isLoading && !data.emailConfirmed && 
                        <Spin spinning={sendEmail}>
                          <button className="btn" onClick={handleConfirmEmail}>{t('confirm_email')}</button>
                        </Spin>
                      }
                      <button className="btn" onClick={handleEditEmail}>{t('change_email')}</button>
                    </>
                  )}
                </Row>
              }
            </Row>
            {!editEmail &&
              <Marginer direction="vertical" margin={24} />
            }
            
            <RowWithTopAlign>
              <RowTitle>{t('phone_numbers')}:&ensp;</RowTitle>
              <PhonesContainer>
                {!isDataEmpty && !isLoading && data.phones.length > 0 ? (data.phones.map((phone) => (
                  <Popconfirm title={t('lock_phone')} onConfirm={() => updatePhoneStatus(phone.id)}>
                    <PhoneNumberContainer key={phone}>+ {phone}</PhoneNumberContainer>
                  </Popconfirm>
                ))) : (
                  <Row>
                    <div>{t('no_phones')}&ensp;</div>
                    <Tooltip title={t('to_send_order_you_need_at_least_one_confirmed_phone_number')} color="#000000c2">
                      <ExclamationCircleOutlined />
                    </Tooltip>
                  </Row>
                )}
              </PhonesContainer>
            </RowWithTopAlign>

            {addPhoneNumber ? (
              <>
                {isValidationReqSended ? (
                  <Form ref={formRef} name="validate_other" onFinish={onConfirmPhoneNumber}>
                    <Marginer direction="vertical" margin={10} />
                    <RowWithTopAlign>
                      <Form.Item 
                        name="token"
                        rules={[
                          {
                            required: true,
                            message: `${t('please_enter_numbers_from_sms')}`,
                          },
                          {
                            pattern: "^[0-9]{4}$",
                            message: `${t('incorrect_input')}`,
                          },
                        ]}
                        onChange={e => token.token = e.target.value}
                      >
                        <Input autoComplete="off"  />
                      </Form.Item>
                      <Marginer direction="horizontal" margin={10} />
                      <Countdown format="mm:ss" value={Date.now() + 120 * 1000} onChange={onCounterChange} />
                      <div>
                        <button className="btn" type="submit">{t('confirm_phone_number')}</button>
                        <button className="btn" onClick={onConfirmPhoneCancel}>{t('cancel')}</button>
                      </div>
                    </RowWithTopAlign>
                    
                  </Form>
                ) : (
                  <Form ref={formRef} name="validate_other" onFinish={onPhoneNumberFinish}>
                    <Marginer direction="vertical" margin={10} />
                    <RowWithTopAlign>
                      <Form.Item 
                        name="phoneNumber"
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
                          onChange={e => token.phoneNumber = e}
                        />
                      </Form.Item>
                      <div>
                        <button className="btn" type="submit">{t('send_confirmation_sms')}</button>
                        <button className="btn" onClick={handleCancelAddPhoneNumber}>{t('cancel')}</button>
                      </div>
                    </RowWithTopAlign>
                  </Form>
                )}
              </>
            ) : (
              <ButtonContainer>
                <button className="btn" onClick={handleAddPhoneNumber}>{t('add_phone_number')}</button>
              </ButtonContainer>
            )}
            

            {editPassword ? (
              <Form ref={formRef} name="validate_other" validateMessages={passwordChangeValidateMessages} onFinish={onPasswordChangingFinish}>
                <RowWithTopAlign>
                  <Column>
                    <Marginer direction="vertical" margin={3} />
                    <RowTitle>{t('Password')}:&ensp;</RowTitle>
                  </Column>
                  <Form.Item 
                    name="password"
                    rules={[
                      {
                        pattern: "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                        required: true,
                      },
                    ]}
                  >
                    <Input.Password type="password" placeholder={t('enter_password')}/>
                  </Form.Item>
                </RowWithTopAlign>
                <RowWithTopAlign>
                  <Column>
                    <Marginer direction="vertical" margin={3} />
                    <RowTitle>{t('confirm_password')}:&ensp;</RowTitle>
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
                </RowWithTopAlign>
                <RowWithTopAlign>
                  <button className="btn" type="submit">{t('submit')}</button>
                  <button className="btn" onClick={handleCancelEditPassword}>{t('cancel')}</button>
                </RowWithTopAlign>
              </Form>
            ) : (
              <ButtonContainer>
                <button className="btn" onClick={handleEditPassword}>{t('change_password')}</button>
              </ButtonContainer>
            )}
          </>
        )}
      </AccountInformationContainer>

      <PassportInformationContainer>
        <ContainerTitle>
          {t('passport_information')}
        </ContainerTitle>
        { isLoading ? (
          <div>
            {t('loading')} . . . <Spin/>
            <Marginer direction="vertical" margin={20} />
          </div>
        ) : (
          <>
            <Row>
              <div>{t('passport')}&ensp;</div>
              {!isDataEmpty && !isLoading && data.passportStatus === "CONFIRMED" && 
                <>
                  {t('confirmed_low')}&ensp;
                  <CheckCircleOutlined />
                </>
              }
              {!isDataEmpty && !isLoading && data.passportStatus === "UNDER_CONSIDERATION" && 
                <>
                  {t('confirmation_request_sended')}&ensp;
                  <ExclamationCircleOutlined />
                </>
              }
              {!isDataEmpty && !isLoading && data.passportStatus === "NOT_CONFIRMED" && 
                <>
                  {t('is_not_confirmed')}&ensp;
                  <Tooltip title={t('to_send_order_you_need_to_confirm_your_passport')} color="#000000c2">
                    <CloseCircleOutlined />
                  </Tooltip>
                </>
              }
              { !isLoading && data && data.passportStatus === null && 
                <>
                  {t('passport_not_specified')}&ensp;
                  <Tooltip title={t('to_send_order_you_need_to_specify_and_confirm_your_passport')} color="#000000c2">
                    <ExclamationCircleOutlined />
                  </Tooltip>
                </>
              }
              <div>
                <button className="btn" onClick={handleSwitchPassportInformation}>{showPassportDetails ? `${t('close_passport_info')}` : `${t('view_passport_info')}` }</button>
              </div>
            </Row>
            <Marginer direction="vertical" margin={10} />
            
            <PassportDetailsContainer style={showPassportDetails ? ({display: 'block'}) : ({display: 'none'})}>
              <Form ref={formRef} name="validate_other" onFinish={onEditPassport}>
                { isPassportLoading ? (<WarningText>{t('loading')} . . . <Spin /></WarningText>) : (
                  <>
                    {passport && !isPassportLoading ? (
                      <>
                        <RowWithTopAlign>
                          <Column>
                            <RowTitle>{t('edit_information')}:&ensp;</RowTitle>
                          </Column>
                          <Switch onChange={onEditePassportChange} />
                        </RowWithTopAlign>
                        <Marginer direction="vertical" margin={24} />
                      </>
                    ) : (
                      <InfoRowTitle>{t('enter_your_passport_details')}:</InfoRowTitle>
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('first_name')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.firstName}
                          name="firstName"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_enter_first_name')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('please_enter_first_name')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_first_name')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="firstName"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_enter_first_name')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('please_enter_first_name')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_first_name')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.firstName}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('mid_name_opt')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.middleName}
                          name="middleName"
                          rules={[
                            {
                              whitespace: true,
                              message: `${t('middle_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_middle_name')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="middleName"
                          rules={[
                            {
                              whitespace: true,
                              message: `${t('middle_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_middle_name')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.middleName}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('last_name')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.lastName}
                          name="lastName"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_enter_last_name')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('last_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_last_name')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="lastName"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_enter_last_name')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('last_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_last_name')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.lastName}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('date_of_birth')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {moment(new Date(passport.dateOfBirth))}
                          name="dateOfBirth"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_date_of_birth')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_date_of_birth')} disabledDate={disabledDate}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="dateOfBirth"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_date_of_birth')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_date_of_birth')} disabledDate={disabledDate}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.dateOfBirth}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('passport_series')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.passportSeries}
                          name="passportSeries"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_passport_series')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('passport_series_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_passport_series')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="passportSeries"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_passport_series')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('passport_series_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_passport_series')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.passportSeries}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('passport_number')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.passportNumber}
                          name="passportNumber"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_passport_number')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('passport_number_can_not_be_empty')}`,
                            },
                            {
                              pattern: "[0-9]",
                              message: `${t('incorrect_input')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_passport_number')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="passportNumber"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_passport_number')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('passport_number_can_not_be_empty')}`,
                            },
                            {
                              pattern: "[0-9]",
                              message: `${t('incorrect_input')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_passport_number')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.passportNumber}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('documents_date_of_issue')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {moment(new Date(passport.dateOfIssue))}
                          name="dateOfIssue"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_date_of_issue_document')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_date_of_issue_document')} disabledDate={disabledDate}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="dateOfIssue"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_date_of_issue_document')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_date_of_issue_document')} disabledDate={disabledDate}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.dateOfIssue}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('validity_period')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {moment(new Date(passport.validityPeriod))}
                          name="validityPeriod"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_validity_period')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_validity_period')} disabledDate={disabledDatePast}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="validityPeriod"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_select_validity_period')}`,
                            },
                          ]}
                        >
                          <DatePicker placeholder={t('select_validity_period')} disabledDate={disabledDatePast}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.validityPeriod}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    <RowWithTopAlign>
                      <Column>
                        <Marginer direction="vertical" margin={3} />
                        <RowTitle>{t('issued_org')}:&ensp;</RowTitle>
                      </Column>
                      {passport && editPassport && (
                        <Form.Item 
                          initialValue = {passport.organizationThatIssued}
                          name="organizationThatIssued"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_organization')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('organization_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_organization_that_issued')}/>
                        </Form.Item>
                      )}
                      {!passport && !editPassport && (
                        <Form.Item 
                          name="organizationThatIssued"
                          rules={[
                            {
                              required: true,
                              message: `${t('please_input_organization')}`,
                            },
                            {
                              whitespace: true,
                              message: `${t('organization_name_can_not_be_empty')}`,
                            },
                          ]}
                        >
                          <Input autoComplete="off" type="text" placeholder={t('enter_organization_that_issued')}/>
                        </Form.Item>
                      )}
                      {passport && !editPassport && (
                        <InfoContainer>{passport.organizationThatIssued}</InfoContainer>
                      )}
                    </RowWithTopAlign>
                    {passport && !editPassport && (
                      <Marginer direction="vertical" margin={24} />
                    )}
                    {passport && !editPassport ? (
                      <>
                        {data && data.passportStatus == "NOT_CONFIRMED" && (
                          <ButtonContainer>
                            <button className="btn" type="button" onClick={handleConfirmPassportInfo}>{t('confirm_passport_info')}</button>
                          </ButtonContainer>
                        )}
                      </>
                    ) : (
                      <ButtonContainer>
                        <button className="btn" type="submit">{t('submit')}</button>
                      </ButtonContainer>
                    )}
                  </>
                )}
              </Form>
            </PassportDetailsContainer>
          </>
        )}
      </PassportInformationContainer>

      <DrivingLicenseInformationContainer>
        <ContainerTitle>{t('driving_license_information')}</ContainerTitle>
        { isLoading ? (
          <div>
            {t('loading')} . . . <Spin/>
            <Marginer direction="vertical" margin={20} />
          </div>
        ) : (
          <div>
            <Row>
              <div>{t('driving_license')}&ensp;</div>
              {!isDataEmpty && !isLoading && data.drivingLicenseStatus == "CONFIRMED" && 
                <>
                  {t('confirmed_low')}&ensp;
                  <CheckCircleOutlined />
                </>
              }
              {!isDataEmpty && !isLoading && data.drivingLicenseStatus == "UNDER_CONSIDERATION" && 
                <>
                  {t('confirmation_request_sended')}&ensp;
                  <ExclamationCircleOutlined />
                </>
              }
              {!isDataEmpty && !isLoading && data.drivingLicenseStatus == "NOT_CONFIRMED" && 
                <>
                  {t('is_not_confirmed')}&ensp;
                  <Tooltip title={t('to_send_order_you_need_to_confirm_your_driving_license')} color="#000000c2">
                    <CloseCircleOutlined />
                  </Tooltip>
                </>
              }
              { !isLoading && data && data.drivingLicenseStatus === null && 
                <>
                  {t('driving_license_not_specified')}&ensp;
                  <Tooltip title={t('to_send_order_you_need_to_specify_and_confirm_your_driving_license')} color="#000000c2">
                    <ExclamationCircleOutlined />
                  </Tooltip>
                </>
              }
              <div>
                <button className="btn" onClick={handleSwitchDrivingLicenseInformation}>{showDrivingLicenseDetails ? `${t('close_driving_license_info')}` : `${t('view_driving_license_info')}`}</button>
              </div>
            </Row>
            <Marginer direction="vertical" margin={10} />
            
            <DrivingLicenseDetailsContainer style={showDrivingLicenseDetails ? ({display: 'block'}) : ({display: 'none'})}>
              <Form ref={formRef} name="validate_other" onFinish={onEditDrivingLicense}>
                { isLicenseLoading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
                {drivingLicense && !isLicenseLoading ? (
                  <>
                    <RowWithTopAlign>
                      <Column>
                        <RowTitle>{t('edit_information')}:&ensp;</RowTitle>
                      </Column>
                      <Switch onChange={onEditeDrivingLicenseChange} />
                    </RowWithTopAlign>
                    <Marginer direction="vertical" margin={24} />
                  </>
                ) : (
                  <InfoRowTitle>{t('enter_your_driving_license_details')}:</InfoRowTitle>
                )}
                <RowWithTopAlign>
                  <Column>
                    <Marginer direction="vertical" margin={3} />
                    <RowTitle>{t('documents_date_of_issue')}:&ensp;</RowTitle>
                  </Column>
                  {drivingLicense && editDrivingLicense && (
                    <Form.Item 
                      initialValue = {moment(new Date(drivingLicense.dateOfIssue))}
                      name="dateOfIssue"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_select_date_of_issue_document')}`,
                        },
                      ]}
                    >
                      <DatePicker placeholder={t('select_date_of_issue_document')} disabledDate={disabledDate}/>
                    </Form.Item>
                  )}
                  {!drivingLicense && !editDrivingLicense && (
                    <Form.Item 
                      name="dateOfIssue"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_select_date_of_issue_document')}`,
                        },
                      ]}
                    >
                      <DatePicker placeholder={t('select_date_of_issue_document')} disabledDate={disabledDate}/>
                    </Form.Item>
                  )}
                  {drivingLicense && !editDrivingLicense && (
                    <InfoContainer>{drivingLicense.dateOfIssue}</InfoContainer>
                  )}
                </RowWithTopAlign>
                {drivingLicense && !editDrivingLicense && (
                  <Marginer direction="vertical" margin={24} />
                )}
                <RowWithTopAlign>
                  <Column>
                    <Marginer direction="vertical" margin={3} />
                    <RowTitle>{t('validity_period')}:&ensp;</RowTitle>
                  </Column>
                  {drivingLicense && editDrivingLicense && (
                    <Form.Item 
                      initialValue = {moment(new Date(drivingLicense.validityPeriod))}
                      name="validityPeriod"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_select_validity_period')}`,
                        },
                      ]}
                    >
                      <DatePicker placeholder={t('select_validity_period')} disabledDate={disabledDatePast}/>
                    </Form.Item>
                  )}
                  {!drivingLicense && !editDrivingLicense && (
                    <Form.Item 
                      name="validityPeriod"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_select_validity_period')}`,
                        },
                      ]}
                    >
                      <DatePicker placeholder={t('select_validity_period')} disabledDate={disabledDatePast}/>
                    </Form.Item>
                  )}
                  {drivingLicense && !editDrivingLicense && (
                    <InfoContainer>{drivingLicense.validityPeriod}</InfoContainer>
                  )}
                </RowWithTopAlign>
                {drivingLicense && !editDrivingLicense && (
                  <Marginer direction="vertical" margin={24} />
                )}
                <RowWithTopAlign>
                  <Column>
                    <Marginer direction="vertical" margin={3} />
                    <RowTitle>{t('issued_org')}:&ensp;</RowTitle>
                  </Column>
                  {drivingLicense && editDrivingLicense && (
                    <Form.Item 
                      initialValue = {drivingLicense.organizationThatIssued}
                      name="organizationThatIssued"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_input_organization')}`,
                        },
                        {
                          whitespace: true,
                          message: `${t('organization_name_can_not_be_empty')}`,
                        },
                      ]}
                    >
                      <Input autoComplete="off" type="text" placeholder={t('enter_organization_that_issued')}/>
                    </Form.Item>
                  )}
                  {!drivingLicense && !editDrivingLicense && (
                    <Form.Item 
                      name="organizationThatIssued"
                      rules={[
                        {
                          required: true,
                          message: `${t('please_input_organization')}`,
                        },
                        {
                          whitespace: true,
                          message: `${t('organization_name_can_not_be_empty')}`,
                        },
                      ]}
                    >
                      <Input autoComplete="off" type="text" placeholder={t('enter_organization_that_issued')}/>
                    </Form.Item>
                  )}
                  {drivingLicense && !editDrivingLicense && (
                    <InfoContainer>{drivingLicense.organizationThatIssued}</InfoContainer>
                  )}
                </RowWithTopAlign>
                {drivingLicense && !editDrivingLicense && (
                  <Marginer direction="vertical" margin={24} />
                )}
                {drivingLicense && !editDrivingLicense ? (
                  <>
                    {(data && data.drivingLicenseStatus == "NOT_CONFIRMED") && (
                      <ButtonContainer>
                        <button className="btn" type="button" onClick={handleConfirmDrivingLicenseInfo}>{t('confirm_driving_license_info')}</button>
                      </ButtonContainer>
                    )}
                  </>
                ) : (
                  <ButtonContainer>
                    <button className="btn" type="submit">{t('submit')}</button>
                  </ButtonContainer>
                )}
              </Form>
            </DrivingLicenseDetailsContainer>
          </div>
        )}
      </DrivingLicenseInformationContainer>
      <Modal
        width="600px"
        visible={showConfirmPassportInfo}
        title={t('passport_data_confirmation')}
        onCancel={handleConfirmPassportInfoCancel}
        footer={[
          <button className="btn" type="button" onClick={sendPassportConfirmationRequest}>{t('send_confirmation_request')}</button>
        ]}
      >
        <ConfirmPassportComponent />
      </Modal>
      <Modal
        width="600px"
        visible={showConfirmDrivingLicenseInfo}
        title={t('driving_license_data_confirmation')}
        onCancel={handleConfirmDrivingLicenseInfoCancel}
        footer={[
          <button className="btn" type="button" onClick={sendDrivingLicenseConfirmationRequest}>{t('send_confirmation_request')}</button>
        ]}
      >
        <ConfirmDrivingLicenseComponent />
      </Modal>
    </ProfileContainer>
  )
}

export default ProfileComponent
