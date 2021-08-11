import styled from "styled-components";
import noImage from '../../../images/no-image.png';

const CardContainer = styled.div`
  display: inline-flex;
  flex-direction: column;
  width: 250px;
  min-height: 200px;
  background-color: #fff;
  box-shadow: 0 0 3px rgba(0, 0, 0, 0.6);
  border-radius: 10px;
  font-family: 'Poppins';
  transition: all 200ms ease-in-out;
  cursor: pointer;

  &:hover {
    box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);
  }

  @media (max-width: 580px) { 
    width: 400px;
  }

  @media (max-width: 400px) { 
    width: 300px;
  }    
`;

const ContextContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  flex: 1;
  padding: 10px;
  justify-content: center;
`;

const Title = styled.h2`
  font-size: 22px;
  margin: 0;
  font-weight: 600;
  color: #000;
  font-family: 'Poppins';
`;

const TopContainer = styled.div`
  width: 100%;
  justify-content: center;
`;

const LogoContainer = styled.div`
  width: 100%;
  justify-content: center;
  height: 150px;
  align-items: center;
  display: inline-flex;

  img {
    max-width: 80%;
    max-height: 80%;
  }
`;

const BrandCard = (props) => {
    
  const { imageLink, name } = props;

  return (
    <CardContainer>
      <TopContainer>
        <LogoContainer>
          {imageLink ? (
            <img src={imageLink} alt={name} />
          ) : (
            <img src={noImage} alt={name} />
          )}
        </LogoContainer>                
      </TopContainer>
      <ContextContainer>
        <Title>{name}</Title>
      </ContextContainer>
    </CardContainer>
  )
}

export default BrandCard
