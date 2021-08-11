import React from 'react';
import styled from 'styled-components';

const LogoContainer = styled.div`
  display: flex;
  align-items: center;
  cursor: pointer;
  font-family: 'Roboto';
  margin-left: 3%;
`;

const LogoTitle = styled.h2`
  margin: 0;
  font-size: ${({ size }) => size ? size + "px" : "25px"};
  color: #fff;
  font-weight: 900;
`;

const Logo = (props) => {
  const { titleSize } = props;

  return (
    <LogoContainer>
      <LogoTitle size={titleSize} >CarRental</LogoTitle>           
    </LogoContainer>
  )
}

export default Logo
