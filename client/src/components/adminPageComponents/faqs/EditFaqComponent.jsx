import { createRef } from 'react';
import styled from 'styled-components';
import { Form, Input, notification } from 'antd';
import FaqDataService from '../../../services/faq/FaqDataService';
import Marginer from '../../marginer/Marginer';
import { useTranslation } from 'react-i18next';

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

const HorizontalLine = styled.div`
  width: 100%;
  border-bottom: 1px solid #f0f0f0;
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
  font-size: 15px;
  width: 170px;
  text-align: left;
`;

const Column = styled.div`
  display: flex;
  flex-direction: column;
`;

const EditFaqComponent = (props) => {

  const { t } = useTranslation();

  const formRef = createRef();

  const onReset = () => {
    formRef.current.resetFields();
  };

  const onFinish = (values) => {

    FaqDataService.update(props.data.id, values).then(
      res => {
        if(res.status === 200) {
          onReset();
          props.handleEditInfoCancel();
          props.fetchFaqs();
          notification.success({
            message: `${t('faq_updated')}`,
          });
        }
      }
    ).catch(
      err => {
        if(err && err.response){
          switch(err.response.status){
            case 406:
              notification.error({
                message: `${t('faq_with_the_same_question_already_exists')}`,
              });
              break;
            default:
              notification.error({
                message: `${t('error_when_updating_faq')}`,
              });
          }
        }
      }
    )
  };

  return (
    <AddNewCarContainer>
      <Form ref={formRef} name="validate_other" onFinish={onFinish}>
        <AddNewCarContainer>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('question_ru')}:</RowTitle>
            </Column>
            <Form.Item name="questionRu" 
              initialValue = {props.data.questionRu}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_question')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder = {t('input_question')}/>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('answer_ru')}:</RowTitle>
            </Column>
            <Form.Item name="answerRu" 
              initialValue = {props.data.answerRu}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_answer')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_answer')} />
            </Form.Item>
          </Row>
          <HorizontalLine/>
          <Marginer direction="vertical" margin={24} />
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('question_be')}:</RowTitle>
            </Column>
            <Form.Item name="questionBe" 
              initialValue = {props.data.questionBe}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_question')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder = {t('input_question')}/>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('answer_be')}:</RowTitle>
            </Column>
            <Form.Item name="answerBe" 
              initialValue = {props.data.answerBe}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_answer')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_answer')} />
            </Form.Item>
          </Row>
          <HorizontalLine/>
          <Marginer direction="vertical" margin={24} />
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('question_en')}:</RowTitle>
            </Column>
            <Form.Item name="questionEn" 
              initialValue = {props.data.questionEn}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_question')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder = {t('input_question')}/>
            </Form.Item>
          </Row>
          <Row>
            <Column>
              <Marginer direction="vertical" margin={4} />
              <RowTitle>{t('answer_en')}:</RowTitle>
            </Column>
            <Form.Item name="answerEn" 
              initialValue = {props.data.answerEn}
              rules={[
                {
                  required: true,
                  message: `${t('please_input_answer')}`,
                },
              ]}
            >
              <Input autoComplete="off" placeholder={t('input_answer')} />
            </Form.Item>
          </Row>
          <Row>
            <button type="submit" className="btn">
              {t('submit')}
            </button>
            <button type="button" className="btn" onClick={onReset}>
              {t('reset')}
            </button>
          </Row>
        </AddNewCarContainer>
      </Form>
    </AddNewCarContainer>
  )
}

export default EditFaqComponent
