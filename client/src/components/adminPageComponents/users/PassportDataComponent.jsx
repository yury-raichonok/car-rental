import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { notification, Spin } from 'antd';
import Marginer from '../../marginer/Marginer';
import PassportDataService from '../../../services/user/PassportDataService';
import { useTranslation } from 'react-i18next';

const ContentContainer = styled.div`
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

  .ant-form-item {
    width: 100%;
  }

  .ant-switch-checked {
    background-color: #ea5c52;
  }
`;

const RowTitle = styled.div`
  font-size: 16px;
  width: 130px;
  text-align: left;
`;

const RowData = styled.div`
  font-size: 16px;
  width: 400px;
  text-align: left;
`;

const PassportDataComponent = (props) => {

  const { t } = useTranslation();
  const FileDownload = require('js-file-download');

  const [loading, setLoading] = useState(false);
  const [passport, setPassport] = useState();
  const [documentsLoading, setDocumentsLoading] = useState(false);

  const [directory, setDirectory] = useState({
    directory: null,
  })

  const fetchPassportData = async () => {
    setLoading(true);

    const res = await PassportDataService.findPassportDataById(props.data).catch(
      err => {
        props.handlePassportDataCancel();
        notification.error({
          message: `${t('error_when_fetching_passport_data')}`,
        });
        setLoading(false);
      }
    )

    if (res) {
      setPassport(res.data);
      directory.directory = res.data.documentsFileLink;
      setLoading(false);
    }
  }

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
      FileDownload(resp.data, `${passport.firstName + passport.lastName + ".zip"}`);
      setDocumentsLoading(false);
    }
  }

  useEffect(() => {
    fetchPassportData();
  }, []);

  return (
    <ContentContainer>
      <Spin spinning={loading}>
        {passport && (
          <>
            <Row>
              <RowTitle>{t('first_name')}:</RowTitle>
              <RowData>{passport.firstName}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('middle_name')}:</RowTitle>
              <RowData>{passport.middleName ? (passport.middleName) : ("Not specified")}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('last_name')}:</RowTitle>
              <RowData>{passport.lastName}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('date_of_birth')}:</RowTitle>
              <RowData>{passport.dateOfBirth}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('passport_series')}:</RowTitle>
              <RowData>{passport.passportSeries}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('passport_numb')}:</RowTitle>
              <RowData>{passport.passportNumber}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('documents_date_of_issue')}:</RowTitle>
              <RowData>{passport.dateOfIssue}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('validity_period')}:</RowTitle>
              <RowData>{passport.validityPeriod}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('issued_org')}:</RowTitle>
              <RowData>{passport.organizationThatIssued}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('documents_link')}:</RowTitle>
              {passport.documentsFileLink ? (
                <RowData>
                  <Spin spinning={documentsLoading}>
                    <button type="button" className="btn gray" onClick={downloadDocuments}>{t('download_documents')}</button>
                  </Spin>
                </RowData>
              ) : (
                <RowData>{t('not_specified')}</RowData>
              )}
            </Row>
          </>
        )}
      </Spin>
    </ContentContainer>
  )
}

export default PassportDataComponent
