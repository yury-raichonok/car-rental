import React from 'react'
import styled from 'styled-components'

const ButtonWrapper = styled.input`
    border: none;
    outline: none;
    color: #fff;
    padding: 5px 3em;
    font-size: 18px;
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

const SubmitButton = (props) => {
    return (
        <ButtonWrapper type="submit" value={props.signature} />
    )
}

export default SubmitButton
