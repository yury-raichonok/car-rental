import PageContainer from './PageContainer';
import Footer from '../footer/Footer';
import NavigationBar from '../navigationBar/NavigationBar';
import ScrollToTop from "react-scroll-to-top";
import styled from 'styled-components';

const ContentContainer = styled.div`
  width: 100%;
  height: 100%;
`;

const PageComponent = (props) => {

  return (
    <div>
      <PageContainer>
        <ScrollToTop smooth color="#fff" style={{backgroundColor: '#f44336'}}/>
        <NavigationBar setUser={props.setUser} user={props.user} />
        <ContentContainer>
          {props.child && props.child() }
        </ContentContainer>        
      </PageContainer>
      <Footer />
    </div>
  )
}

export default PageComponent
