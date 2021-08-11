import React from 'react'

const CurrencyConverter = (props) => {

  const { value, USDrate, EURrate, RUBrate } = props;

  return (
    <div>
      <div>USD: {value / USDrate}</div>
      <div>EUR: {value / EURrate}</div>
      <div>RUB: {value / RUBrate * 100}</div>
    </div>
  )
}

export default CurrencyConverter
