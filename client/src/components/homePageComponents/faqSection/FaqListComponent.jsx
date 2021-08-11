import { useState, useEffect } from 'react';
import styled from 'styled-components';
import { Collapse, Spin } from 'antd';
import 'antd/dist/antd.css';
import cookies from 'js-cookie';
import { useTranslation } from 'react-i18next';
import FaqDataService from '../../../services/faq/FaqDataService';

const { Panel } = Collapse;

const ComponentContainer = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;

const FAQContainer = styled.div`
  width: 1400px;
  display: flex;
  flex-direction: column;
  align-items: center;
    
  @media (max-width: 1417px) { 
    width: 100%;
  }
`;

const ContentContainer = styled.div`
  width: 100%;
  max-width: 1300px;
  margin-bottom: 100px;
  text-align: left;
  font-weight: 600;

  .ant-collapse-content-box{
    padding-left: 30px;
    font-weight: 300;
  }

  .ant-collapse-borderless {
    background-color: #fff;
    border: 0;
    font-size: 17px;        
  }
`;

const Title = styled.h1`
  font-size: 32px;
  font-weight: 700;
  color: #000;
  margin: 50px 0 50px 0;

  @media (max-width: 400px) { 
    font-size: 28px;
  }
`;

const WarningText = styled.div`
  color: rgba(100, 100, 100);
  font-weight: 500;
  text-align: center;
  font-size: 18px;
`;

const languages = [
  {
    code: 'be',
    name: 'BY',
    country_code: 'by',
  },
  {
    code: 'ru',
    name: 'RU',
    country_code: 'ru',
  },
  {
    code: 'en',
    name: 'EN',
    country_code: 'gb',
  }
]

const FaqListComponent = () => {

  const currentLanguageCode = cookies.get('i18next') || 'en';
  const currentLanguage = languages.find((l) => l.code === currentLanguageCode);

  const { t } = useTranslation();

  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchFaqs = async () => {
    setLoading(true);
    const res = await FaqDataService.findAll().catch((err) => {
      console.log(err.response)
    });

    if (res) {
      setData(res.data);
    }

    setLoading(false);
  };

  useEffect(() => {
    fetchFaqs();
  }, [currentLanguage]);

  const isDataEmpty = !data || (data && data.length === 0);

  return (
    <ComponentContainer>
      <FAQContainer>
        <Title>{t('frequently_asked_questions')}</Title>
        <ContentContainer>
          {isDataEmpty && !loading && (<WarningText>{t('no_results')}</WarningText>)}
          { loading &&  (<WarningText>{t('loading')} . . . <Spin /></WarningText>)}
          <Collapse bordered={false} >
            {!isDataEmpty && !loading && data.map((row) => (
              <Panel header={row.question} key={row.id}>
                {row.answer}
              </Panel>
            ))}
          </Collapse> 
        </ContentContainer>
      </FAQContainer>
    </ComponentContainer>
  )
}

export default FaqListComponent
