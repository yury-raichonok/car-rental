import { useCallback, useState } from 'react';
import styled from 'styled-components';
import { useDropzone } from 'react-dropzone';
import { Spin, notification } from 'antd';
import PassportDataService from '../../../services/user/PassportDataService';
import { useTranslation } from 'react-i18next';

const ContentContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;

  
`;

const TextContainer = styled.div`
  font-size: 16px;
  font-weight: 400;
`;

const DropZoneContentWrapper = styled.div`
  wigth: 100%;
  height: 100px;
  min-height: 150px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
    font-size: 16px;
    font-weight: 500;
`;

const ConfirmPassportComponent = (props) => {

  const { t } = useTranslation();

  const [isLoading, setLoading] = useState(false);

  function MyDropzone() {
    const onDrop = useCallback(acceptedFiles => {
      setLoading(true);

      const file = acceptedFiles[0];
  
      const formData = new FormData();
      formData.append("passportFile", file);
  
      PassportDataService.uploadFile(formData).then(
        res => {
          notification.success({
            message: `${t('file_uploaded_successfully')}`,
          });
          setLoading(false);
        }  
      ).catch(
        err => {
          notification.error({
            message: `${t('file_uploading_failed')}`,
            description: `${err.response.data}`,
          });
          setLoading(false);
        }
      )
    }, [])
    const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})
  
    return (
      <DropZoneContentWrapper {...getRootProps()}>
        <input {...getInputProps()} />
        {
          isDragActive ?
            <>{t('drop_the_image_here')} ...</>
            :
            <>{t('drag_n_drop_image_here_or_click_to_select')}</>
        }
      </DropZoneContentWrapper>
    )
  }

  return (
    <ContentContainer>
      <TextContainer >
        {t('upload_files_to_verify_your_passport_data')}
      </TextContainer>
      <TextContainer>
        {t('after_submitting_the_request_you_will_receive_a_notification_of_the_decision')}
      </TextContainer>
      <Spin spinning={isLoading}>
        <MyDropzone/>
      </Spin>
    </ContentContainer>
  )
}

export default ConfirmPassportComponent
