import React from 'react'
import styled from "styled-components";

const ButtonWrapper = styled.button`
    border: none;
    outline: none;
    color: #fff;
    padding: 5px 1em;
    font-size: ${({size}) => size ? size + "px" : "18px"};
    width: ${({width}) => width ? width + "px" : "auto"};
    font-weigth: 700;
    border-radius: 5px;
    background-color: #f44336;
    cursor: pointer;
    transition: all 200ms ease-in-out;

    &:focus {
        outline: none;
    }

    &:hover {
        background-color: #ea5c52;
    }
`;

const Button = (props) => {
    const { size, width, onClick } = props;
    
    return (
        <ButtonWrapper onClick={onClick} width={width} size={size}>{props.children}</ButtonWrapper>
    )
}

export default Button
