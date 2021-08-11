import React from 'react';
import styled from 'styled-components';
import FastSearchFormComponent from './FastSearchFormComponent';
import FastSearchBackgroundImg from '../../../images/3.jpg';

const FastSearchContainer = styled.div`
  width: 100%;
  min-height: 100vh;
  background: url(${FastSearchBackgroundImg});
  background-position: center;
  background-size: cover;
  box-shadow: 0 16px 20px -12px rgb(0 0 0 / 56%), 0 4px 25px 0 rgb(0 0 0 / 12%), 0 8px 10px -5px rgb(0 0 0 / 20%);

  @media (max-width: 991px) { 
    min-height: calc(100vh - 51px);
  }
`;

const BackgroundFilter = styled.div`
  width: 100%;
  min-height: 100vh;
  background-color: rgba(0, 0, 0, 0.4);
  display: flex;
  flex-direction: column;
`;

const FastSearchInnerContainer = styled.div`
  width: 100%;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const TopSectionComponent = (props) => {

  const { children } = props;

  return (
    <FastSearchContainer>
      <BackgroundFilter>
        { children }
        <FastSearchInnerContainer>
          <FastSearchFormComponent /> 
        </FastSearchInnerContainer>
      </BackgroundFilter>
    </FastSearchContainer>
  )
}

export default TopSectionComponent
