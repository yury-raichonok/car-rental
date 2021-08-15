import PageContainer from './PageContainer';
import Footer from '../footer/Footer';
import NavigationBar from '../navigationBar/NavigationBar';
import ScrollToTop from "react-scroll-to-top";
import styled from 'styled-components';
import ChatBotComponent from '../chatBotComponent/ChatBotComponent';

const ContentContainer = styled.div`
  width: 100%;
  height: 100%;
`;

const style = {
  bottom: "110px",
  backgroundColor: "#f44336",
};

const PageComponent = (props) => {

  return (
    <div>
      <PageContainer>
        <ScrollToTop smooth color="#fff" style={style}/>
        <NavigationBar setUser={props.setUser} user={props.user} />
        <ChatBotComponent/>
        <ContentContainer>
          {props.child && props.child() }
        </ContentContainer>        
      </PageContainer>
      <Footer />
    </div>
  )
}

export default PageComponent
