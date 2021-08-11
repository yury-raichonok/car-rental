import styled from "styled-components";
import { useTranslation } from 'react-i18next';
import Marginer from '../marginer/Marginer';
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";

import BackgroundImage from '../../images/about.jpg';
import AmazonImage from '../../images/amazon.png';
import JavaImage from '../../images/java.png';
import HibernateImage from '../../images/hibernate.png';
import ReactImage from '../../images/react.png';
import MySQLImage from '../../images/mysql.png';
import SpringImage from '../../images/spring.png';
import TwilioImage from '../../images/twilio.png';

const ImgContainer = styled.img`
  width: 160px;
  object-fit: scale-down;
`;

const PageContainer = styled.div`
  width: 100%;
  min-height: calc( 100vh - 250px );
  display: flex;
  background: url(${BackgroundImage});
  background-position: center canter;
  background-size: cover;

  @media (max-width: 991px) { 
    min-height: calc( 100vh - 306px );
  }
`;

const BackgroundFilter = styled.div`
  width: 100%;
  min-height: 100%;
  background-color: rgba(0, 0, 0, 0.6);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const Title = styled.h2`
  margin: 0;
  font-size: 40px;
  font-weight: 700;
  color: #fff;
  line-height: 1.7;

  @media (max-width: 450px) { 
    font-size: 30px;
  }

  @media (max-width: 300px) { 
    font-size: 25px;
  }
`;

const ContentContainer = styled.div`
  margin-top: 51px;
  width: 1400px;
  display: flex;
  flex-direction: column;
  color: #fff;

  @media (max-width: 1400px) { 
    width: 95%;
  }

  @media (max-width: 991px) { 
    margin-top: 0;
  }
`;

const CarouselContainer = styled.div`
  .react-multi-carousel-track {
    align-items: center;
  }
`;

const TextContainer = styled.div`
  width: 100%;
  text-align: left;
  font-size: 17px;
`;

const responsive = {
  desktop: {
    breakpoint: { max: 3000, min: 1024 },
    items: 4
  },
  tablet: {
    breakpoint: { max: 1024, min: 560 },
    items: 3
  },
  mobile: {
    breakpoint: { max: 560, min: 0 },
    items: 1
  }
};

const AboutUsPageComponent = () => {

  const { t } = useTranslation();

  return (
    <PageContainer>
      <BackgroundFilter>
        <ContentContainer>
          <Title>Car rental web app</Title>
          <TextContainer>
            <p>{t('web_application_for_car_rental_service')}</p>
            <p>{t('user')}:</p>
            <ul>
              <li>
                {t('registration_and_authentication')};
              </li>
              <li>
                {t('confirmation_of_email_address_and_password_recovery')};
              </li>
              <li>
                {t('changing_data_in_your_personal_account_adding_changing_passport_data_and_data_on_a_drivers_license')};
              </li>
              <li>
                {t('confirmation_of_the_specified_personal_data_by_sending_scans_of_documents')};
              </li>
              <li>
                {t('viewing_the_list_of_presented_cars_selection_of_cars_according_to_the_specified_parameters')};
              </li>
              <li>
                {t('placing_an_order_by_a_user_with_confirmed_personal_data')};
              </li>
              <li>
                {t('payment_for_orders_approved_by_the_administrator_in_the_personal_account')};
              </li>
              <li>
                {t('payment_of_fines_invoices_for_repairs_issued_by_the_administrator')};
              </li>
              <li>
                {t('view_current_orders_and_order_history')}.
              </li>
            </ul>

            <p>{t('administrator')}:</p>
            <ul>
              <li>
                {t('possibilities_of_an_ordinary_user')};
              </li>
              <li>
                {t('adding_and_editing_entities_presented_in_the_application')};
              </li>
              <li>
                {t('consideration_of_personal_information_specified_by_users_approval_or_rejection_of_the_application')};
              </li>
              <li>
                {t('consideration_of_orders_placed_by_users_approval_or_rejection_of_orders')};
              </li>
              <li>
                {t('completion_of_current_orders_issuing_a_fine_and_invoicing_for_repairs_to_the_user')};
              </li>
              <li>
                {t('editing_rental_data')}.
              </li>
            </ul>
            
            <p>{t('technologies_used')}:</p>
            <ul>
              <li>
              Frontend - React JS, Axios, Styled components, Antd, Bootstrap;
              </li>
              <li>
              Backend - Java EE, Spring Boot, REST, Hibernate, MySql, Amazon S3, Twilio.
              </li>
            </ul>
          </TextContainer>
          
          <CarouselContainer>
            <Marginer direction="vertical" margin={40} />
            <Carousel
              swipeable={true}
              draggable={true}
              showDots={false}
              responsive={responsive}
              ssr={true} 
              infinite={true}
              autoPlay={true}
              autoPlaySpeed={3000}
              keyBoardControl={true}
              customTransition="all .5"
              transitionDuration={500}
              containerClass="carousel-container"
              removeArrowOnDeviceType={["tablet", "mobile"]}
              dotListClass="custom-dot-list-style"
              itemClass="carousel-item-padding-40-px"
            >
              
              <ImgContainer src={JavaImage} alt="" />
              <ImgContainer src={AmazonImage} alt="" />
              <ImgContainer src={HibernateImage} alt="" />
              <ImgContainer src={ReactImage} alt="" />
              <ImgContainer src={MySQLImage} alt="" />
              <ImgContainer src={SpringImage} alt="" />
              <ImgContainer src={TwilioImage} alt="" />
            </Carousel>
            <Marginer direction="vertical" margin={40} />
          </CarouselContainer>
        </ContentContainer>
      </BackgroundFilter>
    </PageContainer>
  )
}

export default AboutUsPageComponent
