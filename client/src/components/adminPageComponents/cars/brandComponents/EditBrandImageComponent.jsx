import { useCallback, useState } from 'react';
import styled from 'styled-components';
import { useDropzone } from 'react-dropzone';
import { Spin, notification } from 'antd';
import BrandDataService from '../../../../services/brand/BrandDataService';
import { useTranslation } from 'react-i18next';

const ContentContainer = styled.div`
  width: 100%;
  display: flex;

  div {
    width: 100%;
    cursor: pointer;
    font-size: 16px;
    font-weight: 500;
  }
`;

const DropZoneContentWrapper = styled.div`
  wigth: 100%;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const EditBrandImageComponent = (props) => {

  const { t } = useTranslation();

  const [isLoading, setLoading] = useState(false);

  function MyDropzone() {
    const onDrop = useCallback(acceptedFiles => {
      setLoading(true);

      const file = acceptedFiles[0];
  
      const formData = new FormData();
      formData.append("brandFile", file);
  
      BrandDataService.uploadImage(props.data.id, formData).then(
        res => {
          props.fetchBrands();
          props.handleEditImageCancel();
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
      <Spin spinning={isLoading}>
        <MyDropzone/>
      </Spin>
    </ContentContainer>
  )
}

export default EditBrandImageComponent
