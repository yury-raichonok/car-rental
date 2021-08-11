import React, { useState, useEffect, createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, notification, Spin } from 'antd';
import Marginer from '../../marginer/Marginer';
import RequestDataService from '../../../services/request/RequestDataService';
import { useTranslation } from 'react-i18next';
import PassportDataService from '../../../services/user/PassportDataService';

const AddNewCarContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;

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

  .ant-input:focus, .ant-input-focused {
    border: 1px solid gray;
    box-shadow: none;
  }

  .ant-input:hover {
    border: 1px solid gray;
    box-shadow: none;
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

  .btn {
    width: 100%;
    font-size: 15px;
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

  @media (max-width: 900px) { 
    flex-direction: column;
  }
`;

const Row = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;

  .btn {
    margin: 0px 5px;
  }

  .green {
    background: #1b7125;

    &:hover {
      background: #367d3e;
    }
  }

  .gray {
    background: #4842420f;
    color: #000;

    &:hover {
      background: #ea5c52;
    }
  }

  .ant-form-item {
    width: 100%;
  }

  .ant-switch-checked {
    background-color: #ea5c52;
  }
`;

const RowTitle = styled.div`
  font-size: 16px;
  width: 140px;
  text-align: left;
`;

const RowData = styled.div`
  font-size: 16px;
  width: 400px;
  text-align: left;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const LoadingScreen = styled.div`
  width: 100%;
  heigth: 321px;
`;

const PassportRequestComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();

  const [documentsLoading, setDocumentsLoading] = useState(false);

  const onReset = () => {
    formRef.current.resetFields();
  };

  const [data, setData] = useState();
  const [denying, setDenying] = useState(false);
  const [loading, setLoading] = useState(false);
  const [directory, setDirectory] = useState({
    directory: null,
  })

  const FileDownload = require('js-file-download');

  const findPassportRequestData = () => {
    setLoading(true);
    RequestDataService.findRequestPassportData(props.data).then(
      res => {
        setData(res.data);
        directory.directory = res.data.documentsFileLink;
        setLoading(false);
      }
    ).catch(
      err => {
        props.handlePassportCancel();
        notification.error({
          message: `${t('error_when_fetching_passport_data')}`,
        });
        setLoading(false);
      }
    )
  }

  const onApproveRequest = async () => {
    setLoading(true);
    const resp = await RequestDataService.approveRequest(props.data).catch(
      err => {
        props.handlePassportCancel();
        props.fetchRequests();
        notification.error({
          message: `${t('error_when_approving_passport_data')}`,
        });
        setLoading(false);
      }
    )

    if (resp) {
      setLoading(false);
      props.fetchRequests();
      props.handlePassportCancel();
      notification.success({
        message: `${t('passport_confirmation_request_approved')}`,
        description: `${t('notification_with_message_has_been_sent_to_the_user')}`
      });
    }
  }

  const onDenyRequest = (values) => {
    RequestDataService.rejectRequest(props.data, values).then(
      res => {
        onReset();
        props.handlePassportCancel();
        props.fetchRequests();
        notification.success({
          message: `${t('passport_confirmation_request_denyed')}`,
          description: `${t('notification_with_message_has_been_sent_to_the_user')}`
        });
      }
    ).catch(
      err => {
        props.fetchRequests();
        props.handlePassportCancel();
        notification.error({
          message: `${t('error_when_denying_passport_confirmation_request')}`,
        });
      }
    )
  }

  const AWS = require('aws-sdk');
    AWS.config.update(
      {
        accessKeyId: "AKIAV7WU4TWVX36ZG3TP",
        secretAccessKey: "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",
        region: "eu-west-2"
      }
    );

  const downloadDocuments = async () => {

    setDocumentsLoading(true);

    const resp = await PassportDataService.downloadFiles(directory).catch(
      err => {
        notification.error({
          message: `${t('error_when_downloading_documents')}`,
        });
        setDocumentsLoading(false);
      }
    )

    if (resp) {
      FileDownload(resp.data, `${data.firstName + data.lastName + ".zip"}`);
      setDocumentsLoading(false);
    }
  }

  useEffect(() => {
    findPassportRequestData();
  }, []);

  const handleDenyRequest = () => {
    setDenying(!denying);
  }

  return (
    <AddNewCarContainer>
      {!loading && data ? 
        <>
          <Row>
            <RowTitle>{t('first_name')}:</RowTitle>
            <RowData>{data.firstName}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          {data.middleName && (
            <>
              <Row>
                <RowTitle>{t('middle_name')}:</RowTitle>
                <RowData>{data.middleName}</RowData>
              </Row>
              <Marginer direction="vertical" margin={8} />
            </>
          )}
          <Row>
            <RowTitle>{t('last_name')}:</RowTitle>
            <RowData>{data.lastName}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('date_of_birth')}:</RowTitle>
            <RowData>{data.dateOfBirth}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('passport_series')}:</RowTitle>
            <RowData>{data.passportSeries}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('passport_numb')}:</RowTitle>
            <RowData>{data.passportNumber}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('documents_date_of_issue')}:</RowTitle>
            <RowData>{data.dateOfIssue}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('validity_per')}:</RowTitle>
            <RowData>{data.validityPeriod}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          <Row>
            <RowTitle>{t('organization')}:</RowTitle>
            <RowData>{data.organizationThatIssued}</RowData>
          </Row>
          <Marginer direction="vertical" margin={8} />
          {denying ? (
            <Form ref={formRef} name="validate_other" onFinish={onDenyRequest}>
              <Row>
                <Column>
                  <Marginer direction="vertical" margin={4} />
                  <RowTitle>{t('message')}:</RowTitle>
                </Column>
                <Form.Item name="comments" 
                  rules={[
                    {
                      required: true,
                      message: `${t('please_input_message')}`,
                    },
                    {
                      whitespace: true,
                      message: `${t('message_can_not_be_empty')}`,
                    },
                    {
                      max: 255,
                      message: `${t('message_is_too_long')}`,
                    }
                  ]}
                >
                  <Input autoComplete="off" placeholder = {t('input_message_reason_of_denying')}/>
                </Form.Item>
              </Row>
              <Row>
                <button type="submit" className="btn" onClick={handleDenyRequest}>
                  {t('cancel')}
                </button>
                <button type="submit" className="btn">
                  {t('deny_request')}
                </button>
              </Row>
          </Form>
          ) : (
            <>
              <Row>
                <Marginer direction="vertical" margin={4} />
                <RowTitle>{t('documents_link')}:</RowTitle>
                {data.documentsFileLink ? (
                  <RowData>
                    <Spin spinning={documentsLoading}>
                      <button type="button" className="btn gray" onClick={downloadDocuments}>{t('download_documents')}</button>
                    </Spin>
                  </RowData>
                ) : (
                  <RowData>{t('not_specified')}</RowData>
                )}
              </Row>
              <Marginer direction="vertical" margin={31} />
              <Row>
                <button type="button" className="btn" onClick={handleDenyRequest}>
                  {t('deny_request')}
                </button>
                <button type="button" className="btn green" onClick={onApproveRequest}>
                  {t('accept_request')}
                </button>
              </Row>
            </>
          )}
        </>
        :
        <Spin spinning={loading}>
          <LoadingScreen />
        </Spin>
      }
    </AddNewCarContainer>
  )
}

export default PassportRequestComponent
