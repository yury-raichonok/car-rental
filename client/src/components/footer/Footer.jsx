import React from 'react';
import styled from "styled-components";
import { FacebookOutlined, TwitterOutlined, InstagramOutlined, HeartOutlined, CopyrightOutlined } from '@ant-design/icons';
import Marginer from '../marginer/Marginer';
import { useTranslation } from 'react-i18next';
import { Icon28LogoVkColor } from '@vkontakte/icons';
import { AiFillGithub } from 'react-icons/ai';
import { IoLogoLinkedin } from 'react-icons/io';

const FooterContainer = styled.div`
  width: 100%;
  display: flex;
  min-height: 250px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  clear: both;
  position: relative;
  margin-top: -250px;
  background-color: #fff;
  box-shadow: 10px 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);

  a {
    color: rgb(19 85 120);
    font-weight: 500;
  }
`;

const Title = styled.h4`
  font-size: 19px;
  font-weight: 500;
  color: #000;
  display: flex;
`;

const Text = styled.p`
  font-size: 15px;
  font-weight: 300;
  color: #000;
  display: flex;
  margin: 0;
  align-items: center;
  justify-content: center;

  @media (max-width: 400px) { 
    font-size: 12px;
  }
`;

const Orange = styled.span`
  font-size: 19px;
  padding: 0 10px;
  display: flex;
  align-items: center;
  justify-content: center;
    
  @media (max-width: 400px) { 
    font-size: 14px;
  }
`;

const IconsContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  font-size: 24px;

  .github {
    color: #000;
  }

  .linkedin {
    color: #0a66c2;
  }
`;

const Footer = (props) => {

  const { t } = useTranslation();

  return (
    <FooterContainer>
      <Title>{t('about_app')}</Title>
      <Text>{t('car_rental_web_project_final_project_based_on_the_results_of_the_course')}:</Text>
      <Text>{t('technologies_for_the_development_of_enterprise_solutions_in_java')}</Text>
      <Text>{t('from')} <Marginer direction="horizontal" margin={10}/><a onClick={()=> window.open("https://www.it-academy.by/", "_blank")} >IT-Academy</a>.</Text>
      <Marginer direction="verticel" margin={10}/>
      <IconsContainer>
        <a onClick={()=> window.open("https://vk.com/yourrray", "_blank")} ><Icon28LogoVkColor /></a>
        <Marginer direction="horizontal" margin={20}/>
        <div>
          <a onClick={()=> window.open("https://github.com/yury-raichonok", "_blank")} className="github"><AiFillGithub /></a>
          <Marginer direction="vertical" margin={9}/>
        </div>
        <Marginer direction="horizontal" margin={20}/>
        <div>
          <a onClick={()=> window.open("https://www.linkedin.com/in/yury-raichonak/", "_blank")} className="linkedin"><IoLogoLinkedin /></a>
          <Marginer direction="vertical" margin={7}/>
        </div>
      </IconsContainer>
      <Marginer direction="verticel" margin={10}/>
      <Text>{t('made_with')} <Orange><HeartOutlined /></Orange> {t('by_yr')}</Text>
      <Marginer direction="verticel" margin={10}/>
      <Text><CopyrightOutlined /> <Marginer direction="horizontal" margin={10}/> 2021</Text>
    </FooterContainer>
  )
}

export default Footer
