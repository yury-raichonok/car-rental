import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { notification, Spin } from 'antd';
import Marginer from '../../marginer/Marginer';
import DrivingLicenseDataService from '../../../services/user/DrivingLicenseDataService';
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

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const DrivingLicenseDataComponent = (props) => {

  const { t } = useTranslation();
  const FileDownload = require('js-file-download');

  const [loading, setLoading] = useState(false);
  const [drivingLicense, setDrivingLicense] = useState();
  const [documentsLoading, setDocumentsLoading] = useState(false);

  const [directory, setDirectory] = useState({
    directory: null,
  })

  const fetchDrivingLicense = async () => {
    setLoading(true);

    const res = await DrivingLicenseDataService.findDrivingLicenseById(props.data).catch(
      err => {
        props.handleDrivingLicenseDataCancel();
        notification.error({
          message: `${t('error_when_fetching_driving_license_data')}`,
        });
        setLoading(false);
      }
    )

    if (res) {
      setDrivingLicense(res.data);
      directory.directory = res.data.documentsFileLink;
      setLoading(false);
    }
  }

  const downloadDocuments = async () => {

    setDocumentsLoading(true);

    const resp = await DrivingLicenseDataService.downloadFiles(directory).catch(
      err => {
        notification.error({
          message: `${t('error_when_downloading_documents')}`,
        });
        setDocumentsLoading(false);
      }
    )

    if (resp) {
      FileDownload(resp.data, `${directory.directory + ".zip"}`);
      setDocumentsLoading(false);
    }
  }

  useEffect(() => {
    fetchDrivingLicense();
  }, []);

  return (
    <ContentContainer>
      <Spin spinning={loading}>
        {drivingLicense && (
          <>
            <Row>
              <RowTitle>{t('documents_date_of_issue')}:</RowTitle>
              <RowData>{drivingLicense.dateOfIssue}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('validity_period')}:</RowTitle>
              <RowData>{drivingLicense.validityPeriod}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('issued_org')}:</RowTitle>
              <RowData>{drivingLicense.organizationThatIssued}</RowData>
            </Row>
            <Marginer direction="vertical" margin={8} />
            <Row>
              <RowTitle>{t('documents_link')}:</RowTitle>
              {drivingLicense.documentsFileLink ? (
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

export default DrivingLicenseDataComponent
